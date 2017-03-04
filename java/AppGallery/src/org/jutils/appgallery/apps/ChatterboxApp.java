package org.jutils.appgallery.apps;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.jutils.IconConstants;
import org.jutils.appgallery.ILibraryApp;
import org.jutils.ui.app.IFrameApp;

import chatterbox.ChatterboxConstants;
import chatterbox.data.ChatUser;

//TODO comments

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterboxApp implements ILibraryApp
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Icon getIcon32()
    {
        return IconConstants.getIcon( IconConstants.CHAT_32 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Chatterbox";
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createApp()
    {
        ChatUser user = ChatterboxConstants.createDefaultUser();
        IFrameApp frameApp = new chatterbox.ChatterboxApp( user );
        JFrame frame = frameApp.createFrame();

        frameApp.finalizeGui();

        return frame;
    }
}
