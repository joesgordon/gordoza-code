package org.jutils.apps;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.duak.DuakMain;
import org.jutils.IconConstants;
import org.jutils.ui.FrameRunner;

public class DuakApp implements ILibraryApp
{
    @Override
    public Icon getIcon()
    {
        return IconConstants.loader.getIcon( IconConstants.ATOMIC_32 );
    }

    @Override
    public String getName()
    {
        return "Duak";
    }

    @Override
    public JFrame runApp()
    {
        FrameRunner r = new DuakMain();
        r.run();
        return r.getFrame();
    }
}
