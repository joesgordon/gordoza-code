package org.jutils.appgallery.apps;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.jutils.IconConstants;
import org.jutils.appgallery.ILibraryApp;
import org.jutils.ui.app.IFrameApp;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JHexApp implements ILibraryApp
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Icon getIcon32()
    {
        return IconConstants.loader.getIcon( IconConstants.BINARY_32 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return "JHex";
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createApp()
    {
        IFrameApp frameApp = new org.jutils.apps.jhex.JHexApp();

        return frameApp.createFrame();
    }
}
