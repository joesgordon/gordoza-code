package org.jutils.appgallery.apps;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.jutils.IconConstants;
import org.jutils.appgallery.ILibraryApp;
import org.jutils.apps.jexplorer.JExplorerMain;
import org.jutils.ui.app.IFrameApp;

public class JExplorerApp implements ILibraryApp
{
    @Override
    public Icon getIcon32()
    {
        return IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_32 );
    }

    @Override
    public String getName()
    {
        return "JExplorer";
    }

    @Override
    public JFrame createApp()
    {
        IFrameApp r = new JExplorerMain();

        return r.createFrame();
    }
}
