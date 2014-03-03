package org.jutils.appgallery;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.jutils.IconConstants;
import org.jutils.apps.filespy.FileSpyMain;
import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;

public class FileSpyApp implements ILibraryApp
{
    @Override
    public Icon getIcon()
    {
        return IconConstants.loader.getIcon( IconConstants.PAGEMAG_32 );
    }

    @Override
    public String getName()
    {
        return "FileSpy";
    }

    @Override
    public JFrame runApp()
    {
        IFrameApp frameApp = new org.jutils.apps.filespy.FileSpyApp(
            FileSpyMain.createUserIO() );
        FrameApplication app = new FrameApplication( frameApp, true );

        return app.createAndShowFrame();
    }
}
