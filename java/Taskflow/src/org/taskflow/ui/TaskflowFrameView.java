package org.taskflow.ui;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ValidationException;
import org.jutils.io.XStreamUtils;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.RecentFilesViews;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.event.FileChooserListener;
import org.jutils.ui.event.FileChooserListener.IFileSelected;
import org.jutils.ui.model.IView;
import org.taskflow.TaskflowIcons;
import org.taskflow.data.Project;

/*******************************************************************************
 *
 ******************************************************************************/
public class TaskflowFrameView implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final TaskflowView mainPanel;
    /**  */
    private final RecentFilesViews recentFiles;

    /***************************************************************************
     *
     **************************************************************************/
    public TaskflowFrameView()
    {
        this.frameView = new StandardFrameView();
        this.recentFiles = new RecentFilesViews();
        this.mainPanel = new TaskflowView();

        createMenuBar( frameView.getMenuBar() );

        frameView.setToolbar( createToolBar() );
        frameView.setContent( mainPanel.getView() );

        frameView.setTitle( "Taskflow" );

        frameView.getView().setIconImages( TaskflowIcons.getAppImages() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolBar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        recentFiles.install( toolbar, createOpenAction() );
        recentFiles.setListeners( ( f, c ) -> openFile( f ) );

        SwingUtils.addActionToToolbar( toolbar, createSaveAction() );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, createAddAction() );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createNewAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.NEW_FILE_16 );
        ActionListener listener = ( e ) -> setNew();

        return new ActionAdapter( listener, "New", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Action createOpenAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );
        IFileSelected ifsl = ( f ) -> openFile( f );
        FileChooserListener fcl = new FileChooserListener( getView(),
            "Open Tasks", false, ifsl );
        // TODO add last file selected
        return new ActionAdapter( fcl, "Open", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createSaveAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.SAVE_16 );
        IFileSelected ifsl = ( f ) -> saveFile( f );
        FileChooserListener fcl = new FileChooserListener( getView(),
            "Save Tasks", true, ifsl );
        // TODO add last file selected
        return new ActionAdapter( fcl, "Save", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createAddAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.EDIT_ADD_16 );
        ActionListener listener = ( e ) -> showAddDialog();
        // TODO add last file selected
        return new ActionAdapter( listener, "Add Task", icon );
    }

    /***************************************************************************
     * @param menuBar
     **************************************************************************/
    private void createMenuBar( JMenuBar menuBar )
    {
        createFileMenu( frameView.getFileMenu() );

        menuBar.add( createViewMenu() );
        menuBar.add( createToolsMenu() );
        menuBar.add( createHelpMenu() );
    }

    /***************************************************************************
     * @param fileMenu
     **************************************************************************/
    private void createFileMenu( JMenu fileMenu )
    {
        JMenuItem newMenuItem = new JMenuItem( createNewAction() );
        JMenuItem openMenuItem = new JMenuItem( createOpenAction() );
        JMenuItem saveMenuItem = new JMenuItem( createSaveAction() );

        fileMenu.add( newMenuItem, 0 );
        fileMenu.add( openMenuItem, 1 );
        fileMenu.add( saveMenuItem, 2 );
        fileMenu.add( new JSeparator(), 3 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showAddDialog()
    {
        AddTaskView newTaskView = new AddTaskView();
        OkDialogView dialogView = new OkDialogView( getView(),
            newTaskView.getView(), ModalityType.DOCUMENT_MODAL,
            OkDialogButtons.OK_CANCEL );

        dialogView.setTitle( "Add New Task" );
        dialogView.setOkButtonText( "Add" );

        if( dialogView.show() )
        {
            // String name = newTaskView.getData();
            // TODO create and add task
        }
    }

    /***************************************************************************
     * @param f
     **************************************************************************/
    public void openFile( File f )
    {
        // TODO Auto-generated method stub
        try
        {
            Project proj = XStreamUtils.readObjectXStream( f );
            mainPanel.setData( proj );
        }
        catch( ValidationException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
    }

    /***************************************************************************
     * @param f
     **************************************************************************/
    private void saveFile( File f )
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setNew()
    {
        mainPanel.setData( new Project() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JMenu createViewMenu()
    {
        JMenu viewMenu = new JMenu( "View" );
        JMenuItem foldersMenuItem = new JMenuItem( "Folders" );

        viewMenu.add( foldersMenuItem );

        return viewMenu;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JMenu createToolsMenu()
    {
        JMenu toolsMenu = new JMenu( "Tools" );
        JMenuItem optionsMenuItem = new JMenuItem( "Options" );

        toolsMenu.add( optionsMenuItem );

        return toolsMenu;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JMenu createHelpMenu()
    {
        JMenu helpMenu = new JMenu( "Help" );
        JMenuItem aboutMenuItem = new JMenuItem( "About" );

        helpMenu.add( aboutMenuItem );

        return helpMenu;
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
