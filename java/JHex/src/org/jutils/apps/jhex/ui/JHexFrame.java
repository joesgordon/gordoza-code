package org.jutils.apps.jhex.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.apps.jhex.JHexIcons;
import org.jutils.apps.jhex.JHexMain;
import org.jutils.apps.jhex.data.JHexOptions;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.*;
import org.jutils.ui.event.*;
import org.jutils.ui.event.FileChooserListener.IFileSelected;
import org.jutils.ui.event.FileChooserListener.ILastFile;
import org.jutils.ui.event.FileDropTarget.IFileDropEvent;
import org.jutils.ui.hex.HexBufferSize;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * Represents the view that builds and contains the main frame for the
 * application.
 ******************************************************************************/
public class JHexFrame implements IView<JFrame>
{
    // -------------------------------------------------------------------------
    // Main panel widgets
    // -------------------------------------------------------------------------
    /**  */
    private final StandardFrameView frameView;
    /** The file tree displaying the directories in the given file system. */
    private final HexFileView editor;
    /** The serializer to access user options. */
    private final OptionsSerializer<JHexOptions> options;
    /** The recent files menu. */
    private final RecentFilesViews recentFiles;

    /** Index of the currently selected buffer size. */
    private HexBufferSize bufferSize;

    /***************************************************************************
     * Creates a JHex frame.
     **************************************************************************/
    public JHexFrame()
    {
        this( true );
    }

    /***************************************************************************
     * @param closeFileWithFrame
     **************************************************************************/
    public JHexFrame( boolean closeFileWithFrame )
    {
        this.options = JHexMain.getOptions();

        this.frameView = new StandardFrameView();
        this.editor = new HexFileView();
        this.recentFiles = new RecentFilesViews();

        this.bufferSize = HexBufferSize.LARGE;

        // ---------------------------------------------------------------------
        // Setup frame
        // ---------------------------------------------------------------------
        createMenuBar( frameView.getMenuBar(), frameView.getFileMenu() );

        frameView.setToolbar( createToolbar() );
        frameView.setContent( editor.getView() );
        frameView.setSize( 1000, 600 );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        JFrame frame = frameView.getView();
        if( closeFileWithFrame )
        {
            frame.addWindowListener( new WindowCloseListener( this ) );
        }
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setTitle( "JHex" );

        frame.setIconImages( JHexIcons.getAppImages() );

        recentFiles.setListeners( ( f, c ) -> openFile( f ) );
        editor.getView().setDropTarget(
            new FileDropTarget( new FileDroppedListener( this ) ) );
    }

    /***************************************************************************
     * Creates the toolbar.
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        recentFiles.install( toolbar, createOpenListener() );

        // SwingUtils.addActionToToolbar( toolbar,
        // new ActionAdapter( ( e ) -> saveFile(), "Save File",
        // IconConstants.getIcon( IconConstants.SAVE_16 ) ) );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar,
            new ActionAdapter( ( e ) -> showBufferSizeDialog(),
                "Configure Options",
                IconConstants.getIcon( IconConstants.CONFIG_16 ) ) );

        return toolbar;
    }

    private FileChooserListener createOpenListener()
    {
        IFileSelected ifs = ( f ) -> openFile( f );
        ILastFile ilf = () -> options.getOptions().lastAccessedFiles.first();
        FileChooserListener fcl = new FileChooserListener( getView(),
            "Open File", false, ifs, ilf );

        return fcl;
    }

    /***************************************************************************
     * Creates the menu bar.
     * @param menubar
     * @param fileMenu
     **************************************************************************/
    private JMenuBar createMenuBar( JMenuBar menubar, JMenu fileMenu )
    {
        createFileMenu( fileMenu );
        menubar.add( fileMenu );
        menubar.add( createNavMenu() );
        menubar.add( createToolsMenu() );

        updateFileMenu();

        return menubar;
    }

