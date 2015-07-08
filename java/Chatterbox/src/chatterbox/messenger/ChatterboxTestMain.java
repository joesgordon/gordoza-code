package chatterbox.messenger;

import javax.swing.JFrame;

import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;

import chatterbox.controller.ChatController;
import chatterbox.ui.ChatFrameView;
import chatterbox.view.IChatView;

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
        FrameApplication.invokeLater( new ChatterboxTestMain() );
    }

    @Override
    public JFrame createFrame()
    {
        ChatFrameView frameView = new ChatFrameView();
        IChatView chatView = frameView.getChatView();

        chatView.setChat( chat );

        new ChatController( chat, chatView );

        chat.connect( "238.192.69.69", 6969 );

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
