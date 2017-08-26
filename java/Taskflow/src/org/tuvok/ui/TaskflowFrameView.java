package org.tuvok.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.*;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.event.FileChooserListener;
import org.jutils.ui.event.FileChooserListener.IFileSelected;
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
    /**  */
    private final SplitButtonView<File> recentButton;

    /***************************************************************************
     *
     **************************************************************************/
    public TaskflowFrameView()
    {
        this.frameView = new StandardFrameView();
        this.frame = frameView.getView();
        this.recentButton = new SplitButtonView<>( null,
            IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 ),
            new ArrayList<>(), ( f ) -> f.getAbsolutePath() );

        TaskflowView mainPanel = new TaskflowView();

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
    private JToolBar createToolBar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        recentButton.install( toolbar );
        IFileSelected ifsl = ( f ) -> openFile( f );
        recentButton.addButtonListener(
            new FileChooserListener( frame, "Open Tasks", false, ifsl ) );
        // TODO add last file selected
        recentButton.addItemSelected( ( f ) -> openFile( f ) );

        SwingUtils.addActionToToolbar( toolbar, createSaveAction() );

        return toolbar;
    }

    private Action createSaveAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.SAVE_16 );
        IFileSelected ifsl = ( f ) -> saveFile( f );
        ActionListener listener = new FileChooserListener( frame, "Save Tasks",
            true, ifsl );
        // TODO add last file selected
        return new ActionAdapter( listener, "Save", icon );
    }

    private void openFile( File f )
    {
        // TODO Auto-generated method stub
    }

    private void saveFile( File f )
    {
        // TODO Auto-generated method stub
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
