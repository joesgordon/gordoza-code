package org.jutils.apps.jhex.ui;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.*;
import java.io.*;
import java.util.List;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.apps.jhex.JHexIcons;
import org.jutils.apps.jhex.JHexMain;
import org.jutils.apps.jhex.data.JHexOptions;
import org.jutils.apps.jhex.task.DataDistributionTask;
import org.jutils.apps.jhex.task.SearchTask;
import org.jutils.chart.ChartIcons;
import org.jutils.datadist.DataDistribution;
import org.jutils.io.IStream;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.task.TaskView;
import org.jutils.ui.*;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.event.*;
import org.jutils.ui.event.FileDropTarget.IFileDropEvent;
import org.jutils.ui.fields.HexBytesFormField;
import org.jutils.ui.hex.HexBufferSize;
import org.jutils.ui.hex.HexFileView;
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
    private final RecentFilesMenuView recentFiles;

    /**  */
    private final Action prevAction;
    /**  */
    private final Action nextAction;
    /**  */
    private final Action searchAction;
    /**  */
    private final Action gotoAction;
    /**  */
    private final Action analyzeAction;
    /**  */
    private final Action plotAction;

    /** Index of the currently selected buffer size. */
    private HexBufferSize bufferSize;
    /**  */
    private byte [] lastSearch;

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
        this.recentFiles = new RecentFilesMenuView();

        this.bufferSize = HexBufferSize.LARGE;
        this.lastSearch = null;

        this.prevAction = new ActionAdapter( ( e ) -> editor.jumpPrevious(),
            "Previous Data Block", JHexIcons.getIcon( JHexIcons.JUMP_LEFT ) );
        this.nextAction = new ActionAdapter( ( e ) -> editor.jumpForward(),
            "Next Data Block", JHexIcons.getIcon( JHexIcons.JUMP_RIGHT ) );

        this.searchAction = new ActionAdapter( ( e ) -> showSearchDialog(),
            "Search", IconConstants.getIcon( IconConstants.FIND_16 ) );
        this.gotoAction = new ActionAdapter( ( e ) -> showGotoDialog(),
            "Go To Byte", JHexIcons.loader.getIcon( JHexIcons.GOTO ) );

        this.analyzeAction = new ActionAdapter( ( e ) -> showAnalyzer(),
            "Analyze", IconConstants.getIcon( IconConstants.ANALYZE_16 ) );
        this.plotAction = new ActionAdapter( ( e ) -> showPlot(), "Plot",
            ChartIcons.getIcon( ChartIcons.CHART_016 ) );

        JPanel editorView = editor.getView();
        KeyStroke key;
        Action action;
        InputMap inMap = editorView.getInputMap(
            JComponent.WHEN_IN_FOCUSED_WINDOW );
        ActionMap acMap = editorView.getActionMap();

        editorView.setDropTarget(
            new FileDropTarget( new FileDroppedListener( this ) ) );

        key = KeyStroke.getKeyStroke( "control F" );
        searchAction.putValue( Action.ACCELERATOR_KEY, key );
        // inMap.put( key, "findAction" );
        // acMap.put( "findAction", findAction );
        searchAction.putValue( Action.MNEMONIC_KEY, ( int )'F' );

        action = new ActionAdapter( new FindAgainListener( this ), "Find Next",
            null );
        key = KeyStroke.getKeyStroke( "F3" );
        action.putValue( Action.ACCELERATOR_KEY, key );
        inMap.put( key, "findNextAction" );
        acMap.put( "findNextAction", action );

        action = new ActionAdapter( new FindAgainListener( this, false ),
            "Find Previous", null );
        key = KeyStroke.getKeyStroke( "shift F3" );
        action.putValue( Action.ACCELERATOR_KEY, key );
        inMap.put( key, "findPrevAction" );
        acMap.put( "findPrevAction", action );

        prevAction.setEnabled( false );
        nextAction.setEnabled( false );
        searchAction.setEnabled( false );
        gotoAction.setEnabled( false );
        analyzeAction.setEnabled( false );
        plotAction.setEnabled( false );

        // ---------------------------------------------------------------------
        // Setup frame
        // ---------------------------------------------------------------------
        createMenuBar( frameView.getMenuBar(), frameView.getFileMenu() );

        frameView.setToolbar( createToolbar() );
        frameView.setContent( editor.getView() );

        JFrame frame = frameView.getView();
        if( closeFileWithFrame )
        {
            frame.addWindowListener( new WindowCloseListener( this ) );
        }
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setTitle( "JHex" );

        frame.setIconImages( JHexIcons.getAppImages() );

        recentFiles.addSelectedListener( ( f, c ) -> openFile( f ) );
    }

    /***************************************************************************
     * Creates the toolbar.
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        SwingUtils.addActionToToolbar( toolbar,
            new ActionAdapter( ( e ) -> showOpenDialog(), "Open File",
                IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 ) ) );

        // SwingUtils.addActionToToolbar( toolbar,
        // new ActionAdapter( ( e ) -> saveFile(), "Save File",
        // IconConstants.getIcon( IconConstants.SAVE_16 ) ) );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, prevAction );

        // SwingUtils.addActionToToolbar( toolbar, new ActionAdapter( null,
        // "Previous Data", JHexIcons.getIcon( JHexIcons.INCH_LEFT ) ) );
        //
        // SwingUtils.addActionToToolbar( toolbar, new ActionAdapter( null,
        // "Next Data", JHexIcons.getIcon( JHexIcons.INCH_RIGHT ) ) );

        SwingUtils.addActionToToolbar( toolbar, nextAction );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, searchAction );
        SwingUtils.addActionToToolbar( toolbar, gotoAction );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, analyzeAction );
        SwingUtils.addActionToToolbar( toolbar, plotAction );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar,
            new ActionAdapter( ( e ) -> showBufferSizeDialog(),
                "Configure Options",
                IconConstants.getIcon( IconConstants.CONFIG_16 ) ) );

        return toolbar;
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
        menubar.add( createSearchMenu() );
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
        item.addActionListener( ( e ) -> showOpenDialog() );
        item.setIcon( IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        fileMenu.add( item, idx++ );

        item = new JMenuItem( "Save" );
        item.addActionListener( ( e ) -> saveFile() );
        item.setIcon( IconConstants.getIcon( IconConstants.SAVE_16 ) );
        // fileMenu.add( item, idx++ );

        fileMenu.add( recentFiles.getView(), idx++ );

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
    private JMenu createSearchMenu()
    {
        JMenu menu = new JMenu( "Navigate" );
        JMenuItem item;

        item = new JMenuItem( "Go To Offset" );
        item.setIcon( JHexIcons.loader.getIcon( JHexIcons.GOTO ) );
        item.addActionListener( ( e ) -> showGotoDialog() );
        menu.add( item );

        item = new JMenuItem( searchAction );
        menu.add( item );

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
     * 
     **************************************************************************/
    public void showAnalyzer()
    {
        try( IStream stream = editor.openStreamCopy() )
        {
            if( stream == null )
            {
                return;
            }

            DataDistributionTask ddt = new DataDistributionTask( stream );

            TaskView.startAndShow( getView(), ddt, "Analyzing Data" );

            DataDistribution dist = ddt.getDistribution();

            if( dist != null )
            {
                VerboseMessageView msgView = new VerboseMessageView();

                msgView.setMessages( "Finished Analyzing",
                    dist.getDescription() );

                msgView.show( getView(), "Finished Analyzing" );
            }
        }
        catch( FileNotFoundException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        catch( IOException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showPlot()
    {
        try( IStream stream = editor.openStreamCopy() )
        {
            if( stream == null )
            {
                return;
            }

            Window w = SwingUtils.getComponentsWindow( getView() );
            DataPlotView plotView = new DataPlotView( stream );
            OkDialogView dialogView = new OkDialogView( w, plotView.getView(),
                ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_ONLY );

            dialogView.setOkButtonText( "Close" );

            dialogView.show( "Data Plot", JHexIcons.getAppImages(),
                new Dimension( 640, 480 ) );
        }
        catch( FileNotFoundException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        catch( IOException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
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
     * Displays the dialog that allows the user to open a file.
     **************************************************************************/
    private void showOpenDialog()
    {
        JFileChooser chooser = new JFileChooser();
        int choice = JFileChooser.CANCEL_OPTION;
        JHexOptions options = this.options.getOptions();

        chooser.setSelectedFile( options.getLastFile() );
        choice = chooser.showOpenDialog( getView() );

        if( choice == JFileChooser.APPROVE_OPTION )
        {
            File f = chooser.getSelectedFile();
            openFile( f );
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

        try
        {
            editor.openFile( f );

            boolean enabled = editor.isOpen();

            prevAction.setEnabled( enabled );
            nextAction.setEnabled( enabled );

            searchAction.setEnabled( enabled );
            gotoAction.setEnabled( enabled );

            analyzeAction.setEnabled( enabled );
            plotAction.setEnabled( enabled );
        }
        catch( IOException ex )
        {
            SwingUtils.showErrorMessage( getView(), ex.getMessage(),
                "I/O Error" );
        }

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
     * Displays the dialog that allows the user to enter bytes to be found.
     **************************************************************************/
    private void showSearchDialog()
    {
        if( !editor.isOpen() )
        {
            return;
        }

        HexBytesFormField hexField = new HexBytesFormField( "Hex Bytes" );
        StandardFormView form = new StandardFormView( true );

        form.addField( hexField.getName(), hexField.getView() );

        hexField.getTextField().addAncestorListener(
            new RequestFocusListener() );

        int ans = JOptionPane.showOptionDialog( getView(), form.getView(),
            "Enter Hexadecimal String", JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, null, null );

        if( ans == JOptionPane.OK_OPTION )
        {
            if( !hexField.getValidity().isValid )
            {
                JOptionPane.showMessageDialog( getView(),
                    hexField.getValidity().reason, "Invalid Hexadecimal Entry",
                    JOptionPane.ERROR_MESSAGE );
                return;
            }

            byte [] bytes = hexField.getValue();
            long fromOffset = editor.getSelectedOffset();

            fromOffset = fromOffset > -1 ? fromOffset : 0;

            search( bytes, fromOffset );
        }
        // else
        // {
        // LogUtils.printDebug( "cancelled" );
        // }
    }

    /***************************************************************************
     * @param bytes
     * @param fromOffset
     **************************************************************************/
    private void search( byte [] bytes, long fromOffset )
    {
        search( bytes, fromOffset, true );
    }

    /***************************************************************************
     * @param bytes
     * @param fromOffset
     * @param isForward
     **************************************************************************/
    private void search( byte [] bytes, long fromOffset, boolean isForward )
    {
        this.lastSearch = bytes;

        // LogUtils.printDebug( "Searching for: " + HexUtils.toHexString( bytes
        // ) +
        // " @ " + fromOffset + " " + ( isForward ? "Forward" : "Backward" ) );

        try( IStream stream = editor.openStreamCopy() )
        {
            if( stream == null )
            {
                return;
            }

            SearchTask task = new SearchTask( bytes, stream, fromOffset,
                isForward );

            TaskView.startAndShow( getView(), task, "Byte Search" );

            long foundOffset = task.foundOffset;

            if( foundOffset > -1 )
            {
                editor.highlightOffset( foundOffset, bytes.length );
            }
        }
        catch( FileNotFoundException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        catch( IOException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     * Displays the dialog that allows the user to go to a particular offset
     * into the file.
     **************************************************************************/
    private void showGotoDialog()
    {
        Object ans = JOptionPane.showInputDialog( getView(),
            "Enter Offset in hexadecimal:", new Integer( 0 ) );
        if( ans != null )
        {
            try
            {
                long offset = Long.parseLong( ans.toString(), 16 );
                editor.highlightOffset( offset, 1 );
            }
            catch( NumberFormatException ex )
            {
                JOptionPane.showMessageDialog( getView(),
                    "'" + ans.toString() + "' is not a hexadecimal string.",
                    "ERROR", JOptionPane.ERROR_MESSAGE );
            }
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

    /***************************************************************************
     * Listener to open a file that is drag and dropped onto the table.
     **************************************************************************/
    private static class FileDroppedListener
        implements ItemActionListener<IFileDropEvent>
    {
        private final JHexFrame frame;

        public FileDroppedListener( JHexFrame view )
        {
            this.frame = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<IFileDropEvent> event )
        {
            IFileDropEvent dropEvent = event.getItem();
            List<File> files = dropEvent.getFiles();

            if( !files.isEmpty() )
            {
                frame.openFile( files.get( 0 ) );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FindAgainListener implements ActionListener
    {
        private final JHexFrame view;
        private final boolean isForward;

        public FindAgainListener( JHexFrame view )
        {
            this( view, true );
        }

        public FindAgainListener( JHexFrame view, boolean forward )
        {
            this.view = view;
            this.isForward = forward;
        }

        @Override
        public synchronized void actionPerformed( ActionEvent e )
        {
            if( view.lastSearch != null )
            {
                long off = view.editor.getSelectedOffset();
                off = off + ( isForward ? 1 : -1 );

                // LogUtils.printDebug( "Searching for: " +
                // HexUtils.toHexString( view.lastSearch ) + " @ " +
                // String.format( "%016X", off ) + " " +
                // ( isForward ? "Forward" : "Backward" ) );

                view.search( view.lastSearch, off, isForward );
            }
        }
    }
}
