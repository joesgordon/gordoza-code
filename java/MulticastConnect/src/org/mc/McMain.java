package org.mc;

import javax.swing.JFrame;

import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.mc.ui.McFrame;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McMain implements IFrameApp
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        FrameRunner.invokeLater( new McMain() );
    }

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
