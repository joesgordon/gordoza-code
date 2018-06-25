package chatterbox;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.jutils.IconConstants;
import org.jutils.io.IconLoader;
import org.jutils.ui.IToolView;
import org.jutils.ui.app.IFrameApp;

import chatterbox.data.ChatUser;

//TODO comments

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterboxTool implements IToolView
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Icon getIcon24()
    {
        return IconConstants.getIcon( IconConstants.CHAT_24 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Chatterbox";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        ChatUser user = ChatterboxConstants.createDefaultUser();
        IFrameApp frameApp = new chatterbox.ChatterboxApp( user );
        JFrame frame = frameApp.createFrame();

        frameApp.finalizeGui();

        return frame;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getDescription()
    {
        return "Chat client for closed or air gapped networks";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<Image> getImages()
    {
        return IconConstants.getImages( IconLoader.buildNameList( "CHAT_" ) );
    }
}
