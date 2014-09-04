package org.cojo;

import javax.swing.JFrame;

import org.cojo.ui.CojoFrame;
import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;

public class CoJoMain implements IFrameApp
{
    public static void main( String [] args )
    {
        FrameApplication.invokeLater( new CoJoMain() );
    }

    @Override
    public JFrame createFrame()
    {
        CojoFrame frame = new CojoFrame();

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 800, 600 );
        return frame;
    }

    @Override
    public void finalizeGui()
    {
    }
}
