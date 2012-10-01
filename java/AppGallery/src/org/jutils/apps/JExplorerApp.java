package org.jutils.apps;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.jutils.IconConstants;
import org.jutils.apps.jexplorer.JExplorerMain;
import org.jutils.ui.FrameRunner;

public class JExplorerApp implements ILibraryApp
{
    @Override
    public Icon getIcon()
    {
        return IconConstants.getIcon( IconConstants.OPEN_FOLDER_32 );
    }

    @Override
    public String getName()
    {
        return "JExplorer";
    }

    @Override
    public JFrame runApp()
    {
        FrameRunner r = new JExplorerMain();
        r.run();
        return r.getFrame();
    }
}
