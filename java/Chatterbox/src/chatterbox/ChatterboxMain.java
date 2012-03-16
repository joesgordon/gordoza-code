package chatterbox;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jutils.ui.FrameRunner;

import chatterbox.controller.ChatController;
import chatterbox.messager.Chat;
import chatterbox.ui.ChatFrame;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterboxMain extends FrameRunner
{
    private Chat messager;
    private String address;
    private int port;

    public ChatterboxMain()
    {
        messager = new Chat();
        address = "238.192.69.69";
        port = 6969;
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new ChatterboxMain() );
    }

    @Override
    protected JFrame createFrame()
    {
        ChatFrame frame = new ChatFrame( messager );
        new ChatController( messager, frame );

        try
        {
            messager.connect( address, port );

            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frame.setSize( 550, 450 );
            frame.validate();
            frame.setLocationRelativeTo( null );
            frame.setVisible( true );
        }
        catch( IOException ex )
        {
            // TODO Need to ask the user for a different ip/port and try
            // again
            ex.printStackTrace();
            throw new RuntimeException( "Could not connect", ex );
        }

        return frame;
    }

    @Override
    protected boolean validate()
    {
        return true;
    }
}
