package org.cojo;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.cojo.ui.CojoFrame;
import org.jutils.ui.FrameRunner;

public class CoJoMain extends FrameRunner
{
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new CoJoMain() );
    }

    @Override
    protected JFrame createFrame()
    {
        CojoFrame frame = new CojoFrame();

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 800, 600 );
        return frame;
    }

    @Override
    protected boolean validate()
    {
        return true;
    }
}
