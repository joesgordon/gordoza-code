package org.cojo;

import javax.swing.JFrame;

import org.cojo.ui.CojoFrame;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;

public class CoJoMain implements IFrameApp
{
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new CoJoMain() );
    }

    @Override
    public JFrame createFrame()
    {
        CojoFrame frameView = new CojoFrame();
        JFrame frame = frameView.getView();

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }

    @Override
    public void finalizeGui()
    {
    }
}
