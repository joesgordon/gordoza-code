package org.jutils.apps;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.budgey.BudgeyIconConstants;
import org.budgey.BudgeyMain;
import org.budgey.data.BudgeyOptions;
import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;

//TODO comments

/*******************************************************************************
 * 
 ******************************************************************************/
public class BudgeyApp implements ILibraryApp
{
    @Override
    public Icon getIcon()
    {
        return BudgeyIconConstants.getWallet32Icon();
    }

    @Override
    public String getName()
    {
        return "Budgey";
    }

    @Override
    public JFrame runApp()
    {
        BudgeyOptions options = BudgeyOptions.read( BudgeyMain.OPTIONS_FILE );
        IFrameApp frameApp = new BudgeyMain( options );
        FrameApplication app = new FrameApplication( frameApp, true );

        return app.createAndShowFrame();
    }
}
