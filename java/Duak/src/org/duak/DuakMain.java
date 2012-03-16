package org.duak;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.duak.ui.DuakFrame;
import org.jutils.ui.FrameRunner;

public class DuakMain extends FrameRunner
{
    @Override
    protected JFrame createFrame()
    {
        JFrame frame = new DuakFrame();

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 600, 500 );

        return frame;
    }

    @Override
    protected boolean validate()
    {
        return true;
    }

    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new DuakMain() );
    }
}
