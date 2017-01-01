package org.tuvok.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import org.jutils.IconConstants;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class TaskflowFrameView implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final JFrame frame;

    /***************************************************************************
     *
     **************************************************************************/
    public TaskflowFrameView()
    {
        this.frameView = new StandardFrameView();
        this.frame = frameView.getView();

        TaskflowPanel mainPanel = new TaskflowPanel();

        createMenuBar( frameView.getMenuBar() );

        frameView.setToolbar( createToolBar() );
        frameView.setContent( mainPanel.getView() );

        frame.setTitle( "Taskflow" );

        frame.setIconImages( IconConstants.getImages( IconConstants.CALENDAR_16,
            IconConstants.CALENDAR_32 ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JToolBar createToolBar()
    {
        JButton openButton = new JButton(
            IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 ) );

        JButton upButton = new JButton(
            IconConstants.getIcon( IconConstants.UP_16 ) );

        JButton leftButton = new JButton(
            IconConstants.getIcon( IconConstants.LEFT_16 ) );

        JButton rightButton = new JButton(
            IconConstants.getIcon( IconConstants.RIGHT_16 ) );

        JButton undoButton = new JButton(
            IconConstants.getIcon( IconConstants.UNDO_16 ) );

        JButton redoButton = new JButton(
            IconConstants.getIcon( IconConstants.REDO_16 ) );

        JButton addButton = new JButton(
            IconConstants.getIcon( IconConstants.EDIT_ADD_16 ) );

        JButton removeButton = new JButton(
            IconConstants.getIcon( IconConstants.EDIT_DELETE_16 ) );

        JToolBar mainToolBar = new JGoodiesToolBar();
        mainToolBar.add( openButton );
        mainToolBar.addSeparator();
        mainToolBar.add( upButton );
        mainToolBar.add( leftButton );
        mainToolBar.add( rightButton );
        mainToolBar.addSeparator();
        mainToolBar.add( undoButton );
        mainToolBar.add( redoButton );
        mainToolBar.addSeparator();
        mainToolBar.add( addButton );
        mainToolBar.add( removeButton );

        return mainToolBar;
    }

    /***************************************************************************
     * @param jMenuBar
     * @return
     **************************************************************************/
    private void createMenuBar( JMenuBar menuBar )
    {
        createFileMenu( frameView.getFileMenu() );

        menuBar.add( createViewMenu() );
        menuBar.add( createToolsMenu() );
        menuBar.add( createHelpMenu() );
    }

    /***************************************************************************
     * @param jMenu
     * @return
     **************************************************************************/
    private void createFileMenu( JMenu fileMenu )
    {
        JMenuItem newMenuItem = new JMenuItem( "New" );
        newMenuItem.setIcon(
            IconConstants.getIcon( IconConstants.NEW_FILE_16 ) );
        newMenuItem.addActionListener( new NewListener() );

        JMenuItem openMenuItem = new JMenuItem( "Open" );
        openMenuItem.setIcon(
            IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        openMenuItem.addActionListener( new OpenListener( this ) );

        JMenuItem saveMenuItem = new JMenuItem( "Save" );
        saveMenuItem.setIcon( IconConstants.getIcon( IconConstants.SAVE_16 ) );
        saveMenuItem.addActionListener( new SaveListener( this ) );

        fileMenu.add( newMenuItem, 0 );
        fileMenu.add( openMenuItem, 1 );
        fileMenu.add( saveMenuItem, 2 );
        fileMenu.add( new JSeparator(), 3 );
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
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SaveListener implements ActionListener
    {
        private final TaskflowFrameView view;

        public SaveListener( TaskflowFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            JFileChooser jfc = new JFileChooser();
            jfc.setFileFilter( new TdlFileFilter() );
            jfc.showSaveDialog( view.frame );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OpenListener implements ActionListener
    {
        private final TaskflowFrameView view;

        public OpenListener( TaskflowFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            JFileChooser jfc = new JFileChooser();
            jfc.setFileFilter( new TdlFileFilter() );
            jfc.showOpenDialog( view.frame );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class NewListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class TdlFileFilter extends FileFilter
    {
        @Override
        public boolean accept( File f )
        {
            if( f.isDirectory() || f.getName().endsWith( ".tdl" ) )
            {
                return true;
            }
            return false;
        }

        @Override
        public String getDescription()
        {
            return "*.tdl";
        }
    }
}
