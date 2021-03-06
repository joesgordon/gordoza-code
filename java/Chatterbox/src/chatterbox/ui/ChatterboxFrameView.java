package chatterbox.ui;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import org.jutils.core.IconConstants;
import org.jutils.core.OptionUtils;
import org.jutils.core.SwingUtils;
import org.jutils.core.io.options.OptionsSerializer;
import org.jutils.core.ui.JGoodiesToolBar;
import org.jutils.core.ui.OkDialogView;
import org.jutils.core.ui.OkDialogView.OkDialogButtons;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.model.IView;

import chatterbox.ChatterboxConstants;
import chatterbox.data.ChatterboxOptions;
import chatterbox.messenger.ChatterboxHandler;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterboxFrameView implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final ChatterboxView chatView;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatterboxFrameView()
    {
        this.frameView = new StandardFrameView();
        this.chatView = new ChatterboxView();

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
    public ChatterboxView getChatView()
    {
        return chatView;
    }

    /***************************************************************************
     * @return {@code true} if the user clicks "OK", {@code false} otherwise.
     **************************************************************************/
    public ChatterboxOptions showConfig()
    {
        ChatterboxOptionsView configView = new ChatterboxOptionsView();
        OkDialogView dialogView = new OkDialogView( getView(),
            configView.getView(), ModalityType.DOCUMENT_MODAL,
            OkDialogButtons.OK_CANCEL );

        OptionsSerializer<ChatterboxOptions> options = ChatterboxConstants.getOptions();
        ChatterboxOptions config = options.getOptions();
        ChatterboxOptions newConfig = new ChatterboxOptions( config );

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
        OptionsSerializer<ChatterboxOptions> options = ChatterboxConstants.getOptions();
        ChatterboxOptions oldConfig = options.getOptions();
        ChatterboxOptions newConfig = showConfig();

        if( newConfig != null )
        {
            reconnect( oldConfig, newConfig );
        }
    }

    /***************************************************************************
     * @param oldCfg
     * @param newCfg
     **************************************************************************/
    private void reconnect( ChatterboxOptions oldCfg, ChatterboxOptions newCfg )
    {
        ChatterboxHandler chat = chatView.getData();

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
                OptionUtils.showErrorMessage( getView(),
                    "Cannot connect to chat: " + ex.getMessage(),
                    "Connection Error" );
                return;
            }
        }
    }

    /***************************************************************************
     * @param chat
     **************************************************************************/
    public void setChat( ChatterboxHandler chat )
    {
        chatView.setData( chat );

        getView().setTitle( "Chatterbox - " + chat.getLocalUser().userId );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FrameListener extends WindowAdapter
    {
        /**  */
        private final ChatterboxFrameView view;

        /**
         * @param view
         */
        public FrameListener( ChatterboxFrameView view )
        {
            this.view = view;
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public void windowClosing( WindowEvent e )
        {
            ChatterboxHandler chat = view.chatView.getData();

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
        /**  */
        private final ChatterboxFrameView view;

        /**
         * @param view
         */
        public HistoryListener( ChatterboxFrameView view )
        {
            this.view = view;
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public void actionPerformed( ActionEvent e )
        {
            OptionUtils.showErrorMessage( view.getView(),
                "This functionality is not yet supported. Good try, though.",
                "Not Supported" );
        }
    }
}
