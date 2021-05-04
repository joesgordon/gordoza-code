package chatterbox.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.jutils.core.OptionUtils;
import org.jutils.core.Utils;
import org.jutils.core.ui.TitleView;
import org.jutils.core.ui.event.ItemActionEvent;
import org.jutils.core.ui.event.ItemActionListener;
import org.jutils.core.ui.model.IDataView;
import org.jutils.core.ui.model.IView;

import chatterbox.data.ChatUser;
import chatterbox.data.DecoratedText;
import chatterbox.data.messages.ChatMessage;
import chatterbox.messenger.ChatHandler;
import chatterbox.model.ChangeType;
import chatterbox.model.IUserListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatView implements IDataView<ChatHandler>
{
    // -------------------------------------------------------------------------
    // GUI Components.
    // -------------------------------------------------------------------------

    /**  */
    private final TitleView view;
    /**  */
    private final AppendableTextPane chatEditorPane;
    /**  */
    private final DecoratedTextView textView;
    /**  */
    private final UserListView usersView;

    // -------------------------------------------------------------------------
    // Helper members
    // -------------------------------------------------------------------------

    /**  */
    private final SimpleDateFormat dateFormatter;
    /**  */
    private ChatHandler conversation;

    // -------------------------------------------------------------------------
    // Listeners to be added to the model.
    // -------------------------------------------------------------------------

    /**  */
    private final IUserListener userListener;
    /**  */
    private final ItemActionListener<ChatMessage> messageReceivedListener;

    /***************************************************************************
     * @param showUserPanel
     **************************************************************************/
    public ChatView()
    {
        this.chatEditorPane = new AppendableTextPane();
        this.textView = new DecoratedTextView();
        this.usersView = new UserListView();
        this.view = new TitleView( "", createView() );

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
        ChatUser me = conversation.getChat().getLocalUser();
        List<ChatUser> users = conversation.getUsers();

        for( ChatUser user : users )
        {
            if( user.available && !user.userId.equals( me.userId ) )
            {
                canSend = true;
            }
        }

        if( canSend )
        {
            ChatMessage msg = new ChatMessage( conversation.getConversationId(),
                me.userId, 0L, 0L, text );

            conversation.sendMessage( msg );

            textView.setData( new DecoratedText() );
        }
        else
        {
            OptionUtils.showErrorMessage( getView(), "Um... No one's listening",
                "Forever Alone" );
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
        ChatUser localUser = conversation.getLocalUser();
        ChatUser sender = conversation.getChat().getUser( message.sender );
        boolean isLocal = localUser.userId.equals( message.sender );
        Color fg = isLocal ? Color.blue : Color.red;
        String username = isLocal
            ? conversation.getChat().getLocalUser().displayName
            : sender.displayName;

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
        return view.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ChatHandler getData()
    {
        return conversation;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( ChatHandler conversation )
    {
        this.conversation = conversation;

        view.setTitle( conversation.info.name );

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
        private final ChatView view;

        public UserListener( ChatView view )
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
        private final ChatView view;

        public MessageReceivedListener( ChatView view )
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
