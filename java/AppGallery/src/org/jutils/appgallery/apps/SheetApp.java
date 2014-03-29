package org.jutils.appgallery.apps;

import java.awt.Component;

import javax.swing.Icon;

import org.eglsht.EagleSheetApp;
import org.eglsht.EagleSheetIcons;
import org.jutils.appgallery.ILibraryApp;
import org.jutils.ui.app.IFrameApp;

public class SheetApp implements ILibraryApp
{
    @Override
    public Icon getIcon()
    {
        return EagleSheetIcons.getApp32();
    }

    @Override
    public String getName()
    {
        return "EagleSheet";
    }

    @Override
    public Component createApp()
    {
        IFrameApp app = new EagleSheetApp();

        return app.createFrame();
    }
}
