package org.jutils.appgallery.apps;

import java.awt.Component;

import javax.swing.Icon;

import org.jutils.IconConstants;
import org.jutils.appgallery.ILibraryApp;
import org.tuvok.TuvokApp;

public class TuvokLibApp implements ILibraryApp
{
    @Override
    public Icon getIcon32()
    {
        return IconConstants.loader.getIcon( IconConstants.CALENDAR_32 );
    }

    @Override
    public String getName()
    {
        return "Tuvok";
    }

    @Override
    public Component createApp()
    {
        TuvokApp app = new TuvokApp();

        return app.createFrame();
    }

}
