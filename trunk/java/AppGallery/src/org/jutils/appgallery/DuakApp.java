package org.jutils.appgallery;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.duak.DuakMain;
import org.jutils.IconConstants;
import org.jutils.ui.app.IFrameApp;

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
        IFrameApp r = new DuakMain();

        return r.createFrame();
    }
}
