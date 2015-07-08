package chatterbox;

import java.io.IOException;

import javax.swing.JFrame;

import org.jutils.ui.app.IFrameApp;

import chatterbox.controller.ChatController;
import chatterbox.data.ChatConfig;
import chatterbox.messenger.ChatRoom;
import chatterbox.ui.ChatView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterboxApp implements IFrameApp
{
    private ChatRoom chat;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatterboxApp()
    {
        this.chat = new ChatRoom();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        ChatView frame = new ChatView( chat );
        new ChatController( chat, frame );

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 550, 450 );
        frame.validate();
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );

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
