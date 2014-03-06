package org.duak;

import javax.swing.JFrame;

import org.duak.ui.DuakFrame;
import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;

public class DuakMain implements IFrameApp
{
    @Override
    public JFrame createFrame()
    {
        JFrame frame = new DuakFrame();

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 600, 500 );

        return frame;
    }

    @Override
    public void finalizeGui()
    {
    }

    public static void main( String[] args )
    {
        FrameApplication.invokeLater( new DuakMain() );
    }
}
