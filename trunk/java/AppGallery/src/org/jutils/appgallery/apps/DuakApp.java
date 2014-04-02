package org.jutils.appgallery.apps;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.jutils.IconConstants;
import org.jutils.appgallery.ILibraryApp;
import org.jutils.ui.app.IFrameApp;

public class DuakApp implements ILibraryApp
{
    @Override
    public Icon getIcon32()
    {
        return IconConstants.loader.getIcon( IconConstants.ATOMIC_32 );
    }

    @Override
    public String getName()
    {
        return "Duak";
    }

    @Override
    public JFrame createApp()
    {
        IFrameApp r = new org.duak.DuakApp();

        return r.createFrame();
    }
}
