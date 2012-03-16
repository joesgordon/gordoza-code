package testbed.layers;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jutils.ui.FrameRunner;

public class LayeredTestMain extends FrameRunner
{
    @Override
    protected JFrame createFrame()
    {
        JFrame frame = new LayeredTestFrame();

        frame.setSize( 500, 500 );
        frame.setTitle( "Layered Tester" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }

    @Override
    protected boolean validate()
    {
        return true;
    }

    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new LayeredTestMain() );
    }
}
