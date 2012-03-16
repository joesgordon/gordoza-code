package utesting;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jutils.ui.FrameRunner;
import org.jutils.ui.hex.ByteBuffer;
import org.jutils.ui.hex.HexPanel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexPanelRunner extends FrameRunner
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected JFrame createFrame()
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
    protected String getLookAndFeelName()
    {
        // return "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected boolean validate()
    {
        return true;
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new HexPanelRunner() );
    }
}
