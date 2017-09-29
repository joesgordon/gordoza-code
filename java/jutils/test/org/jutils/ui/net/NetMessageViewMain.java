package org.jutils.ui.net;

import java.net.*;
import java.nio.charset.Charset;

import javax.swing.JFrame;

import org.jutils.net.NetMessage;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetMessageViewMain
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
        @Override
        public JFrame createFrame()
        {
            JFrame frame = new JFrame();

            NetMessageView panel = new NetMessageView();

            frame.setContentPane( panel.getView() );

            frame.setSize( 680, 400 );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            byte [] contents = "So long and thanks f".getBytes(
                Charset.forName( "UTF-8" ) );
            InetAddress address;
            try
            {
                address = Inet4Address.getLocalHost();
            }
            catch( UnknownHostException ex )
            {
                throw new RuntimeException( "Localhost should never be unknown",
                    ex );
            }
            int port = 80;
            NetMessage msg = new NetMessage( true, address.getHostAddress(),
                port, contents );

            panel.setData( msg );

            return frame;
        }

        @Override
        public void finalizeGui()
        {
        }
    }
}
