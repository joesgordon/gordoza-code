package org.jutils.apps.jhex.ui;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.apps.jhex.JHexIcons;
import org.jutils.apps.jhex.JHexMain;
import org.jutils.apps.jhex.data.HexBufferSize;
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
import org.jutils.ui.hex.HexEditorFilePanel;
import org.jutils.ui.hex.HexTable.IRangeSelectedListener;
import org.jutils.ui.hex.ValueView;
import org.jutils.ui.model.IView;
import org.jutils.ui.validation.ValidationView;

/*******************************************************************************
 * Represents the view that builds and contains the main frame for the
 * application.
 ******************************************************************************/
public class JHexFrame implements IView<JFrame>
{
    // -------------------------------------------------------------------------
    // Main panel widgets
    // -------------------------------------------------------------------------
    private final StandardFrameView frameView;
    /** The actual window. */
    private final JFrame frame;
    /** The file tree displaying the directories in the given file system. */
    private final HexEditorFilePanel editor;
    /** The serializer to access user options. */
    private final OptionsSerializer<JHexOptions> options;
    /**
     * The dialog to show the data in different formats starting at the
     * currently selected byte.
     */
    private final JDialog dataDialog;
    /**
     * The view to show the data in different formats starting at the currently
     * selected byte.
     */
    private final ValueView valuePanel;
    /** The button to toggle the data view between visible and invisible. */
    private final JToggleButton dataViewButton;
    /** The recent files menu. */
    private final RecentFilesMenuView recentFiles;

    /**  */
    private final ActionAdapter searchAction;

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
        this.frame = frameView.getView();
        this.editor = new HexEditorFilePanel();
        this.dataViewButton = new JToggleButton();
        this.valuePanel = new ValueView();
        this.dataDialog = createDataDialog();
        this.recentFiles = new RecentFilesMenuView();

        this.bufferSize = HexBufferSize.LARGE;
        this.lastSearch = null;

        this.searchAction = new ActionAdapter( new SearchListener( this ),
            "Search", IconConstants.loader.getIcon( IconConstants.FIND_16 ) );

        JPanel editorView = editor.getView();
        KeyStroke key;
        Action action;
        InputMap inMap = editorView.getInputMap(
            JComponent.WHEN_IN_FOCUSED_WINDOW );
        ActionMap acMap = editorView.getActionMap();

        editorView.setDropTarget(
            new FileDropTarget( new FileDroppedListener( this ) ) );
        editor.addRangeSelectedListener( new SelectionListener( this ) );

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

        // ---------------------------------------------------------------------
        // Setup frame
        // ---------------------------------------------------------------------
        createMenuBar( frameView.getMenuBar(), frameView.getFileMenu() );

