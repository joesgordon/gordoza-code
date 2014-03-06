package testbed.slider;

import javax.swing.JFrame;

import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;

public class SliderTestMain implements IFrameApp
{
    @Override
    public JFrame createFrame()
    {
        SliderTestFrame frame = new SliderTestFrame();

        frame.setSize( 500, 500 );
        frame.setTitle( "Slider Tester" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }

    @Override
    public void finalizeGui()
    {
    }

    public static void main( String[] args )
    {
        FrameApplication.invokeLater( new SliderTestMain() );
    }
}
