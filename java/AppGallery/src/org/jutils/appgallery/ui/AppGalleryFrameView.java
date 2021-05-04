package org.jutils.appgallery.ui;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuItem;

import org.jutils.appgallery.AppGalleryIcons;
import org.jutils.core.ui.IToolView;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.ToolsView;
import org.jutils.core.ui.model.IView;

/*******************************************************************************
 * Defines the window for this application that displays tools in a gallery.
 ******************************************************************************/
public class AppGalleryFrameView implements IView<JFrame>
{
    /** The window for this application. */
    private final StandardFrameView frameView;
    /** The view that displays the tools. */
    private final ToolsView view;

    /***************************************************************************
     * Creates a new app gallery window that displays the provided tools.
     * @param tools the tools to be displayed in the gallery.
     **************************************************************************/
    public AppGalleryFrameView( List<IToolView> tools )
    {
        this.frameView = new StandardFrameView();
        this.view = new ToolsView( tools, "JUtils App Gallery" );

        frameView.setContent( view.getView() );
        frameView.setTitle( "JUtils Application Gallery" );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 500, 500 );

        frameView.getView().setIconImages( AppGalleryIcons.getAppImages() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }

    /***************************************************************************
     * Creates a menu for the tools displayed.
     * @return a menu containing the tools displayed.
     * @see ToolsView#createMenu()
     **************************************************************************/
    public JMenuItem createMenu()
    {
        return view.createMenu();
    }
}
