package chatterbox.ui;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.*;
import javax.swing.text.*;

import org.jutils.Utils;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.IDataView;
import org.jutils.ui.model.IView;

import chatterbox.data.ChatUser;
import chatterbox.data.DecoratedText;
import chatterbox.data.messages.ChatMessage;
import chatterbox.messenger.Chat;
import chatterbox.messenger.Conversation;
import chatterbox.model.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ConversationView implements IDataView<Conversation>
{
    // -------------------------------------------------------------------------
    // GUI Components.
    // -------------------------------------------------------------------------

    /**  */
    private final JPanel view;
    /**  */
    private final AppendableTextPane chatEditorPane;
    /**  */
    private final DecoratedTextView textView;
    /**  */
    private final UserView usersView;

    // -------------------------------------------------------------------------
    // Helper members
    // -------------------------------------------------------------------------

    /**  */
    private final SimpleDateFormat dateFormatter;
    /**  */
    private Conversation conversation;

    // -------------------------------------------------------------------------
    // Listeners to be added to the model.
    // -------------------------------------------------------------------------

    /**  */
    private final IUserListener userListener;
    /**  */
    private final ItemActionListener<ChatMessage> messageReceivedListener;

    /**  */
    private final Chat chat;

    /***************************************************************************
     * @param showUserPanel
     **************************************************************************/
    public ConversationView( Chat chat )
    {
        this.chat = chat;

        this.chatEditorPane = new AppendableTextPane();
        this.textView = new DecoratedTextView();
        this.usersView = new UserView();
        this.view = createView();

        this.dateFormatter = new SimpleDateFormat( "(MM-dd-yy HH:mm:ss)" );

        // ---------------------------------------------------------------------
        // Setup listeners.
        // ---------------------------------------------------------------------
        this.userListener = new UserListener( this );

        this.messageReceivedListener = new MessageReceivedListener( this );

        textView.addEnterListener( ( e ) -> sendMessage( e.getItem() ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        JScrollPane chatScrollPane = new JScrollPane(
            chatEditorPane.getView() );
        BottomScroller chatScroller = new BottomScroller(
            chatEditorPane.getView() );

        chatEditorPane.getView().setEditable( false );
        chatEditorPane.getView().addComponentListener( chatScroller );

        chatScrollPane.setPreferredSize( new Dimension( 100, 100 ) );
        chatScrollPane.setMinimumSize( new Dimension( 100, 100 ) );

        panel.add( chatScrollPane,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );
        panel.add( textView.getView(),
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        panel.add( usersView.getView(),
            new GridBagConstraints( 1, 0, 1, 2, 0.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        return panel;
    }

    /***************************************************************************
     * @param decoratedText
     **************************************************************************/
    private void sendMessage( DecoratedText text )
    {
        boolean canSend = false;

        List<ChatUser> users = conversation.getUsers();

        for( ChatUser user : users )
        {
            if( user.available )
            {
                canSend = true;
            }
        }

        if( canSend )
        {
            ChatMessage msg = new ChatMessage( conversation.getConversationId(),
                conversation.getChat().getLocalUser(), 0L, 0L, text );

            conversation.sendMessage( msg );

            textView.setData( new DecoratedText() );
        }
        else
        {
            JOptionPane.showMessageDialog( getView(),
                "Um... No one's listening", "Forever Alone",
                JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void leaveConversation()
    {
        conversation.leaveConversation();
    }

    /***************************************************************************
     * @param message
     **************************************************************************/
    private void addMessage( ChatMessage message )
    {
        StyledDocument doc = chatEditorPane.getView().getStyledDocument();
        SimpleAttributeSet a = new SimpleAttributeSet();
        ChatUser localUser = chat.getLocalUser();
        boolean isLocal = localUser.equals( message.sender );
        Color fg = isLocal ? Color.blue : Color.red;
        String username = isLocal
            ? conversation.getChat().getLocalUser().displayName
            : message.sender.displayName;

        StyleConstants.setFontFamily( a, "Dialog" );
        StyleConstants.setFontSize( a, 12 );
        StyleConstants.setForeground( a, fg );
        chatEditorPane.appendText( dateFormatter.format( message.txTime ), a );

        StyleConstants.setBold( a, true );
        chatEditorPane.appendText( " " + username, a );

        chatEditorPane.appendText( ": " + message.text + Utils.NEW_LINE );

        a = new SimpleAttributeSet();
        StyleConstants.setLineSpacing( a, 0.3f );
        doc.setParagraphAttributes( doc.getLength() - 1, doc.getLength(), a,
            false );

        a = new SimpleAttributeSet();
        StyleConstants.setLineSpacing( a, 0.0f );
        doc.setParagraphAttributes( doc.getLength(), doc.getLength(), a,
            false );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Conversation getData()
    {
        return conversation;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Conversation conversation )
    {
        this.conversation = conversation;

        usersView.setData( conversation.getUsers() );

        conversation.addUserListener( userListener );
        conversation.addMessageReceivedListener( messageReceivedListener );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class AppendableTextPane implements IView<JTextPane>
    {
        private final JTextPane field;

        public AppendableTextPane()
        {
            this.field = new JTextPane();
        }

        public void appendText( String text )
        {
            appendText( text, null );
        }

        public void appendText( String text, AttributeSet a )
        {
            Document doc = field.getDocument();
            try
            {
                doc.insertString( doc.getLength(), text, a );
            }
            catch( BadLocationException ex )
            {
                ex.printStackTrace();
            }
        }

        @Override
        public JTextPane getView()
        {
            return field;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class UserListener implements IUserListener
    {
        private final ConversationView view;

        public UserListener( ConversationView view )
        {
            this.view = view;
        }

        @Override
        public void userChanged( ChatUser user, ChangeType change )
        {
            view.usersView.setData( view.getData().getUsers() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class MessageReceivedListener
        implements ItemActionListener<ChatMessage>
    {
        private final ConversationView view;

        public MessageReceivedListener( ConversationView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<ChatMessage> event )
        {
            view.addMessage( event.getItem() );
        }
    }
}
