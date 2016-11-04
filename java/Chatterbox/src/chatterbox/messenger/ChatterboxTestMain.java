package chatterbox.messenger;

import javax.swing.JFrame;

import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;

import chatterbox.data.ChatConfig;
import chatterbox.ui.ChatFrameView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterboxTestMain implements IFrameApp
{
    private TestChat chat;

    public ChatterboxTestMain()
    {
        chat = new TestChat();
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        FrameRunner.invokeLater( new ChatterboxTestMain() );
    }

    @Override
    public JFrame createFrame()
    {
        ChatFrameView frameView = new ChatFrameView( chat );
        ChatConfig config = new ChatConfig();

        config.address = "238.192.69.69";
        config.port = 6969;
        config.displayName = "test";

        chat.connect( config );

        JFrame frame = frameView.getView();

        frame.setTitle( "Chatterbox - Test" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 550, 450 );

        return frame;
    }

    @Override
    public void finalizeGui()
    {
    }
}
