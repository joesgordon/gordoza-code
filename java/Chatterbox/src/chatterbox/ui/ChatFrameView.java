package chatterbox.ui;

import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.*;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.model.IView;

import chatterbox.ChatterboxConstants;
import chatterbox.data.ChatConfig;
import chatterbox.data.ChatterConfig;
import chatterbox.model.IChat;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatFrameView implements IView<JFrame>
{
    /**  */
    private final ChatView chatView;
    /**  */
    private final StandardFrameView frameView;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatFrameView( IChat chat )
    {
        this.frameView = new StandardFrameView();
        this.chatView = new ChatView( chat );

        frameView.setContent( chatView.getView() );
        frameView.setToolbar( createToolbar() );

        JFrame frame = getView();

        frame.addWindowListener( new FrameListener( this ) );

        frame.setIconImages( IconConstants.loader.getImages(
            IconConstants.CHAT_16, IconConstants.CHAT_32, IconConstants.CHAT_48,
            IconConstants.CHAT_64 ) );

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

        // icon = IconConstants.loader.getIcon( IconConstants.CLOCK_16 );
        // action = new ActionAdapter( new HistoryListener( this ),
        // "View Chat History", icon );
        // SwingUtils.addActionToToolbar( toolbar, action );

        icon = IconConstants.loader.getIcon( IconConstants.CONFIG_16 );
        action = new ActionAdapter( new ConfigListener( this ),
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
            configView.getView(), OkDialogButtons.OK_CANCEL );

        ChatterConfig config = ChatterboxConstants.getOptions().getOptions();

        config.chatCfg.set( chatView.getChat().getConfig() );

        configView.setData( config );

        boolean accept = dialogView.show( "Chat Configuration",
            getView().getIconImages(), null );

        return accept ? configView.getData() : null;
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
            view.chatView.getChat().disconnect();
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

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ConfigListener implements ActionListener
    {
        private final ChatFrameView view;

        public ConfigListener( ChatFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            ChatterConfig newCfg = view.showConfig();

            if( newCfg != null )
            {
                IChat chat = view.chatView.getChat();
                ChatConfig config = chat.getConfig();

                ChatterboxConstants.getOptions().write( newCfg );

                if( newCfg.chatCfg.address.equals( config.address ) ||
                    newCfg.chatCfg.port != config.port )
                {
                    chat.disconnect();

                    try
                    {
                        chat.connect( newCfg.chatCfg );
                    }
                    catch( IOException ex )
                    {
                        JOptionPane.showMessageDialog( view.getView(),
                            "Cannot connect to chat: " + ex.getMessage(),
                            "Connection Error", JOptionPane.ERROR_MESSAGE );
                        return;
                    }
                }

                if( newCfg.chatCfg.displayName.equals( config.displayName ) )
                {
                    chat.getLocalUser().setDisplayName(
                        newCfg.chatCfg.displayName );
                }
            }
        }
    }
}
