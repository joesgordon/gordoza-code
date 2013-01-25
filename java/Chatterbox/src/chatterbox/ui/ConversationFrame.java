package chatterbox.ui;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.List;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.ui.event.ItemActionListener;

import chatterbox.model.*;
import chatterbox.view.IChatView;
import chatterbox.view.IConversationView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ConversationFrame extends JFrame implements IConversationView
{
    private ConversationPanel conversationPanel;

    private IConversation conversation;

    private IChatView chatView;

    /***************************************************************************
     * 
     **************************************************************************/
    public ConversationFrame( IChatView chatView, IConversation conversation )
    {
        this.chatView = chatView;
        conversationPanel = new ConversationPanel( chatView );

        // ---------------------------------------------------------------------
        // Setup the toolbar.
        // ---------------------------------------------------------------------
        JToolBar toolbar = new JToolBar();
        JButton userAddButton = new JButton(
            IconConstants.loader.getIcon( IconConstants.USER_ADD_24 ) );

        userAddButton.setFocusable( false );
        userAddButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                JOptionPane.showMessageDialog(
                    ConversationFrame.this,
                    "This functionality is not yet supported. Good try, though.",
                    "Not Supported", JOptionPane.ERROR_MESSAGE );
            }
        } );

        toolbar.add( userAddButton );

        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        // ---------------------------------------------------------------------
        // Setup content panel.
        // ---------------------------------------------------------------------
        JPanel contentPanel = new JPanel( new BorderLayout() );

        contentPanel.add( toolbar, BorderLayout.NORTH );
        contentPanel.add( conversationPanel, BorderLayout.CENTER );

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

        setIconImages( IconConstants.loader.getImages(
            IconConstants.IM_USER_16, IconConstants.IM_USER_32 ) );

        setContentPane( contentPanel );
        setConversation( conversation );
        setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        setSize( 450, 450 );
        validate();
        setLocationByPlatform( true );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setConversation( IConversation model )
    {
        String title = "";
        List<IUser> users = model.getUsers();
        conversation = model;
        conversationPanel.setConversation( model );
        if( users.size() > 0 )
        {
            title = users.get( 0 ).getDisplayName();
        }
        else
        {
            int i = 0;
            for( IUser user : users )
            {
                if( i > 0 )
                {
                    title += ", ";
                }

                title += user.getDisplayName();
                i++;
            }
        }

        setTitle( title );
        setTitle( conversation.getConversationId() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addMessageSentListener( ItemActionListener<IChatMessage> l )
    {
        conversationPanel.addMessageSentListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addUserChangedListener( ItemActionListener<String> l )
    {
        conversationPanel.addUserChangedListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void showView()
    {
        if( !isVisible() )
        {
            setVisible( true );
        }
        else
        {
            this.toFront();
        }
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
        conversationPanel.addConversationStartedListener( l );
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
        conversationPanel.addConversationLeftListener( l );
    }
}
