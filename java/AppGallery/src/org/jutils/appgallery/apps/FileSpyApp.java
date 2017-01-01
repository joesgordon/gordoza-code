package org.jutils.appgallery.apps;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.jutils.IconConstants;
import org.jutils.appgallery.ILibraryApp;
import org.jutils.ui.app.IFrameApp;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FileSpyApp implements ILibraryApp
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Icon getIcon32()
    {
        return IconConstants.getIcon( IconConstants.PAGEMAG_32 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return "FileSpy";
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createApp()
    {
        IFrameApp frameApp = new org.jutils.apps.filespy.FileSpyApp();

        return frameApp.createFrame();
    }
}
