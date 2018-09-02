package org.budgey;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.jutils.ui.IToolView;
import org.jutils.ui.app.IFrameApp;

//TODO comments

/*******************************************************************************
 * 
 ******************************************************************************/
public class BudgeyTool implements IToolView
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Icon getIcon24()
    {
        return BudgeyIcons.getIcon( BudgeyIcons.WALLET_24 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Budgey";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        IFrameApp frameApp = new org.budgey.BudgeyApp();

        return frameApp.createFrame();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getDescription()
    {
        return "Personal budgeting application";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<Image> getImages()
    {
        return BudgeyIcons.getAppImages();
    }
}
