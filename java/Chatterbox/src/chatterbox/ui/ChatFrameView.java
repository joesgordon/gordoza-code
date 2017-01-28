package chatterbox.ui;

import java.awt.Dialog.ModalityType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.*;

import org.jutils.*;
import org.jutils.io.LogUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.*;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.model.IView;

import chatterbox.ChatterboxConstants;
import chatterbox.data.ChatConfig;
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
    public ChatFrameView( Chat chat )
    {
        this.frameView = new StandardFrameView();
        this.chatView = new ChatView( chat );

        frameView.setContent( chatView.getView() );
        frameView.setToolbar( createToolbar() );

        JFrame frame = getView();

        frame.addWindowListener( new FrameListener( this ) );

        frame.setIconImages( ChatterboxConstants.getIcons() );

        frame.setTitle( "Chatterbox" );
    }

    /***************************************************************************
     * 
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

        // icon = IconConstants.getIcon( IconConstants.CLOCK_16 );
        // action = new ActionAdapter( new HistoryListener( this ),
        // "View Chat History", icon );
        // SwingUtils.addActionToToolbar( toolbar, action );

        icon = IconConstants.getIcon( IconConstants.CONFIG_16 );
        action = new ActionAdapter( ( e ) -> showConfig(), "Edit Configuration",
            icon );
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
            newConfig = configView.getData();
            options.write();
            reconnect( config, newConfig );
            config = newConfig;
        }
        else
        {
            config = null;
        }

        return config;
    }

    private void reconnect( ChatterConfig oldCfg, ChatterConfig newCfg )
    {
        Chat chat = chatView.getChat();
        ChatConfig config = chat.getConfig();

        if( !newCfg.chatCfg.address.equals( config.address ) ||
            newCfg.chatCfg.port != config.port )
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

        if( !newCfg.displayName.equals( oldCfg.displayName ) )
        {
            chat.getLocalUser().displayName = newCfg.displayName;
        }
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
            // showMessage();
            Stopwatch watch = new Stopwatch();
            view.chatView.getChat().disconnect();
            watch.stop();

            LogUtils.printDebug( "Disconnected in %d ms", watch.getElapsed() );
        }

        private void showMessage()
        {
            JOptionPane pane = new JOptionPane( "Disconnecting",
                JOptionPane.INFORMATION_MESSAGE );

            JDialog dialog = pane.createDialog( view.getView(),
                "Disconnecting" );

            dialog.setModalityType( ModalityType.MODELESS );

            dialog.pack();
            dialog.setVisible( true );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    // private static class HistoryListener implements ActionListener
    // {
    // private final ChatFrameView view;
    //
    // public HistoryListener( ChatFrameView view )
    // {
    // this.view = view;
    // }
    //
    // @Override
    // public void actionPerformed( ActionEvent e )
    // {
    // JOptionPane.showMessageDialog( view.getView(),
    // "This functionality is not yet supported. Good try, though.",
    // "Not Supported", JOptionPane.ERROR_MESSAGE );
    // }
    // }
}
