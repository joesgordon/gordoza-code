package utesting;

import javax.swing.JFrame;

import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.hex.ByteBuffer;
import org.jutils.ui.hex.HexPanel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexPanelRunner implements IFrameApp
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        JFrame frame = new JFrame( "HexRunner" );
        HexPanel p = new HexPanel();
        // int count = 1024 * 1024;
        int count = 55;
        byte[] bytes = new byte[count];

        for( int i = 0; i < bytes.length; i++ )
        {
            Double d = Math.random();
            Integer hash = d.hashCode();
            bytes[i] = hash.byteValue();
        }

        p.setBuffer( new ByteBuffer( bytes ) );

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( p.getView() );
        frame.setSize( 700, 400 );

        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        FrameApplication.invokeLater( new HexPanelRunner() );
    }
}
