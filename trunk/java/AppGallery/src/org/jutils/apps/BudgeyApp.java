package org.jutils.apps;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.budgey.BudgeyIconConstants;
import org.budgey.BudgeyMain;
import org.budgey.data.BudgeyOptions;
import org.jutils.ui.FrameRunner;

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
        FrameRunner r = new BudgeyMain( options );
        r.run();
        return r.getFrame();
    }
}
