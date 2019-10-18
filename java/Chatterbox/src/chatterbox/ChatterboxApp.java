package chatterbox;

import java.io.IOException;

import javax.swing.JFrame;

import org.jutils.OptionUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.ExitListener;
import org.jutils.ui.app.IFrameApp;

import chatterbox.data.ChatUser;
import chatterbox.data.ChatterboxOptions;
import chatterbox.messenger.ChatterboxHandler;
import chatterbox.ui.ChatterboxFrameView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterboxApp implements IFrameApp
{
    /**  */
    private final ChatUser user;

    /**  */
    private ChatterboxFrameView frameView;

    /***************************************************************************
     * @param user
     **************************************************************************/
    public ChatterboxApp( ChatUser user )
    {
        this.user = user;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        this.frameView = new ChatterboxFrameView();

        JFrame frame = frameView.getView();

        return frame;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
        OptionsSerializer<ChatterboxOptions> options = ChatterboxConstants.getOptions();
        ChatterboxOptions config = options.getOptions();
        ChatterboxHandler chat = new ChatterboxHandler( user );

        boolean connected = false;

        while( !connected )
        {
            try
            {
                chat.connect( config.chatCfg );
                frameView.setChat( chat );

                connected = true;
            }
            catch( IOException ex )
            {
                OptionUtils.showErrorMessage( frameView.getView(),
                    "Unable to connect: " + ex.getMessage(),
                    "Unable to connect" );

                config = frameView.showConfig();

                if( config == null )
                {
                    break;
                }

                options.write( config );
            }
        }

        if( !connected )
        {
            ExitListener.doDefaultCloseOperation( frameView.getView() );
        }
    }
}
