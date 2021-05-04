package org.cojo.ui;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import org.cojo.data.ProjectManager;
import org.jutils.core.IconConstants;
import org.jutils.core.SwingUtils;
import org.jutils.core.ValidationException;
import org.jutils.core.io.xs.XsUtils;
import org.jutils.core.ui.JGoodiesToolBar;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.event.FileChooserListener;
import org.jutils.core.ui.event.FileChooserListener.IFileSelected;
import org.jutils.core.ui.event.FileChooserListener.ILastFile;
import org.jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CojoFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final ProjectPanel mainPanel;

    /***************************************************************************
     * 
     **************************************************************************/
    public CojoFrame()
    {
        this.frameView = new StandardFrameView();
        this.mainPanel = new ProjectPanel();

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
        ProjectManager manager = new ProjectManager();
        mainPanel.setData( manager );
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void openProject( File file )
    {
        try
        {
            ProjectManager manager = new ProjectManager();
            manager.project = XsUtils.readObjectXStream( file );
            mainPanel.setData( manager );
        }
        catch( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch( ValidationException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO Open project
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void saveProject( File file )
    {
        ProjectManager manager = mainPanel.getData();

        try
        {
            XsUtils.writeObjectXStream( manager.project, file );
        }
        catch( IOException | ValidationException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