        frameView.setToolbar( createToolbar() );
        frameView.setContent( editor.getView() );

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
     * Creates the data dialog.
     **************************************************************************/
    private JDialog createDataDialog()
    {
        JDialog dialog = new JDialog( frame, ModalityType.MODELESS );
        JPanel panel = new JPanel( new GridBagLayout() );
        JButton okButton = new JButton( "OK" );
        GridBagConstraints constraints;

        valuePanel.addSizeSelectedListener( new SizeSelectedListener( this ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( valuePanel.getView(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( new JSeparator(), constraints );

        okButton.addActionListener( new HideDialogListener( this ) );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 10, 10, 10, 10 ), 30, 10 );
        panel.add( okButton, constraints );

        dialog.setTitle( "Data View" );
        dialog.setContentPane( panel );
        dialog.setAlwaysOnTop( false );
        dialog.setIconImages( JHexIcons.getAppImages() );

        dialog.pack();
        dialog.setLocationRelativeTo( frame );

        return dialog;
    }

    /***************************************************************************
     * Creates the toolbar.
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();
        JButton button;

        button = new JButton(
            IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        button.setToolTipText( "Open File" );
        button.setFocusable( false );
        button.addActionListener( ( e ) -> showOpenDialog() );
        toolbar.add( button );

        button = new JButton(
            IconConstants.loader.getIcon( IconConstants.SAVE_16 ) );
        button.setToolTipText( "Save File (Not Yet Implemented)" );
        button.setFocusable( false );
        button.addActionListener( ( e ) -> saveFile() );
        // toolbar.add( button );

        toolbar.addSeparator();

        button = new JButton( JHexIcons.loader.getIcon( JHexIcons.JUMP_LEFT ) );
        button.setToolTipText( "Previous Data Block" );
        button.setFocusable( false );
        button.addActionListener( new BackListener( this ) );
        toolbar.add( button );

        button = new JButton( JHexIcons.loader.getIcon( JHexIcons.INCH_LEFT ) );
        button.setToolTipText( "Previous Data" );
        button.setFocusable( false );
        button.addActionListener( new BackListener( this ) );
        // toolbar.add( button );
        //
        button = new JButton(
            JHexIcons.loader.getIcon( JHexIcons.INCH_RIGHT ) );
        button.setToolTipText( "Next Data" );
        button.setFocusable( false );
        button.addActionListener( new NextListener( this ) );
        // toolbar.add( button );

        button = new JButton(
            JHexIcons.loader.getIcon( JHexIcons.JUMP_RIGHT ) );
        button.setToolTipText( "Next Data Block" );
        button.setFocusable( false );
        button.addActionListener( new NextListener( this ) );
        toolbar.add( button );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, searchAction );

        button = new JButton( JHexIcons.loader.getIcon( JHexIcons.GOTO ) );
        button.setToolTipText( "Go To Byte" );
        button.setFocusable( false );
        button.addActionListener( new GoToListener( this ) );
        toolbar.add( button );

        button = new JButton(
            IconConstants.loader.getIcon( IconConstants.CONFIG_16 ) );
        button.setToolTipText( "Configure Options" );
        button.setFocusable( false );
        button.addActionListener( new BufferSizeListener( this ) );
        toolbar.add( button );

        JToggleButton jtb = dataViewButton;
        jtb.setIcon( JHexIcons.loader.getIcon( JHexIcons.SHOW_DATA ) );
        jtb.setToolTipText( "Show Data" );
        jtb.setFocusable( false );
        jtb.addActionListener( new ShowDataListener( this, jtb ) );
        toolbar.add( jtb );

        button = new JButton(
            IconConstants.loader.getIcon( IconConstants.ANALYZE_16 ) );
        button.setToolTipText( "Analyze" );
        button.setFocusable( false );
        button.addActionListener( ( e ) -> showAnalyzer() );
        toolbar.add( button );

        SwingUtils.addActionToToolbar( toolbar, createPlotAction() );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createPlotAction()
    {
        Icon icon;

        icon = ChartIcons.getIcon( ChartIcons.CHART_016 );

        return new ActionAdapter( ( e ) -> showPlot(), "Plot", icon );
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
        item.setIcon(
            IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        fileMenu.add( item, idx++ );

        item = new JMenuItem( "Save" );
        item.addActionListener( ( e ) -> saveFile() );
        item.setIcon( IconConstants.loader.getIcon( IconConstants.SAVE_16 ) );
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
        item.addActionListener( new GoToListener( this ) );
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
        item.setIcon( IconConstants.loader.getIcon( IconConstants.CONFIG_16 ) );
        item.addActionListener( new BufferSizeListener( this ) );
        menu.add( item );

        return menu;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void showAnalyzer()
    {
        @SuppressWarnings( "resource")
        IStream stream = editor.getStream();
        DataDistributionTask ddt = new DataDistributionTask( stream );

        TaskView.startAndShow( getView(), ddt, "Analyzing Data" );

        DataDistribution dist = ddt.getDistribution();

        if( dist != null )
        {
            VerboseMessageView msgView = new VerboseMessageView();

            msgView.setMessages( "Finished Analyzing", dist.getDescription() );

            msgView.show( getView(), "Finished Analyzing" );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showPlot()
    {
        @SuppressWarnings( "resource")
        IStream stream = editor.getStream();

        if( stream == null )
        {
            return;
        }

        Window w = SwingUtils.getComponentsWindow( frame );
        DataPlotView plotView = new DataPlotView( stream );
        OkDialogView dialogView = new OkDialogView( w, plotView.getView(),
            ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_ONLY );

        dialogView.setOkButtonText( "Close" );

        dialogView.show( "Data Plot", JHexIcons.getAppImages(),
            new Dimension( 640, 480 ) );
    }

    /***************************************************************************
     * Displays the dialog that allows the user to change the size of the
     * buffer.
     **************************************************************************/
    private void showBufferSizeDialog()
    {
        Object ans = JOptionPane.showInputDialog( frame, "Choose buffer size:",
            "Buffer Size", JOptionPane.QUESTION_MESSAGE, null,
            HexBufferSize.values(), bufferSize );

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
        choice = chooser.showOpenDialog( frame );

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
            editor.setFile( f );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( frame, ex.getMessage(), "I/O Error",
                JOptionPane.ERROR_MESSAGE );
        }

        updateFileMenu();
    }

    /***************************************************************************
     * Saves the file.
     **************************************************************************/
    private void saveFile()
    {
        JOptionPane.showMessageDialog( frame,
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
        choice = chooser.showSaveDialog( frame );

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
        HexBytesFormField hexField = new HexBytesFormField( "Hex Bytes" );
        ValidationView view = new ValidationView(
            hexField.getValidationField() );
        StandardFormView form = new StandardFormView( true );

        form.addField( hexField.getFieldName(), view.getView() );

        hexField.getTextField().addAncestorListener(
            new RequestFocusListener() );

        int ans = JOptionPane.showOptionDialog( frame, form.getView(),
            "Enter Hexadecimal String", JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, null, null );

        if( ans == JOptionPane.OK_OPTION )
        {
            if( !hexField.getValidationField().getValidity().isValid )
            {
                JOptionPane.showMessageDialog( frame,
                    hexField.getValidationField().getValidity().reason,
                    "Invalid Hexadecimal Entry", JOptionPane.ERROR_MESSAGE );
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

        SearchTask task = new SearchTask( bytes, editor.getStream(), fromOffset,
            isForward );

        TaskView.startAndShow( frame, task, "Byte Search" );

        long foundOffset = task.foundOffset;

        if( foundOffset > -1 )
        {
            editor.highlightOffset( foundOffset, bytes.length );
        }
    }

    /***************************************************************************
     * Displays the dialog that allows the user to go to a particular offset
     * into the file.
     **************************************************************************/
    private void showGotoDialog()
    {
        Object ans = JOptionPane.showInputDialog( frame,
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
                JOptionPane.showMessageDialog( frame,
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
            JOptionPane.showMessageDialog( frame, ex.getMessage(), "I/O Error",
                JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * Action listener for displaying the go to dialog.
     **************************************************************************/
    private static class GoToListener implements ActionListener
    {
        private final JHexFrame frame;

        public GoToListener( JHexFrame frame )
        {
            this.frame = frame;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            frame.showGotoDialog();
        }
    }

    /***************************************************************************
     * Action listener for displaying the find dialog.
     **************************************************************************/
    private static class SearchListener implements ActionListener
    {
        private final JHexFrame frame;

        public SearchListener( JHexFrame frame )
        {
            this.frame = frame;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            if( frame.editor.getStream() != null )
            {
                frame.showSearchDialog();
            }
        }
    }

    /***************************************************************************
     * Action listener for displaying the buffer size dialog.
     **************************************************************************/
    private static class BufferSizeListener implements ActionListener
    {
        private final JHexFrame frame;

        public BufferSizeListener( JHexFrame frame )
        {
            this.frame = frame;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            frame.showBufferSizeDialog();
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
     * Action listener for navigating to the previous buffer.
     **************************************************************************/
    private static class BackListener implements ActionListener
    {
        private final JHexFrame frame;

        public BackListener( JHexFrame view )
        {
            this.frame = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            frame.editor.jumpPrevious();
        }
    }

    /***************************************************************************
     * Action listener for navigating to the next buffer.
     **************************************************************************/
    private static class NextListener implements ActionListener
    {
        private final JHexFrame frame;

        public NextListener( JHexFrame view )
        {
            this.frame = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            frame.editor.jumpForward();
        }
    }

    /***************************************************************************
     * Action listener for displaying the data dialog.
     **************************************************************************/
    private static class ShowDataListener implements ActionListener
    {
        private final JHexFrame view;
        private final JToggleButton button;

        public ShowDataListener( JHexFrame view, JToggleButton jtb )
        {
            this.view = view;
            this.button = jtb;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            view.dataDialog.setVisible( button.isSelected() );

            if( button.isSelected() )
            {
                view.editor.setHighlightLength(
                    view.valuePanel.getSelectedSize() );
            }
            else
            {
                view.editor.setHighlightLength( -1 );
            }
        }
    }

    /***************************************************************************
     * Action listener for hiding the dialog.
     **************************************************************************/
    private static class HideDialogListener implements ActionListener
    {
        private final JHexFrame frame;

        public HideDialogListener( JHexFrame view )
        {
            this.frame = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            frame.dataDialog.setVisible( false );
            frame.dataViewButton.setSelected( false );
            frame.editor.setHighlightLength( -1 );
        }
    }

    /***************************************************************************
     * Listener to update the data dialog as bytes are selected.
     **************************************************************************/
    private static class SelectionListener implements IRangeSelectedListener
    {
        private final JHexFrame frame;

        public SelectionListener( JHexFrame view )
        {
            this.frame = view;
        }

        @Override
        public void rangeSelected( int start, int end )
        {
            frame.valuePanel.setBytes( frame.editor.getBuffer().getBytes(),
                end );

            // LogUtils.printDebug( "col: " + col + ", row: " + row +
            // ", start: "
            // +
            // start + ", end: " + end );
        }
    }

    /***************************************************************************
     * Listener to update the buffer size.
     **************************************************************************/
    private static class SizeSelectedListener
        implements ItemActionListener<Integer>
    {
        private final JHexFrame frame;

        public SizeSelectedListener( JHexFrame frame )
        {
            this.frame = frame;
        }

        @Override
        public void actionPerformed( ItemActionEvent<Integer> event )
        {
            int len = event.getItem() == null ? -1 : event.getItem();

            frame.editor.setHighlightLength( len );
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
