package org.mc;

import javax.swing.JFrame;

import org.jutils.ui.app.IFrameApp;
import org.mc.ui.McFrame;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McApp implements IFrameApp
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        McFrame frame = new McFrame();

        return frame.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
    }
}
