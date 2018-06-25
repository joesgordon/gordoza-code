package org.jutils.appgallery.ui;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuItem;

import org.jutils.appgallery.AppGalleryIcons;
import org.jutils.ui.*;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AppGalleryFrameView implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final ToolsView view;

    /***************************************************************************
     * @param apps
     **************************************************************************/
    public AppGalleryFrameView( List<IToolView> apps )
    {
        this.frameView = new StandardFrameView();
        this.view = new ToolsView( apps, "JUtils App Gallery" );

        frameView.setContent( view.getView() );
        frameView.setTitle( "JUtils Application Gallery" );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 500, 500 );

        frameView.getView().setIconImages( AppGalleryIcons.getAppImages() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JMenuItem createMenu()
    {
        return view.createMenu();
    }
}
