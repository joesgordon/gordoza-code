package org.budgey;

import javax.swing.JFrame;

import org.budgey.ui.BudgeyFrame;
import org.jutils.core.ui.app.IFrameApp;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BudgeyApp implements IFrameApp
{
    /***************************************************************************
     * 
     **************************************************************************/
    public BudgeyApp()
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        BudgeyFrame frameView = new BudgeyFrame();
        JFrame frame = frameView.getView();

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
    }
}
