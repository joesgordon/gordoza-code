package chatterbox.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.ui.StatusBarPanel;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

import chatterbox.model.*;
import chatterbox.view.IChatView;
import chatterbox.view.IConversationView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatView extends JFrame implements IChatView
{
    /**  */
    private ConversationPanel conversationPanel;

    /**  */
    private ItemActionList<List<IUser>> conversationStartedListeners;

    /**  */
    private ItemActionList<String> displayNameChangedListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatView( final IChatRoom chat )
    {
        conversationStartedListeners = new ItemActionList<List<IUser>>();
        displayNameChangedListeners = new ItemActionList<String>();

        // ---------------------------------------------------------------------
        // Setup components.
        // ---------------------------------------------------------------------
        conversationPanel = new ConversationPanel( this );

        JPanel mainPanel = new JPanel( new GridBagLayout() );

        mainPanel.add( conversationPanel, new GridBagConstraints( 0, 0, 1, 1,
            1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Setup menu bar
        // ---------------------------------------------------------------------

        JToolBar toolbar = new JToolBar();

        JButton historyButton = new JButton(
            IconConstants.loader.getIcon( IconConstants.CLOCK_24 ) );
        JButton configButton = new JButton(
            IconConstants.loader.getIcon( IconConstants.CONFIG_24 ) );

        historyButton.setToolTipText( "View Chat History" );
        historyButton.setFocusable( false );
        historyButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                JOptionPane.showMessageDialog(
                    ChatView.this,
                    "This functionality is not yet supported. Good try, though.",
                    "Not Supported", JOptionPane.ERROR_MESSAGE );
            }
        } );

        configButton.setToolTipText( "Edit Configuration" );
        configButton.setFocusable( false );
        configButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                new ConfigDialog( ChatView.this, chat );
                // String username = JOptionPane.showInputDialog(
                // "New Username",
                // chat.getLocalUser() );
                // if( username != null )
                // {
                // chat.getLocalUser().setDisplayName( username );
                // }
            }
        } );

        toolbar.add( historyButton );
        toolbar.add( configButton );

        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        // ---------------------------------------------------------------------
        // Setup the content panel.
        // ---------------------------------------------------------------------
        JPanel contentPanel = new JPanel( new BorderLayout() );

        contentPanel.add( toolbar, BorderLayout.NORTH );
        contentPanel.add( mainPanel, BorderLayout.CENTER );
        contentPanel.add( new StatusBarPanel().getView(), BorderLayout.SOUTH );

        // ---------------------------------------------------------------------
        // Setup this frame.
        // ---------------------------------------------------------------------
        addWindowListener( new WindowAdapter()
        {
            public void windowClosing( WindowEvent e )
            {
                conversationPanel.leaveConversation();
            }
        } );

        setIconImages( IconConstants.loader.getImages( IconConstants.CHAT_16,
            IconConstants.CHAT_32, IconConstants.CHAT_48, IconConstants.CHAT_64 ) );

        setContentPane( contentPanel );
        setChat( chat );
        conversationPanel.setConversation( chat.getDefaultConversation() );
        setTitle( "Chatterbox" );
    }

    /***************************************************************************
     * @param chatModel
     **************************************************************************/
    public void setChat( IChatRoom chatModel )
    {
        ;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IConversationView createConversationView( IConversation conversation )
    {
        return new ConversationFrame( this, conversation );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IConversationView getDefaultConversationView()
    {
        return conversationPanel;
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
    public void addDisplayNameChangedListener( ItemActionListener<String> l )
    {
        displayNameChangedListeners.addListener( l );
    }
}
