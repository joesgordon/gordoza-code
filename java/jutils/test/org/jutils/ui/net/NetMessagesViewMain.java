package org.jutils.ui.net;

import java.util.Random;

import javax.swing.JFrame;

import org.jutils.net.NetMessage;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetMessagesViewMain
{
    /***************************************************************************
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new HexMessageApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class HexMessageApp implements IFrameApp
    {

        private final Random rand = new Random( 42 );

        @Override
        public JFrame createFrame()
        {
            JFrame frame = new JFrame();

            // HexMessagePanel panel = new HexMessagePanel();
            NetMessagesView panel = new NetMessagesView();

            frame.setContentPane( panel.getView() );

            frame.setSize( 680, 400 );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            panel.addMessage( buildMessage() );
            panel.addMessage( buildMessage() );
            panel.addMessage( buildMessage() );
            panel.addMessage( buildMessage() );
            panel.addMessage( buildMessage() );
            panel.addMessage( buildMessage() );

            return frame;
        }

        private NetMessage buildMessage()
        {
            int len = rand.nextInt( 1024 );
            byte [] bytes = new byte[len];
            rand.nextBytes( bytes );

            NetMessage msg = new NetMessage( true, "127.0.0.1", 80, bytes );
            return msg;
        }

        @Override
        public void finalizeGui()
        {
        }
    }
}
