package chatterbox.ui;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.text.*;

import org.jutils.IconConstants;
import org.jutils.Utils;
import org.jutils.io.LogUtils;
import org.jutils.ui.FontChooserDialog;
import org.jutils.ui.event.*;

import chatterbox.data.UiChatMessage;
import chatterbox.model.*;
import chatterbox.view.IChatView;
import chatterbox.view.IConversationView;

public class ConversationPanel extends JPanel implements IConversationView
{
    // -------------------------------------------------------------------------
    // GUI Components.
    // -------------------------------------------------------------------------
    private AppendableTextPane chatEditorPane;

    private JTextPane msgEditorPane;

    private JList<IUser> userList;

    private DefaultListModel<IUser> userModel;

    // -------------------------------------------------------------------------
    // Helper members
    // -------------------------------------------------------------------------

    private SimpleDateFormat dateFormatter;

    private IConversation conversation;

    // -------------------------------------------------------------------------
    // Listener Lists
    // -------------------------------------------------------------------------

    private ItemActionList<IChatMessage> msgSentListeners;

    private ItemActionList<String> userChangedListeners;

    private ItemActionList<List<IUser>> conversationStartedListeners;

    private ItemActionList<Object> conversationLeftListeners;

    // -------------------------------------------------------------------------
    // Listeners to be added to the model.
    // -------------------------------------------------------------------------

    private ItemActionListener<IUser> userAddedListener;

    private ItemActionListener<IUser> userAvailableListener;

    private ItemActionListener<IUser> userUnavailableListener;

    private ItemActionListener<IChatMessage> messageReceivedListener;

    private ItemActionListener<IUser> userRemovedListener;

    private IChatView chatView;

