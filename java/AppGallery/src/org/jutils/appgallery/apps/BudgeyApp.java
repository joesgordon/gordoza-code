package org.jutils.appgallery.apps;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.budgey.BudgeyIconConstants;
import org.jutils.appgallery.ILibraryApp;
import org.jutils.ui.app.IFrameApp;

//TODO comments

/*******************************************************************************
 * 
 ******************************************************************************/
public class BudgeyApp implements ILibraryApp
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Icon getIcon32()
    {
        return BudgeyIconConstants.getWallet32Icon();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Budgey";
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createApp()
    {
        IFrameApp frameApp = new org.budgey.BudgeyApp();

        return frameApp.createFrame();
    }
}
