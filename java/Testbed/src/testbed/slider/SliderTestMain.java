package testbed.slider;

import javax.swing.JFrame;

import org.jutils.core.ui.app.FrameRunner;
import org.jutils.core.ui.app.IFrameApp;

public class SliderTestMain implements IFrameApp
{
    @Override
    public JFrame createFrame()
    {
        SliderTestFrame view = new SliderTestFrame();
        JFrame frame = view.getView();

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
        FrameRunner.invokeLater( new SliderTestMain() );
    }
}
