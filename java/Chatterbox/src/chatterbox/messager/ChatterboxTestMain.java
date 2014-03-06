package chatterbox.messager;

import javax.swing.JFrame;

import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;

import chatterbox.controller.ChatController;
import chatterbox.ui.ChatFrame;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterboxTestMain implements IFrameApp
{
    private TestChat messager;

    public ChatterboxTestMain()
    {
        messager = new TestChat();
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        FrameApplication.invokeLater( new ChatterboxTestMain() );
    }

    @Override
    public JFrame createFrame()
    {
        ChatFrame frame = new ChatFrame( messager );
        new ChatController( messager, frame );

        messager.connect( "238.192.69.69", 6969 );

        frame.setTitle( "Chatterbox - Test" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 550, 450 );
        frame.validate();
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );

        return frame;
    }

    @Override
    public void finalizeGui()
    {
    }
}
