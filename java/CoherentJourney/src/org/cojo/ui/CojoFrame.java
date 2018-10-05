package org.cojo.ui;

import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import org.cojo.data.Project;
import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.event.FileChooserListener;
import org.jutils.ui.event.FileChooserListener.IFileSelected;
import org.jutils.ui.event.FileChooserListener.ILastFile;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CojoFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final CojoPanel mainPanel;

    /***************************************************************************
     * 
     **************************************************************************/
    public CojoFrame()
    {
        this.frameView = new StandardFrameView();
        this.mainPanel = new CojoPanel();

        frameView.setToolbar( createToolBar() );
        frameView.setContent( mainPanel.getView() );

        frameView.setTitle( "CoherentJourney v1.0" );
        frameView.setSize( 800, 600 );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolBar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        SwingUtils.addActionToToolbar( toolbar, createNewAction() );
        SwingUtils.addActionToToolbar( toolbar, createOpenAction() );
        SwingUtils.addActionToToolbar( toolbar, createSaveAction() );

        return toolbar;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private Action createNewAction()
    {
        ActionListener listener = ( e ) -> createNewProject();
        Icon icon = IconConstants.getIcon( IconConstants.NEW_FILE_16 );
        return new ActionAdapter( listener, "New Project", icon );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private Action createOpenAction()
    {
        IFileSelected ifs = ( f ) -> openProject( f );
        ILastFile ilf = null; // TODO read from options
        FileChooserListener listener = new FileChooserListener( getView(),
            "Open Project", false, ifs, ilf );
        Icon icon = IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );
        return new ActionAdapter( listener, "Open Project", icon );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private Action createSaveAction()
    {
        IFileSelected ifs = ( f ) -> saveProject( f );
        ILastFile ilf = null; // TODO read from options
        FileChooserListener listener = new FileChooserListener( getView(),
            "Save Project", true, ifs, ilf );
        Icon icon = IconConstants.getIcon( IconConstants.SAVE_16 );
        return new ActionAdapter( listener, "Save Project", icon );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void createNewProject()
    {
        mainPanel.setData( new Project() );
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void openProject( File file )
    {
        // TODO Open project
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void saveProject( File file )
    {
        // TODO Save project
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }
}
