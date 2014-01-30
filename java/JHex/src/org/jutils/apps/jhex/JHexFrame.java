package org.jutils.apps.jhex;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.NumberParsingUtils;
import org.jutils.io.UserOptionsSerializer;
import org.jutils.ui.*;
import org.jutils.ui.event.*;
import org.jutils.ui.event.FileDropTarget.IFileDropEvent;
import org.jutils.ui.hex.*;
import org.jutils.ui.hex.HexTable.IRangeSelectedListener;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * Represents the view that builds and contains the main frame for the
 * application.
 ******************************************************************************/
public class JHexFrame implements IView<JFrame>
{
    // TODO create enum of sizes.
    /** The text description of the sizes. */
    private static final String [] choices = new String[] { "Xtra-Small (1kb)",
        "Small (64kb)", "Medium (512 kb)", "Large (1 Mb)" };
    /** The size of the buffer in bytes. */
    private static final int [] sizes = new int[] { 0x400, 0x10000, 0x80000,
        0x100000 };

    // -------------------------------------------------------------------------
    // Main panel widgets
    // -------------------------------------------------------------------------
    /** The actual window. */
    private final JFrame frame;
    /** The file tree displaying the directories in the given file system. */
    private final HexEditorFilePanel editor;
    /** The serializer to access user options. */
    private final UserOptionsSerializer<JHexOptions> userio;
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
    private int bufferSizeIndex;

    /***************************************************************************
     * Creates a JHex frame.
     * @param userio the serializer used to access user data.
     **************************************************************************/
    public JHexFrame( UserOptionsSerializer<JHexOptions> userio )
    {
        this( userio, true );
    }

    public JHexFrame( UserOptionsSerializer<JHexOptions> userio,
        boolean closeFileWithFrame )
    {
        this.userio = userio;

        this.frame = new JFrame();
        this.editor = new HexEditorFilePanel();
        this.dataViewButton = new JToggleButton();
        this.valuePanel = new ValueView();
        this.dataDialog = createDataDialog();
        this.fileMenu = new JMenu( "File" );

        this.bufferSizeIndex = choices.length - 1;

        editor.getView().setDropTarget(
            new FileDropTarget( new FileDroppedListener( this ) ) );
        editor.addRangeSelectedListener( new SelectionListener( this ) );

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
        button.addActionListener( new FindListener( this ) );
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
        item.addActionListener( new FindListener( this ) );
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
            "Buffer Size", JOptionPane.QUESTION_MESSAGE, null, choices,
            choices[bufferSizeIndex] );

        if( ans != null )
        {
            for( int i = 0; i < choices.length; i++ )
            {
                if( choices[i].equals( ans ) )
                {
                    editor.setBufferSize( sizes[i] );
                    bufferSizeIndex = i;
                }
            }
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
    private void showFindDialog()
    {
        Object ans = JOptionPane.showInputDialog( frame, "Enter hex string:",
            new Integer( 0 ) );
        if( ans != null )
        {
            String strBytes = ans.toString();

            if( ( strBytes.length() & 0x01 ) != 0 )
            {
                JOptionPane.showMessageDialog( frame,
                    "Cannot search for nibbles.", "ERROR",
                    JOptionPane.ERROR_MESSAGE );
                return;
            }

            byte [] bytes = new byte[strBytes.length() / 2];

            try
            {
                for( int i = 0; i < bytes.length; i++ )
                {
                    int b;

                    b = NumberParsingUtils.digitFromHex( strBytes.charAt( i * 2 ) ) & 0x0F;
                    bytes[i] = ( byte )( b << 4 );

                    b = NumberParsingUtils.digitFromHex( strBytes.charAt( i * 2 + 1 ) ) & 0x0F;
                    bytes[i] |= ( byte )b;
                }
                System.out.println( HexUtils.toHexString( bytes ) );

                // TODO actually find the bytes.
            }
            catch( NumberFormatException ex )
            {
                JOptionPane.showMessageDialog( frame, "'" + ans.toString() +
                    "' is not a hexadecimal string.", "ERROR",
                    JOptionPane.ERROR_MESSAGE );
                return;
            }
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

            try
            {
                editor.setStartOffset( offset );
            }
            catch( IOException ex )
            {
                JOptionPane.showMessageDialog( frame, ex.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE );
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
    private static class FindListener implements ActionListener
    {
        private final JHexFrame frame;

        public FindListener( JHexFrame frame )
        {
            this.frame = frame;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            frame.showFindDialog();
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
            // System.out.println( "Window Closing" );
            frame.closeFile();
        }

        @Override
        public void windowClosed( WindowEvent e )
        {
            // System.out.println( "Window Closed" );
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

            // System.out.println( "col: " + col + ", row: " + row + ", start: "
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
}
