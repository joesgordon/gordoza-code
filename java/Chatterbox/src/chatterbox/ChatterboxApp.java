package chatterbox;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.ExitListener;
import org.jutils.ui.app.IFrameApp;

import chatterbox.data.ChatterConfig;
import chatterbox.messenger.Chat;
import chatterbox.ui.ChatFrameView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterboxApp implements IFrameApp
{
    /**  */
    private final Chat chat;
    /**  */
    private ChatFrameView frameView;

    /**************************************************************************
     * @param chat
     **************************************************************************/
    public ChatterboxApp( Chat chat )
    {
        this.chat = chat;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        this.frameView = new ChatFrameView( chat );

        JFrame frame = frameView.getView();

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 550, 450 );

        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
        OptionsSerializer<ChatterConfig> options = ChatterboxConstants.getOptions();
        ChatterConfig config = options.getOptions();

        boolean connected = false;

        while( !connected )
        {
            try
            {
                chat.connect( config.chatCfg );

                connected = true;
            }
            catch( IOException ex )
            {
                JOptionPane.showMessageDialog( frameView.getView(),
                    "Unable to connect: " + ex.getMessage(),
                    "Unable to connect", JOptionPane.ERROR_MESSAGE );

                config = frameView.showConfig();

                if( config == null )
                {
                    break;
                }
                else
                {
                    options.write( config );
                }
            }
        }

        if( !connected )
        {
            ExitListener.doDefaultCloseOperation( frameView.getView() );
        }
    }
}
