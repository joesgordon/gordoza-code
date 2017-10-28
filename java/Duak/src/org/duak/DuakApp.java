package org.duak;

import javax.swing.JFrame;

import org.duak.ui.DuakFrame;
import org.jutils.ui.app.IFrameApp;

public class DuakApp implements IFrameApp
{
    @Override
    public JFrame createFrame()
    {
        DuakFrame df = new DuakFrame();
        JFrame frame = df.getView();

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }

    @Override
    public void finalizeGui()
    {
    }
}
