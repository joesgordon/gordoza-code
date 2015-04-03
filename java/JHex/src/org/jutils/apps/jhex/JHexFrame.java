package org.jutils.apps.jhex;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.io.OptionsSerializer;
import org.jutils.task.TaskView;
import org.jutils.ui.*;
import org.jutils.ui.event.*;
import org.jutils.ui.event.FileDropTarget.IFileDropEvent;
import org.jutils.ui.fields.HexBytesFormField;
import org.jutils.ui.hex.*;
import org.jutils.ui.hex.HexTable.IRangeSelectedListener;
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
    /** The actual window. */
    private final JFrame frame;
    /** The file tree displaying the directories in the given file system. */
    private final HexEditorFilePanel editor;
    /** The serializer to access user options. */
    private final OptionsSerializer<JHexOptions> userio;
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
    /** The file menu. */
    private final JMenu fileMenu;

    /** Index of the currently selected buffer size. */
    private HexBufferSize bufferSize;
    /**  */
    private byte [] lastSearch;

    /***************************************************************************
     * Creates a JHex frame.
     * @param userio the serializer used to access user data.
     **************************************************************************/
    public JHexFrame( OptionsSerializer<JHexOptions> userio )
    {
        this( userio, true );
    }

    /***************************************************************************
     * @param userio
     * @param closeFileWithFrame
     **************************************************************************/
    public JHexFrame( OptionsSerializer<JHexOptions> userio,
        boolean closeFileWithFrame )
    {
        this.userio = userio;

        this.frame = new JFrame();
        this.editor = new HexEditorFilePanel();
        this.dataViewButton = new JToggleButton();
        this.valuePanel = new ValueView();
        this.dataDialog = createDataDialog();
        this.fileMenu = new JMenu( "File" );

        this.bufferSize = HexBufferSize.LARGE;
        this.lastSearch = null;

        JPanel editorView = editor.getView();
        KeyStroke key;
        Action action;
        InputMap inMap = editorView.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW );
        ActionMap acMap = editorView.getActionMap();

        editorView.setDropTarget( new FileDropTarget( new FileDroppedListener(
            this ) ) );
        editor.addRangeSelectedListener( new SelectionListener( this ) );

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
        frame.setJMenuBar( createMenuBar() );

        frame.setContentPane( createContentPane() );

        if( closeFileWithFrame )
        {
            frame.addWindowListener( new WindowCloseListener( this ) );
        }
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setTitle( "JHex" );

        frame.setIconImages( JHexIconConstants.getAppImages() );
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
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 4,
                4, 4, 4 ), 0, 0 );
        panel.add( valuePanel.getView(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 );
        panel.add( new JSeparator(), constraints );

        okButton.addActionListener( new HideDialogListener( this ) );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 10,
                10, 10, 10 ), 30, 10 );
        panel.add( okButton, constraints );

        dialog.setTitle( "Data View" );
        dialog.setContentPane( panel );
        dialog.setAlwaysOnTop( false );
        dialog.setIconImages( IconConstants.loader.getImages( IconConstants.BINARY_32 ) );

        dialog.pack();
        dialog.setLocationRelativeTo( frame );

        return dialog;
    }

    /***************************************************************************
     * Creates the content pane for the frame. This frame contains the toolbar,
     * main panel, and status bar.
     **************************************************************************/
    private Container createContentPane()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        StatusBarPanel statusView = new StatusBarPanel();

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( editor.getView(), BorderLayout.CENTER );
        panel.add( statusView.getView(), BorderLayout.SOUTH );

        return panel;
    }

    /***************************************************************************
     * Creates the toolbar.
     **************************************************************************/
    private Component createToolbar()
    {
        JGoodiesToolBar toolbar = new JGoodiesToolBar();
        JButton button = new JButton();

        button = new JButton(
            IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        button.setToolTipText( "Open File" );
        button.setFocusable( false );
        button.addActionListener( new OpenListener( this ) );
        toolbar.add( button );

        button = new JButton(
            IconConstants.loader.getIcon( IconConstants.SAVE_16 ) );
        button.setToolTipText( "Save File (Not Yet Implemented)" );
        button.setFocusable( false );
        button.addActionListener( new SaveListener( this ) );
        // toolbar.add( button );

        toolbar.addSeparator();

        button = new JButton(
            JHexIconConstants.loader.getIcon( JHexIconConstants.JUMP_LEFT ) );
        button.setToolTipText( "Previous Data Block" );
        button.setFocusable( false );
        button.addActionListener( new BackListener( this ) );
        toolbar.add( button );

        button = new JButton(
            JHexIconConstants.loader.getIcon( JHexIconConstants.INCH_LEFT ) );
        button.setToolTipText( "Previous Data" );
        button.setFocusable( false );
        button.addActionListener( new BackListener( this ) );
        // toolbar.add( button );
        //
        button = new JButton(
            JHexIconConstants.loader.getIcon( JHexIconConstants.INCH_RIGHT ) );
        button.setToolTipText( "Next Data" );
        button.setFocusable( false );
        button.addActionListener( new NextListener( this ) );
        // toolbar.add( button );

        button = new JButton(
            JHexIconConstants.loader.getIcon( JHexIconConstants.JUMP_RIGHT ) );
        button.setToolTipText( "Next Data Block" );
        button.setFocusable( false );
        button.addActionListener( new NextListener( this ) );
        toolbar.add( button );

        toolbar.addSeparator();

        button = new JButton(
            IconConstants.loader.getIcon( IconConstants.FIND_16 ) );
        button.setToolTipText( "Find Bytes" );
        button.setFocusable( false );
        button.addActionListener( new SearchListener( this ) );
        toolbar.add( button );

        button = new JButton(
            JHexIconConstants.loader.getIcon( JHexIconConstants.GOTO ) );
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
        jtb.setIcon( JHexIconConstants.loader.getIcon( JHexIconConstants.SHOW_DATA ) );
        jtb.setToolTipText( "Show Data" );
        jtb.setFocusable( false );
        jtb.addActionListener( new ShowDataListener( this, jtb ) );
        toolbar.add( jtb );

        return toolbar;
    }

    /***************************************************************************
     * Creates the menu bar.
     **************************************************************************/
    private JMenuBar createMenuBar()
    {
        JMenuBar menubar = new JGoodiesMenuBar();

        updateFileMenu();

        menubar.add( fileMenu );
        menubar.add( createSearchMenu() );
        menubar.add( createToolsMenu() );

        return menubar;
    }

    /***************************************************************************
     * Replaces the recently opened files in the file menu with those in the
     * user data.
     * @param menu the file menu
     * @return
     **************************************************************************/
    private void updateFileMenu()
    {
        JMenuItem item;
        JHexOptions options = userio.getOptions();

        fileMenu.removeAll();

        item = new JMenuItem( "Open" );
        item.addActionListener( new OpenListener( this ) );
        item.setIcon( IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        fileMenu.add( item );

        item = new JMenuItem( "Save" );
        item.addActionListener( new SaveListener( this ) );
        item.setIcon( IconConstants.loader.getIcon( IconConstants.SAVE_16 ) );
        // fileMenu.add( item );

        if( !options.lastAccessedFiles.isEmpty() )
        {
            fileMenu.addSeparator();

            for( File f : options.lastAccessedFiles )
            {
                item = new JMenuItem( f.getName() );
                item.addActionListener( new OpenFileListener( this, f ) );
                fileMenu.add( item );
            }
        }

        fileMenu.addSeparator();

        item = new JMenuItem( "Exit" );
        item.addActionListener( new ExitListener( frame ) );
        item.setIcon( IconConstants.loader.getIcon( IconConstants.CLOSE_16 ) );
        fileMenu.add( item );
    }

    /***************************************************************************
     * Creates the search menu.
     **************************************************************************/
    private JMenu createSearchMenu()
    {
        JMenu menu = new JMenu( "Search" );
        JMenuItem item;

        item = new JMenuItem( "Go To Offset" );
        item.setIcon( JHexIconConstants.loader.getIcon( JHexIconConstants.GOTO ) );
        item.addActionListener( new GoToListener( this ) );
        menu.add( item );

        item = new JMenuItem( "Find" );
        item.setIcon( IconConstants.loader.getIcon( IconConstants.FIND_16 ) );
        item.addActionListener( new SearchListener( this ) );
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
        JHexOptions options = userio.getOptions();

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

        JHexOptions options = userio.getOptions();

        options.lastAccessedFiles.push( f );
        userio.write();

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
            "This functionality is not yet implemented.",
            "Not Yet Implemented", JOptionPane.INFORMATION_MESSAGE );

        if( "".length() < 1 )
        {
            return;
        }

        JFileChooser chooser = new JFileChooser();
        int choice = JFileChooser.CANCEL_OPTION;
        JHexOptions options = userio.getOptions();

        chooser.setSelectedFile( options.getLastFile() );
        choice = chooser.showSaveDialog( frame );

        if( choice == JFileChooser.APPROVE_OPTION )
        {
            File f = chooser.getSelectedFile();

            options.lastAccessedFiles.push( f );
            userio.write();

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
        ValidationView view = new ValidationView( hexField.getValidationField() );
        StandardFormView form = new StandardFormView( true );

        form.addField( hexField.getFieldName(), view.getView() );

        hexField.getTextField().addAncestorListener( new RequestFocusListener() );

        int ans = JOptionPane.showOptionDialog( frame, form.getView(),
            "Enter Hexadecimal String", JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, null, null );

        if( ans == JOptionPane.OK_OPTION )
        {
            if( !hexField.getValidationField().isValid() )
            {
                JOptionPane.showMessageDialog( frame,
                    hexField.getValidationField().getInvalidationReason(),
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

        SearchTask task = new SearchTask( bytes, editor.getStream(),
            fromOffset, isForward );

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
            long offset = -1;

            try
            {
                offset = Long.parseLong( ans.toString(), 16 );
            }
            catch( NumberFormatException ex )
            {
                JOptionPane.showMessageDialog( frame, "'" + ans.toString() +
                    "' is not a hexadecimal string.", "ERROR",
                    JOptionPane.ERROR_MESSAGE );
                return;
            }

            editor.highlightOffset( offset, 1 );
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
     * Action listener for displaying the file open dialog.
     **************************************************************************/
    private class OpenListener implements ActionListener
    {
        private final JHexFrame frame;

        public OpenListener( JHexFrame frame )
        {
            this.frame = frame;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            frame.showOpenDialog();
        }
    }

    /***************************************************************************
     * Action listener for opening a particular file.
     **************************************************************************/
    private class OpenFileListener implements ActionListener
    {
        private final JHexFrame frame;
        private final File file;

        public OpenFileListener( JHexFrame frame, File f )
        {
            this.frame = frame;
            this.file = f;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            frame.openFile( file );
        }
    }

    /***************************************************************************
     * Action listener for saving the file.
     **************************************************************************/
    private static class SaveListener implements ActionListener
    {
        private final JHexFrame frame;

        public SaveListener( JHexFrame frame )
        {
            this.frame = frame;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            frame.saveFile();
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
    private static class FileDroppedListener implements
        ItemActionListener<IFileDropEvent>
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

        public void actionPerformed( ActionEvent e )
        {
            view.dataDialog.setVisible( button.isSelected() );

            if( button.isSelected() )
            {
                view.editor.setHighlightLength( view.valuePanel.getSelectedSize() );
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
            frame.valuePanel.setBytes( frame.editor.getBuffer().getBytes(), end );

            // LogUtils.printDebug( "col: " + col + ", row: " + row +
            // ", start: "
            // +
            // start + ", end: " + end );
        }
    }

    /***************************************************************************
     * Listener to update the buffer size.
     **************************************************************************/
    private static class SizeSelectedListener implements
        ItemActionListener<Integer>
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
                view.search( view.lastSearch, off, isForward );
            }
        }
    }
}
