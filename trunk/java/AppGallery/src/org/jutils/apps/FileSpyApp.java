package org.jutils.apps;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.jutils.IconConstants;
import org.jutils.apps.filespy.FileSpyMain;
import org.jutils.ui.FrameRunner;

public class FileSpyApp implements ILibraryApp
{
    @Override
    public Icon getIcon()
    {
        return IconConstants.getIcon( IconConstants.PAGEMAG_32 );
    }

    @Override
    public String getName()
    {
        return "FileSpy";
    }

    @Override
    public JFrame runApp()
    {
        FrameRunner r = new FileSpyMain();
        r.run();
        return r.getFrame();
    }
}
