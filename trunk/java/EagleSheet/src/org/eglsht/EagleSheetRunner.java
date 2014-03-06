package org.eglsht;

import javax.swing.JFrame;

import org.eglsht.ui.EagleSheetFrameView;
import org.jutils.ui.app.IFrameApp;

public class EagleSheetRunner implements IFrameApp
{
    public EagleSheetRunner()
    {
        ;
    }

    @Override
    public JFrame createFrame()
    {
        EagleSheetFrameView view = new EagleSheetFrameView();
        JFrame frame = view.getView();

        frame.setSize( 500, 500 );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }

    @Override
    public void finalizeGui()
    {
    }
}