    /***************************************************************************
     * @param showUserPanel
     **************************************************************************/
    public ConversationPanel( IChatView chatView )
    {
        this.chatView = chatView;
        msgSentListeners = new ItemActionList<IChatMessage>();
        conversationStartedListeners = new ItemActionList<List<IUser>>();
        userChangedListeners = new ItemActionList<String>();
        conversationLeftListeners = new ItemActionList<Object>();
        dateFormatter = new SimpleDateFormat( "(MM-dd-yy HH:mm:ss)" );

        // ---------------------------------------------------------------------
        // Setup listeners.
        // ---------------------------------------------------------------------
        userAddedListener = new ItemActionListener<IUser>()
        {
            @Override
            public void actionPerformed( ItemActionEvent<IUser> event )
            {
                userModel.addElement( event.getItem() );
            }
        };

        userAvailableListener = new ItemActionListener<IUser>()
        {
            @Override
            public void actionPerformed( ItemActionEvent<IUser> event )
            {
                LogUtils.printDebug( event.getItem().getDisplayName() +
                    " is now available" );
                int index = userModel.indexOf( event.getItem() );
                userModel.set( index, event.getItem() );
                userList.repaint();
            }
        };

        userUnavailableListener = new ItemActionListener<IUser>()
        {
            @Override
            public void actionPerformed( ItemActionEvent<IUser> event )
            {
                userList.repaint();
            }
        };

        userRemovedListener = new ItemActionListener<IUser>()
        {
            @Override
            public void actionPerformed( ItemActionEvent<IUser> event )
            {
                userModel.removeElement( event.getItem() );
            }
        };

        messageReceivedListener = new ItemActionListener<IChatMessage>()
        {
            @Override
            public void actionPerformed( ItemActionEvent<IChatMessage> event )
            {
                addMessage( event.getItem() );
            }
        };

        // ---------------------------------------------------------------------
        // Setup GUI listeners.
        // ---------------------------------------------------------------------
        ActionListener fontButtonListener = new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                FontChooserDialog fontChooser = new FontChooserDialog(
                    ( JFrame )SwingUtilities.getWindowAncestor( ConversationPanel.this ) );

                fontChooser.setAttributes( msgEditorPane.getCharacterAttributes() );
                fontChooser.pack();
                fontChooser.setLocationRelativeTo( ConversationPanel.this );
                fontChooser.setVisible( true );

                if( fontChooser.getOption() == JOptionPane.OK_OPTION )
                {
                    AttributeSet s = fontChooser.getAttributes();

                    msgEditorPane.setCharacterAttributes( s, true );
                }
            }
        };

        KeyAdapter msgPaneKeyListener = new KeyAdapter()
        {

            @SuppressWarnings( "deprecation")
            @Override
            public void keyPressed( KeyEvent evt )
            {
                if( evt.getKeyCode() == KeyEvent.VK_ENTER &&
                    evt.isControlDown() )
                {
                    evt.setKeyCode( KeyEvent.VK_ENTER );
                    // The following uses a deperecated API,
                    // it seems that there is not workaround
                    evt.setModifiers( 0 );
                    return;
                }
                else if( evt.getKeyCode() == KeyEvent.VK_ENTER )
                {
                    evt.consume();
                    sendMessage();
                }
            }
        };

        // ---------------------------------------------------------------------
        // Setup message panel.
        // ---------------------------------------------------------------------
        JPanel contentPanel = new JPanel( new GridBagLayout() );
        contentPanel.setBorder( BorderFactory.createEtchedBorder() );

        msgEditorPane = new GrowingTextPane();
        JScrollPane msgScrollPane = new GrowingScrollPane( msgEditorPane );

        JToolBar toolbar = new JToolBar();
        BottomScroller bottomScroller = new BottomScroller( msgEditorPane );
        JButton fontButton = new JButton( "Font" );

        fontButton.setIcon( IconConstants.loader.getIcon( IconConstants.FONT_24 ) );
        fontButton.addActionListener( fontButtonListener );

        msgEditorPane.addComponentListener( bottomScroller );
        msgEditorPane.addKeyListener( msgPaneKeyListener );

        msgScrollPane.setMinimumSize( new Dimension( 100, 48 ) );
        msgScrollPane.setMaximumSize( new Dimension( 100, 150 ) );
        msgScrollPane.setBorder( null );
        msgScrollPane.setBorder( BorderFactory.createMatteBorder( 1, 0, 0, 0,
            Color.gray ) );

        toolbar.add( fontButton );
        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        contentPanel.add( toolbar, new GridBagConstraints( 0, 0, 1, 1, 1.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                0, 0, 0, 0 ), 0, 0 ) );
        contentPanel.add( msgScrollPane, new GridBagConstraints( 0, 1, 1, 1,
            1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Setup main panel.
        // ---------------------------------------------------------------------
        setLayout( new GridBagLayout() );

        userModel = new DefaultListModel<IUser>();
        userList = new JList<IUser>( userModel );
        JScrollPane userScrollPane = new JScrollPane( userList );

        chatEditorPane = new AppendableTextPane();
        JScrollPane chatScrollPane = new JScrollPane( chatEditorPane );
        BottomScroller chatScroller = new BottomScroller( chatEditorPane );

        userList.setCellRenderer( new UserListCellRenderer() );
        userList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        userList.addMouseListener( new MouseAdapter()
        {
            public void mouseClicked( MouseEvent e )
            {
                JOptionPane.showMessageDialog(
                    ConversationPanel.this,
                    "This functionality is not yet supported. Good try, though.",
                    "Not Supported", JOptionPane.ERROR_MESSAGE );

                if( "".length() > 0 && e.getClickCount() == 2 )
                {
                    int index = userList.locationToIndex( e.getPoint() );
                    if( index > -1 )
                    {
                        ArrayList<IUser> users = new ArrayList<IUser>();
                        ListModel<IUser> dlm = userList.getModel();
                        Object item = dlm.getElementAt( index );

                        users.add( ( IUser )item );
                        conversationStartedListeners.fireListeners(
                            ConversationPanel.this, users );
                        userList.ensureIndexIsVisible( index );
                    }
                }
            }
        } );

        chatEditorPane.setEditable( false );

        chatEditorPane.addComponentListener( chatScroller );
        chatScrollPane.setPreferredSize( new Dimension( 100, 100 ) );
        chatScrollPane.setMinimumSize( new Dimension( 100, 100 ) );

        userScrollPane.setPreferredSize( new Dimension( 175, 100 ) );
        userScrollPane.setMinimumSize( new Dimension( 100, 100 ) );

        JLabel userLabel = new JLabel( "Users:" );
        userLabel.setFocusable( false );

        add( chatScrollPane, new GridBagConstraints( 0, 0, 1, 2, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 4,
                4, 4, 4 ), 0, 0 ) );
        add( contentPanel, new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 4,
                4, 4, 4 ), 0, 0 ) );

        add( userLabel, new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 4, 4,
                4, 4 ), 0, 0 ) );
        add( userScrollPane, new GridBagConstraints( 1, 1, 1, 2, 0.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 4, 4,
                4, 4 ), 0, 0 ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void sendMessage()
    {
        boolean canSend = false;
        List<IUser> users = conversation.getUsers();

        for( IUser user : users )
        {
            if( user.isAvailable() )
            {
                canSend = true;
            }
        }

        if( canSend )
        {
            UiChatMessage msg = new UiChatMessage(
                conversation.getChat().getLocalUser(),
                msgEditorPane.getStyledDocument(),
                conversation.getConversationId() );
            msgEditorPane.setText( "" );

            msgSentListeners.fireListeners( this, msg );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void leaveConversation()
    {
        conversationLeftListeners.fireListeners( this, null );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void setVisible( boolean visible )
    {
        super.setVisible( visible );

        if( visible )
        {
            msgEditorPane.requestFocus();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setConversation( IConversation model )
    {
        conversation = model;

        List<IUser> users = model.getUsers();
        for( IUser user : users )
        {
            userModel.addElement( user );
        }

        model.addUserAddedListener( userAddedListener );
        model.addUserAvailableListener( userAvailableListener );
        model.addUserUnavailableListener( userUnavailableListener );
        model.addUserRemovedListener( userRemovedListener );
        model.addMessageReceivedListener( messageReceivedListener );
    }

    /***************************************************************************
     * @param message
     **************************************************************************/
    private void addMessage( IChatMessage message )
    {
        StyledDocument doc = chatEditorPane.getStyledDocument();
        SimpleAttributeSet a = new SimpleAttributeSet();
        Color fg = message.isLocalUser() ? Color.blue : Color.red;
        String username = message.isLocalUser() ? conversation.getChat().getLocalUser().getDisplayName()
            : message.getSender().getDisplayName();

        StyleConstants.setFontFamily( a, "Dialog" );
        StyleConstants.setFontSize( a, 12 );
        StyleConstants.setForeground( a, fg );
        chatEditorPane.appendText( dateFormatter.format( message.getTime() ), a );

        StyleConstants.setBold( a, true );
        chatEditorPane.appendText( " " + username, a );

        chatEditorPane.appendText( ": " + message.getText() + Utils.NEW_LINE );

        a = new SimpleAttributeSet();
        StyleConstants.setLineSpacing( a, 0.3f );
        doc.setParagraphAttributes( doc.getLength() - 1, doc.getLength(), a,
            false );

        a = new SimpleAttributeSet();
        StyleConstants.setLineSpacing( a, 0.0f );
        doc.setParagraphAttributes( doc.getLength(), doc.getLength(), a, false );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addMessageSentListener( ItemActionListener<IChatMessage> l )
    {
        msgSentListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addUserChangedListener( ItemActionListener<String> l )
    {
        userChangedListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void showView()
    {
        ;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IConversation getConversation()
    {
        return conversation;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addConversationStartedListener(
        ItemActionListener<List<IUser>> l )
    {
        conversationStartedListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IChatView getChatView()
    {
        return chatView;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addConversationLeftListener( ItemActionListener<Object> l )
    {
        conversationLeftListeners.addListener( l );
    }
}

/*******************************************************************************
 * 
 ******************************************************************************/
class AppendableTextPane extends JTextPane
{
    /***************************************************************************
     * 
     **************************************************************************/
    public AppendableTextPane()
    {
        super();
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void appendText( String text )
    {
        appendText( text, null );
    }

    /***************************************************************************
     * @param text
     * @param a
     **************************************************************************/
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

class BottomScroller extends ComponentAdapter
{
    private JTextPane textPane;

    public BottomScroller( JTextPane textPane )
    {
        this.textPane = textPane;
    }

    private void scrollToBottom()
    {
        textPane.scrollRectToVisible( new Rectangle( 0, textPane.getHeight(),
            1, 1 ) );
    }

    @Override
    public void componentResized( ComponentEvent e )
    {
        scrollToBottom();
    }
}
