package org.tuvok;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import org.jutils.IconConstants;
import org.jutils.ui.*;

/*******************************************************************************
 *
 ******************************************************************************/
public class ToDLsFrame extends UFrame
{
    /***************************************************************************
     *
     **************************************************************************/
    public ToDLsFrame()
    {
        JPanel contentPanel = new JPanel( new BorderLayout() );
        ToDLsPanel mainPanel = new ToDLsPanel();

        contentPanel.add( createToolBar(), java.awt.BorderLayout.NORTH );
        contentPanel.add( mainPanel, java.awt.BorderLayout.CENTER );
        contentPanel.add( new StatusBarPanel().getView(),
            java.awt.BorderLayout.SOUTH );

        // ---------------------------------------------------------------------
        // Add content
        // ---------------------------------------------------------------------
        setTitle( "Tuvok" );

        setJMenuBar( createMenuBar() );
        setContentPane( contentPanel );

        setIconImages( IconConstants.getImages( IconConstants.CALENDAR_16,
            IconConstants.CALENDAR_32 ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolBar()
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

        JToolBar mainToolBar = new UToolBar();
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
     * @return
     **************************************************************************/
    private JMenuBar createMenuBar()
    {
        JMenuBar mainMenuBar = new UMenuBar();

        mainMenuBar.add( createFileMenu() );
        mainMenuBar.add( createViewMenu() );
        mainMenuBar.add( createToolsMenu() );
        mainMenuBar.add( createHelpMenu() );

        return mainMenuBar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createFileMenu()
    {
        JMenu fileMenu = new JMenu( "File" );

        JMenuItem newMenuItem = new JMenuItem( "New" );
        newMenuItem.setIcon( IconConstants.getIcon( IconConstants.NEW_FILE_16 ) );
        newMenuItem.addActionListener( new NewListener() );

        JMenuItem openMenuItem = new JMenuItem( "Open" );
        openMenuItem.setIcon( IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        openMenuItem.addActionListener( new OpenListener() );

        JMenuItem saveMenuItem = new JMenuItem( "Save" );
        saveMenuItem.setIcon( IconConstants.getIcon( IconConstants.SAVE_16 ) );
        saveMenuItem.addActionListener( new SaveListener() );

        JMenuItem exitMenuItem = new JMenuItem( "Exit" );
        exitMenuItem.setIcon( IconConstants.getIcon( IconConstants.CLOSE_16 ) );
        exitMenuItem.addActionListener( new ExitListener() );

        fileMenu.add( newMenuItem );
        fileMenu.add( openMenuItem );
        fileMenu.add( saveMenuItem );
        fileMenu.add( exitMenuItem );

        return fileMenu;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createViewMenu()
    {
        JMenu viewMenu = new JMenu( "View" );
        JMenuItem foldersMenuItem = new JMenuItem( "Folders" );

        viewMenu.add( foldersMenuItem );

        return viewMenu;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createToolsMenu()
    {
        JMenu toolsMenu = new JMenu( "Tools" );
        JMenuItem optionsMenuItem = new JMenuItem( "Options" );

        toolsMenu.add( optionsMenuItem );

        return toolsMenu;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createHelpMenu()
    {
        JMenu helpMenu = new JMenu( "Help" );
        JMenuItem aboutMenuItem = new JMenuItem( "About" );

        helpMenu.add( aboutMenuItem );

        return helpMenu;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class SaveListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            JFileChooser jfc = new JFileChooser();
            jfc.setFileFilter( new TdlFileFilter() );
            jfc.showSaveDialog( ToDLsFrame.this );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class OpenListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            JFileChooser jfc = new JFileChooser();
            jfc.setFileFilter( new TdlFileFilter() );
            jfc.showOpenDialog( ToDLsFrame.this );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class NewListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class TdlFileFilter extends FileFilter
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
