package chatterbox;

import java.io.IOException;

import javax.swing.JFrame;

import org.jutils.ui.app.IFrameApp;

import chatterbox.controller.ChatController;
import chatterbox.data.ChatConfig;
import chatterbox.messenger.Chat;
import chatterbox.ui.ChatFrameView;
import chatterbox.view.IChatView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterboxApp implements IFrameApp
{
    private Chat chat;

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
        ChatFrameView frameView = new ChatFrameView();
        IChatView chatView = frameView.getChatView();

        chatView.setChat( chat );

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
        ChatConfig config = ChatterboxConstants.getUserIO().getOptions();

        try
        {
            chat.connect( config.address, config.port );
        }
        catch( IOException ex )
        {
            // TODO Need to ask the user for a different ip/port and try
            // again
            ex.printStackTrace();
            throw new RuntimeException( "Could not connect", ex );
        }
    }
}
