package org.mc.ui;

import java.nio.charset.Charset;

import javax.swing.JFrame;

import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.mc.McMessage;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexMessagePanelMain
{
    /***************************************************************************
     * @param args ignored
     **************************************************************************/
    public static void main( String[] args )
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

            HexMessagePanel panel = new HexMessagePanel();

            frame.setContentPane( panel.getView() );

            frame.setSize( 600, 200 );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            byte[] contents = "So long and thanks f".getBytes(
                Charset.forName( "US-ASCII" ) );
            McMessage msg = new McMessage( "fdfdsfsd", 1234, contents );

            panel.setMessage( msg );

            return frame;
        }

        @Override
        public void finalizeGui()
        {
        }
    }
}
