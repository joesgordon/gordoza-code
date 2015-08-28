package chatterbox;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jutils.io.OptionsSerializer;
import org.jutils.ui.app.IFrameApp;

import chatterbox.controller.ChatController;
import chatterbox.data.ChatterConfig;
import chatterbox.messenger.Chat;
import chatterbox.ui.ChatFrameView;
import chatterbox.ui.ChatView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterboxApp implements IFrameApp
{
    private final Chat chat;
    private ChatFrameView frameView;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatterboxApp()
    {
        this.chat = new Chat();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        this.frameView = new ChatFrameView( chat );

        ChatView chatView = frameView.getChatView();

        new ChatController( chat, chatView );

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
        OptionsSerializer<ChatterConfig> userio = ChatterboxConstants.getUserIO();
        ChatterConfig config = userio.getOptions();

        while( config != null )
        {
            try
            {
                chat.connect( config.chatCfg );
                userio.write();
                break;
            }
            catch( IOException ex )
            {
                JOptionPane.showMessageDialog( frameView.getView(),
                    "Unable to connect: " + ex.getMessage(),
                    "Unable to connect", JOptionPane.ERROR_MESSAGE );

                config = frameView.showConfig();
            }
        }
    }
}