    /***************************************************************************
     * @param fileMenu
     **************************************************************************/
    private void createFileMenu( JMenu fileMenu )
    {
        JMenuItem item;
        int idx = 0;

        item = new JMenuItem( "Open" );
        item.addActionListener( createOpenListener() );
        item.setIcon( IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        fileMenu.add( item, idx++ );

        item = new JMenuItem( "Save" );
        item.addActionListener( ( e ) -> saveFile() );
        item.setIcon( IconConstants.getIcon( IconConstants.SAVE_16 ) );
        // fileMenu.add( item, idx++ );

        fileMenu.add( recentFiles.getMenu(), idx++ );

        fileMenu.add( new JSeparator(), idx++ );
    }

    /***************************************************************************
     * Replaces the recently opened files in the file menu with those in the
     * user data.
     * @param menu the file menu
     * @return
     **************************************************************************/
    private void updateFileMenu()
    {
        JHexOptions options = this.options.getOptions();

        recentFiles.setData( options.lastAccessedFiles.toList() );
    }

    /***************************************************************************
     * Creates the search menu.
     **************************************************************************/
    private JMenu createNavMenu()
    {
        JMenu menu = new JMenu( "Navigate" );

        menu.add( editor.gotoAction );
        menu.add( editor.searchAction );

        menu.addSeparator();

        menu.add( editor.firstAction );
        menu.add( editor.prevAction );
        menu.add( editor.nextAction );
        menu.add( editor.lastAction );

        return menu;
    }

    /***************************************************************************
     * Creates the tools menu.
     **************************************************************************/
    private JMenu createToolsMenu()
    {
        JMenu menu = new JMenu( "Tools" );
        JMenuItem item;

        item = new JMenuItem( "Set Buffer Size" );
        item.setIcon( IconConstants.getIcon( IconConstants.CONFIG_16 ) );
        item.addActionListener( ( e ) -> showBufferSizeDialog() );
        menu.add( item );

        return menu;
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
     * Displays the dialog that allows the user to change the size of the
     * buffer.
     **************************************************************************/
    private void showBufferSizeDialog()
    {
        Object ans = JOptionPane.showInputDialog( getView(),
            "Choose buffer size:", "Buffer Size", JOptionPane.QUESTION_MESSAGE,
            null, HexBufferSize.values(), bufferSize );

        if( ans != null )
        {
            bufferSize = ( HexBufferSize )ans;
            editor.setBufferSize( bufferSize.size );
        }
    }

    /***************************************************************************
     * Opens the provided file and displays its content.
     * @param f the file to be opened.
     **************************************************************************/
    public void openFile( File f )
    {
        if( !f.isFile() )
        {
            return;
        }

        JHexOptions options = this.options.getOptions();

        options.lastAccessedFiles.push( f );
        this.options.write();

        editor.setData( f );

        updateFileMenu();
    }

    /***************************************************************************
     * Saves the file.
     **************************************************************************/
    private void saveFile()
    {
        JOptionPane.showMessageDialog( getView(),
            "This functionality is not yet implemented.", "Not Yet Implemented",
            JOptionPane.INFORMATION_MESSAGE );

        if( "".length() < 1 )
        {
            return;
        }

        JFileChooser chooser = new JFileChooser();
        int choice = JFileChooser.CANCEL_OPTION;
        JHexOptions options = this.options.getOptions();

        chooser.setSelectedFile( options.getLastFile() );
        choice = chooser.showSaveDialog( getView() );

        if( choice == JFileChooser.APPROVE_OPTION )
        {
            File f = chooser.getSelectedFile();

            options.lastAccessedFiles.push( f );
            this.options.write();

            try
            {
                editor.saveFile( f );
            }
            catch( IOException ex )
            {
                ex.printStackTrace();
            }

            updateFileMenu();
        }
    }

    /***************************************************************************
     * Closes the currently opened file.
     **************************************************************************/
    public void closeFile()
    {
        try
        {
            editor.closeFile();
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( getView(), ex.getMessage(),
                "I/O Error", JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * Listener to open a file that is drag and dropped onto the table.
     **************************************************************************/
    private static class FileDroppedListener
        implements ItemActionListener<IFileDropEvent>
    {
        private final JHexFrame view;

        public FileDroppedListener( JHexFrame view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<IFileDropEvent> event )
        {
            IFileDropEvent dropEvent = event.getItem();
            List<File> files = dropEvent.getFiles();

            if( !files.isEmpty() )
            {
                view.openFile( files.get( 0 ) );
            }
        }
    }

    /***************************************************************************
     * Window listener to close the file.
     **************************************************************************/
    private static class WindowCloseListener extends WindowAdapter
    {
        private final JHexFrame frame;

        public WindowCloseListener( JHexFrame frame )
        {
            this.frame = frame;
        }

        @Override
        public void windowClosing( WindowEvent e )
        {
            // LogUtils.printDebug( "Window Closing" );
            frame.closeFile();
        }

        @Override
        public void windowClosed( WindowEvent e )
        {
            // LogUtils.printDebug( "Window Closed" );
            frame.closeFile();
        }
    }
}
