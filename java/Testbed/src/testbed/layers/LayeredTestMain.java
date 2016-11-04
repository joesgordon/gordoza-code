package testbed.layers;

import javax.swing.JFrame;

import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;

public class LayeredTestMain implements IFrameApp
{
    @Override
    public JFrame createFrame()
    {
        JFrame frame = new LayeredTestFrame();

        frame.setSize( 500, 500 );
        frame.setTitle( "Layered Tester" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }

    @Override
    public void finalizeGui()
    {
    }

    public static void main( String[] args )
    {
        FrameRunner.invokeLater( new LayeredTestMain() );
    }
}
