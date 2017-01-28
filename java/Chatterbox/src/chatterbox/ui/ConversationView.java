package chatterbox.ui;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.text.*;

import org.jutils.IconConstants;
import org.jutils.Utils;
import org.jutils.ui.FontChooserView;
import org.jutils.ui.FontChooserView.FontDescription;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.event.*;
import org.jutils.ui.model.IDataView;

import chatterbox.data.ChatUser;
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
    private final JTextPane msgEditorPane;
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
        this.usersView = new UserView();
        this.msgEditorPane = new JTextPane();
        this.view = createView();

        msgEditorPane.getDocument().addDocumentListener(
            new GrowingTextDocumentListener( msgEditorPane ) );

        this.dateFormatter = new SimpleDateFormat( "(MM-dd-yy HH:mm:ss)" );

        // ---------------------------------------------------------------------
        // Setup listeners.
        // ---------------------------------------------------------------------
        this.userListener = new UserListener( this );

        this.messageReceivedListener = new MessageReceivedListener( this );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        JScrollPane chatScrollPane = new JScrollPane( chatEditorPane );
        BottomScroller chatScroller = new BottomScroller( chatEditorPane );

        chatEditorPane.setEditable( false );
        chatEditorPane.addComponentListener( chatScroller );

        chatScrollPane.setPreferredSize( new Dimension( 100, 100 ) );
        chatScrollPane.setMinimumSize( new Dimension( 100, 100 ) );

        panel.add( chatScrollPane,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );
        panel.add( createContentPanel(),
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        panel.add( usersView.getView(),
            new GridBagConstraints( 1, 0, 1, 2, 0.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        return panel;
    }

    /**
     * @param textPane
     */
    private void addEnterHook( JTextPane textPane )
    {
        KeyStroke ks;
        String aname;
        Action action;
        ActionMap amap;
        InputMap imap;

        ks = KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 );
        aname = "SEND_MESSAGE";
        action = new ActionAdapter( new EnterListener( this ), aname, null );
        amap = textPane.getActionMap();
        imap = textPane.getInputMap();
        imap.put( ks, aname );
        amap.put( aname, action );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createContentPanel()
    {
        JPanel contentPanel = new JPanel( new GridBagLayout() );
        ActionListener fontButtonListener = new FontListener( this );

        contentPanel.setBorder( BorderFactory.createEtchedBorder() );

        JScrollPane msgScrollPane = new GrowingScrollPane( msgEditorPane );

        JToolBar toolbar = new JToolBar();
        BottomScroller bottomScroller = new BottomScroller( msgEditorPane );
        JButton fontButton = new JButton( "Font" );

        fontButton.setIcon( IconConstants.getIcon( IconConstants.FONT_24 ) );
        fontButton.addActionListener( fontButtonListener );

        this.msgEditorPane.addComponentListener( bottomScroller );
        addEnterHook( msgEditorPane );

        msgScrollPane.setMinimumSize( new Dimension( 100, 48 ) );
        msgScrollPane.setMaximumSize( new Dimension( 100, 150 ) );
        msgScrollPane.setBorder( null );
        msgScrollPane.setBorder(
            BorderFactory.createMatteBorder( 1, 0, 0, 0, Color.gray ) );

        toolbar.add( fontButton );
        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        contentPanel.add( toolbar,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        contentPanel.add( msgScrollPane,
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        return contentPanel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void sendMessage()
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
            AttributeSet as = msgEditorPane.getStyledDocument().getDefaultRootElement().getAttributes();
            List<MessageAttributeSet> attributes = new ArrayList<>();
            String text = msgEditorPane.getText();

            attributes.add( new MessageAttributeSet( as, 0, text.length() ) );

            ChatMessage msg = new ChatMessage( conversation.getConversationId(),
                conversation.getChat().getLocalUser(), 0L, 0L, text,
                attributes );
            msgEditorPane.setText( "" );

            conversation.sendMessage( msg );
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
        StyledDocument doc = chatEditorPane.getStyledDocument();
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
    private static class BottomScroller extends ComponentAdapter
    {
        private JTextPane textPane;

        public BottomScroller( JTextPane textPane )
        {
            this.textPane = textPane;
        }

        private void scrollToBottom()
        {
            textPane.scrollRectToVisible(
                new Rectangle( 0, textPane.getHeight(), 1, 1 ) );
        }

        @Override
        public void componentResized( ComponentEvent e )
        {
            scrollToBottom();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class AppendableTextPane extends JTextPane
    {
        private static final long serialVersionUID = 1L;

        public AppendableTextPane()
        {
            super();
        }

        public void appendText( String text )
        {
            appendText( text, null );
        }

        public void appendText( String text, AttributeSet a )
        {
            Document doc = getDocument();
            try
            {
                doc.insertString( doc.getLength(), text, a );
            }
            catch( BadLocationException ex )
            {
                ex.printStackTrace();
            }
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

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FontListener implements ActionListener
    {
        private final ConversationView view;

        public FontListener( ConversationView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            FontChooserView fontChooser = new FontChooserView();
            OkDialogView dialogView = new OkDialogView( view.getView(),
                fontChooser.getView(), ModalityType.DOCUMENT_MODAL );

            FontDescription desc = new FontDescription();

            desc.setAttributes( view.msgEditorPane.getCharacterAttributes() );
            fontChooser.setData( desc );
            dialogView.pack();

            if( dialogView.show() )
            {
                SimpleAttributeSet s = new SimpleAttributeSet();
                fontChooser.getData().getAttributes( s );

                if( s != null )
                {
                    view.msgEditorPane.setCharacterAttributes( s, true );
                }
            }
        }
    }

    private static class EnterListener implements ActionListener
    {
        private final ConversationView view;

        public EnterListener( ConversationView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            view.sendMessage();
        }
    }
}
