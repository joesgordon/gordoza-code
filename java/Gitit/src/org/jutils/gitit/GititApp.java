package org.jutils.gitit;

import javax.swing.JFrame;

import org.jutils.core.ui.app.IFrameApp;
import org.jutils.gitit.ui.GititFrameView;

/*******************************************************************************
 *
 ******************************************************************************/
public class GititApp implements IFrameApp
{
    private GititFrameView frameView;

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        frameView = new GititFrameView();
        JFrame frame = frameView.getView();

        return frame;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
        if( GititMain.getUserOptions().getOptions().config.directories.isEmpty() )
        {
            frameView.showConfigDialog();
        }
    }
}
