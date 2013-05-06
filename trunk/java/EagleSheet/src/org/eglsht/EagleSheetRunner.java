package org.eglsht;

import javax.swing.JFrame;

import org.eglsht.ui.EagleSheetFrameView;
import org.jutils.ui.FrameRunner;

public class EagleSheetRunner extends FrameRunner
{
    public EagleSheetRunner()
    {
        ;
    }

    @Override
    protected JFrame createFrame()
    {
        EagleSheetFrameView view = new EagleSheetFrameView();
        JFrame frame = view.getView();

        frame.setSize( 500, 500 );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }

    @Override
    protected boolean validate()
    {
        return true;
    }
}
