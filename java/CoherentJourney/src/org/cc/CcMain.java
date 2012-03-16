package org.cc;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.cc.ui.CcFrame;
import org.jutils.ui.FrameRunner;

public class CcMain extends FrameRunner
{
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new CcMain() );
    }

    @Override
    protected JFrame createFrame()
    {
        CcFrame frame = new CcFrame();

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
