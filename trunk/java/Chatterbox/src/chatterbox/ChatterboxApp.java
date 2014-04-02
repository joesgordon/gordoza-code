package chatterbox;

import java.io.IOException;

import javax.swing.JFrame;

import org.jutils.ui.app.IFrameApp;

import chatterbox.controller.ChatController;
import chatterbox.messager.Chat;
import chatterbox.ui.ChatFrame;

public class ChatterboxApp implements IFrameApp
{
    private Chat messager;
    private String address;
    private int port;

    public ChatterboxApp()
    {
        messager = new Chat();
        address = "238.192.69.69";
        port = 6969;
    }

    @Override
    public JFrame createFrame()
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
    public void finalizeGui()
    {
    }
}
