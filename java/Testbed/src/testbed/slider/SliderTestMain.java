package testbed.slider;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jutils.ui.FrameRunner;

public class SliderTestMain extends FrameRunner
{
    @Override
    protected JFrame createFrame()
    {
        SliderTestFrame frame = new SliderTestFrame();

        frame.setSize( 500, 500 );
        frame.setTitle( "Slider Tester" );
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
        SwingUtilities.invokeLater( new SliderTestMain() );
    }
}
