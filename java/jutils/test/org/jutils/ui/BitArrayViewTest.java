package org.jutils.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.hex.BitArrayView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BitArrayViewTest
{
    /***************************************************************************
     * @param args Ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new BitArrayApp(), true );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class BitArrayApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            JFrame frame = new JFrame();
            List<byte []> list = new ArrayList<>();

            list.add( new byte[] { 0x65, 0x43 } );
            list.add( new byte[] { ( byte )0xA3, 0x1F } );
            list.add( new byte[] { ( byte )0xDE, ( byte )0xAD } );
            list.add( new byte[] { ( byte )0xBE, ( byte )0xEF } );

            BitArrayView view = new BitArrayView( list );

            frame.setContentPane( view.getView() );
            frame.setSize( 500, 500 );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            return frame;
        }

        @Override
        public void finalizeGui()
        {
        }
    }
}
