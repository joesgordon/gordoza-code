package chatterbox.ui;

import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.*;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.model.IView;

import chatterbox.ChatterboxConstants;
import chatterbox.data.ChatterConfig;
import chatterbox.messenger.Chat;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatFrameView implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final ChatView chatView;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatFrameView()
    {
        this.frameView = new StandardFrameView();
        this.chatView = new ChatView();

        frameView.setTitle( "Chatterbox" );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 800, 450 );
        frameView.setContent( chatView.getView() );
        frameView.setToolbar( createToolbar() );

        JFrame frame = frameView.getView();

        frame.addWindowListener( new FrameListener( this ) );
        frame.setIconImages( ChatterboxConstants.getIcons() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        Action action;
        Icon icon;

        icon = IconConstants.getIcon( IconConstants.CALENDAR_16 );
        action = new ActionAdapter( new HistoryListener( this ),
            "View Chat History", icon );
        SwingUtils.addActionToToolbar( toolbar, action );

        icon = IconConstants.getIcon( IconConstants.CONFIG_16 );
        action = new ActionAdapter( ( e ) -> showConfigAndReconnect(),
            "Edit Configuration", icon );
        SwingUtils.addActionToToolbar( toolbar, action );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public ChatView getChatView()
    {
        return chatView;
    }

    /***************************************************************************
     * @return {@code true} if the user clicks "OK", {@code false} otherwise.
     **************************************************************************/
    public ChatterConfig showConfig()
    {
        ChatterConfigView configView = new ChatterConfigView();
        OkDialogView dialogView = new OkDialogView( getView(),
            configView.getView(), ModalityType.DOCUMENT_MODAL,
            OkDialogButtons.OK_CANCEL );

        OptionsSerializer<ChatterConfig> options = ChatterboxConstants.getOptions();
        ChatterConfig config = options.getOptions();
        ChatterConfig newConfig = new ChatterConfig( config );

        configView.setData( newConfig );

        if( dialogView.show( "Chat Configuration", getView().getIconImages(),
            null ) )
        {
            config = configView.getData();
            options.write( config );
        }
        else
        {
            config = null;
        }

        return config;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showConfigAndReconnect()
    {
        OptionsSerializer<ChatterConfig> options = ChatterboxConstants.getOptions();
        ChatterConfig oldConfig = options.getOptions();
        ChatterConfig newConfig = showConfig();

        if( newConfig != null )
        {
            reconnect( oldConfig, newConfig );
        }
    }

    /***************************************************************************
     * @param oldCfg
     * @param newCfg
     **************************************************************************/
    private void reconnect( ChatterConfig oldCfg, ChatterConfig newCfg )
    {
        Chat chat = chatView.getData();

        if( !newCfg.displayName.equals( oldCfg.displayName ) )
        {
            chat.getLocalUser().displayName = newCfg.displayName;
        }

        if( !newCfg.chatCfg.equals( oldCfg.chatCfg ) )
        {
            chat.disconnect();

            try
            {
                chat.connect( newCfg.chatCfg );
            }
            catch( IOException ex )
            {
                JOptionPane.showMessageDialog( getView(),
                    "Cannot connect to chat: " + ex.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE );
                return;
            }
        }

        getView().setTitle( "Chatterbox - " + chat.getLocalUser().userId );
    }

    /***************************************************************************
     * @param chat
     **************************************************************************/
    public void setChat( Chat chat )
    {
        chatView.setData( chat );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FrameListener extends WindowAdapter
    {
        private final ChatFrameView view;

        public FrameListener( ChatFrameView view )
        {
            this.view = view;
        }

        @Override
        public void windowClosing( WindowEvent e )
        {
            Chat chat = view.chatView.getData();

            if( chat != null )
            {
                chat.disconnect();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class HistoryListener implements ActionListener
    {
        private final ChatFrameView view;

        public HistoryListener( ChatFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            JOptionPane.showMessageDialog( view.getView(),
                "This functionality is not yet supported. Good try, though.",
                "Not Supported", JOptionPane.ERROR_MESSAGE );
        }
    }
}
