package org.jutils.apps;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.jutils.IconConstants;
import org.jutils.apps.jhex.JHexMain;
import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;

public class JHexApp implements ILibraryApp
{
    @Override
    public Icon getIcon()
    {
        return IconConstants.loader.getIcon( IconConstants.BINARY_32 );
    }

    @Override
    public String getName()
    {
        return "JHex";
    }

    @Override
    public JFrame runApp()
    {
        IFrameApp frameApp = new org.jutils.apps.jhex.JHexApp(
            JHexMain.getUserIO() );
        FrameApplication app = new FrameApplication( frameApp, true );

        return app.createAndShowFrame();
    }
}
