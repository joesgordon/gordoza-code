package org.jutils.apps.jhex;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

import org.jutils.*;
import org.jutils.io.UserOptionsSerializer;
import org.jutils.ui.*;
import org.jutils.ui.event.*;
import org.jutils.ui.event.FileDropTarget.IFileDropEvent;
import org.jutils.ui.hex.*;
import org.jutils.ui.hex.HexTable.IRangeSelectedListener;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JHexFrame implements IView<JFrame>
{
    /**  */
    private static final String[] choices = new String[] { "Xtra-Small (1kb)",
        "Small (64kb)", "Medium (512 kb)", "Large (1 Mb)" };
    /**  */
    private static final int[] sizes = new int[] { 0x400, 0x10000, 0x80000,
        0x100000 };

    // -------------------------------------------------------------------------
    // Main panel widgets
    // -------------------------------------------------------------------------
    /** The actual window. */
    private final JFrame frame;
    /** The file tree displaying the directories in the given file system. */
    private final HexEditorFilePanel editor;
    /**  */
    private final UserOptionsSerializer<JHexOptions> userDataIO;
    /**  */
    private final JDialog dataDialog;
    /**  */
    private final ValueView valuePanel;
    /**  */
    private final JToggleButton dataViewButton;

    /**  */
    private int bufferSizeIndex;

    /***************************************************************************
     * Creates a JHex frame.
     * @param userDataIO
     **************************************************************************/
    public JHexFrame( UserOptionsSerializer<JHexOptions> userDataIO )
    {
        this.userDataIO = userDataIO;

        this.frame = new JFrame();
        this.editor = new HexEditorFilePanel();
        this.dataViewButton = new JToggleButton();
        this.valuePanel = new ValueView();
        this.dataDialog = createDataDialog();

        this.bufferSizeIndex = choices.length - 1;

        editor.setDropTarget( new FileDropTarget(
            new FileDroppedListener( this ) ) );
        editor.addRangeSelectedListener( new SelectionListener( this ) );

        // ---------------------------------------------------------------------
        // Setup frame
        // ---------------------------------------------------------------------
        frame.setJMenuBar( createMenuBar() );

        frame.setContentPane( createContentPane() );

        frame.addWindowListener( new WindowCloseListener() );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setTitle( "JHex" );

        frame.setIconImages( IconConstants.loader.getImages( IconConstants.BINARY_32 ) );
    }

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

        okButton.addActionListener( new HideDialogListener() );

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

    private Container createContentPane()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        StatusBarPanel statusView = new StatusBarPanel();

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( editor, BorderLayout.CENTER );
        panel.add( statusView.getView(), BorderLayout.SOUTH );

        return panel;
    }

    private Component createToolbar()
    {
        JGoodiesToolBar toolbar = new JGoodiesToolBar();
        JButton button = new JButton();

        button = new JButton(
            IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        button.setToolTipText( "Open File" );
        button.setFocusable( false );
        button.addActionListener( new OpenListener() );
        toolbar.add( button );

        button = new JButton(
            IconConstants.loader.getIcon( IconConstants.SAVE_16 ) );
        button.setToolTipText( "Save File (Not Yet Implemented)" );
        button.setFocusable( false );
        button.addActionListener( new SaveListener() );
        // toolbar.add( button );

        toolbar.addSeparator();

        button = new JButton(
            JHexIconConstants.loader.getIcon( JHexIconConstants.JUMP_LEFT ) );
        button.setToolTipText( "Previous Data Block" );
        button.setFocusable( false );
        button.addActionListener( new BackListener() );
        toolbar.add( button );

        button = new JButton(
            JHexIconConstants.loader.getIcon( JHexIconConstants.INCH_LEFT ) );
        button.setToolTipText( "Previous Data" );
        button.setFocusable( false );
        button.addActionListener( new BackListener() );
        // toolbar.add( button );
        //
        button = new JButton(
            JHexIconConstants.loader.getIcon( JHexIconConstants.INCH_RIGHT ) );
        button.setToolTipText( "Next Data" );
        button.setFocusable( false );
        button.addActionListener( new NextListener() );
        // toolbar.add( button );

        button = new JButton(
            JHexIconConstants.loader.getIcon( JHexIconConstants.JUMP_RIGHT ) );
        button.setToolTipText( "Next Data Block" );
        button.setFocusable( false );
        button.addActionListener( new NextListener() );
        toolbar.add( button );

        toolbar.addSeparator();

        button = new JButton(
            IconConstants.loader.getIcon( IconConstants.FIND_16 ) );
        button.setToolTipText( "Find Bytes" );
        button.setFocusable( false );
        button.addActionListener( new FindListener() );
        toolbar.add( button );

        button = new JButton(
            JHexIconConstants.loader.getIcon( JHexIconConstants.GOTO ) );
        button.setToolTipText( "Go To Byte" );
        button.setFocusable( false );
        button.addActionListener( new GoToListener() );
        toolbar.add( button );

        button = new JButton(
            IconConstants.loader.getIcon( IconConstants.CONFIG_16 ) );
        button.setToolTipText( "Configure Options" );
        button.setFocusable( false );
        button.addActionListener( new BufferSizeListener() );
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
     * @return
     **************************************************************************/
    private JMenuBar createMenuBar()
    {
        ActionListener openListener = new OpenListener();
        ActionListener saveListener = new SaveListener();
        ActionListener gotoListener = new GoToListener();
        ActionListener findListener = new FindListener();

        JMenuBar menubar = new JGoodiesMenuBar();
        JMenu fileMenu = new JMenu( "File" );
        JMenuItem openMenuItem = new JMenuItem( "Open" );
        JMenuItem saveMenuItem = new JMenuItem( "Save" );
        JMenuItem exitMenuItem = new JMenuItem( "Exit" );

        JMenu searchMenu = new JMenu( "Search" );
        JMenuItem gotoMenuItem = new JMenuItem( "Go To Offset" );
        JMenuItem findMenuItem = new JMenuItem( "Find" );
        JMenu toolsMenu = new JMenu( "Tools" );
        JMenuItem bufferSizeMenuItem = new JMenuItem( "Set Buffer Size" );

        // ---------------------------------------------------------------------
        // Setup menu bar
        // ---------------------------------------------------------------------
        openMenuItem.addActionListener( openListener );
        openMenuItem.setIcon( IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 ) );

        saveMenuItem.addActionListener( saveListener );
        saveMenuItem.setIcon( IconConstants.loader.getIcon( IconConstants.SAVE_16 ) );

        exitMenuItem.addActionListener( new ExitListener( frame ) );
        exitMenuItem.setIcon( IconConstants.loader.getIcon( IconConstants.CLOSE_16 ) );

        fileMenu.add( openMenuItem );
        // fileMenu.add( saveMenuItem );
        fileMenu.add( exitMenuItem );

        gotoMenuItem.setIcon( JHexIconConstants.loader.getIcon( JHexIconConstants.GOTO ) );
        gotoMenuItem.addActionListener( gotoListener );
        searchMenu.add( gotoMenuItem );

        findMenuItem.setIcon( IconConstants.loader.getIcon( IconConstants.FIND_16 ) );
        findMenuItem.addActionListener( findListener );
        searchMenu.add( findMenuItem );

        bufferSizeMenuItem.setIcon( IconConstants.loader.getIcon( IconConstants.CONFIG_16 ) );
        bufferSizeMenuItem.addActionListener( new BufferSizeListener() );
        toolsMenu.add( bufferSizeMenuItem );

        menubar.add( fileMenu );
        menubar.add( searchMenu );
        menubar.add( toolsMenu );

        return menubar;
    }

    public JFrame getView()
    {
        return frame;
    }

    /***************************************************************************
     * 
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
     * 
     **************************************************************************/
    private void showOpenDialog()
    {
        JFileChooser chooser = new JFileChooser();
        int choice = JFileChooser.CANCEL_OPTION;
        JHexOptions options = userDataIO.getOptions();

        chooser.setCurrentDirectory( options.lastSavedLocation );
        choice = chooser.showOpenDialog( frame );

        if( choice == JFileChooser.APPROVE_OPTION )
        {
            File f = chooser.getSelectedFile();
            openFile( f );
        }
    }

    /***************************************************************************
     * @param f
     **************************************************************************/
    public void openFile( File f )
    {
        if( !f.isFile() )
        {
            return;
        }

        File dir = f.getAbsoluteFile().getParentFile();
        JHexOptions options = userDataIO.getOptions();

        options.lastSavedLocation = dir;
        userDataIO.write();

        try
        {
            editor.setFile( f );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    private void saveFile()
    {
        JOptionPane.showMessageDialog( frame,
            "This functionality is not yet implemented.",
            "Not Yet Implemented", JOptionPane.INFORMATION_MESSAGE );

        if( "".length() < 1 )
            return;

        JFileChooser chooser = new JFileChooser();
        int choice = JFileChooser.CANCEL_OPTION;
        JHexOptions options = userDataIO.getOptions();

        chooser.setCurrentDirectory( options.lastSavedLocation );
        choice = chooser.showSaveDialog( frame );

        if( choice == JFileChooser.APPROVE_OPTION )
        {
            File f = chooser.getSelectedFile();
            File dir = f.getParentFile();
            options.lastSavedLocation = dir;
            userDataIO.write();
            try
            {
                editor.saveFile( f );
            }
            catch( IOException ex )
            {
                ex.printStackTrace();
            }
        }
    }

    /***************************************************************************
     * @param e
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

            byte[] bytes = new byte[strBytes.length() / 2];

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
                System.out.println( Utils.arrayToString( bytes ) );

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
     * @param e
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
     * Closed the currently opened file.
     **************************************************************************/
    private void closeFile()
    {
        try
        {
            editor.closeFile();
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( frame, ex.getMessage(), "ERROR",
                JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class OpenListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            showOpenDialog();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class SaveListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            saveFile();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class GoToListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            showGotoDialog();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class FindListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            showFindDialog();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class BufferSizeListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            showBufferSizeDialog();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class WindowCloseListener extends WindowAdapter
    {
        @Override
        public void windowClosing( WindowEvent e )
        {
            // System.out.println( "Window Closing" );
            closeFile();
        }

        @Override
        public void windowClosed( WindowEvent e )
        {
            // System.out.println( "Window Closed" );
            closeFile();
        }
    }

    private class FileDroppedListener implements
        ItemActionListener<IFileDropEvent>
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
     * 
     **************************************************************************/
    private class BackListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            editor.jumpPrevious();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class NextListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            editor.jumpForward();
        }
    }

    /***************************************************************************
     * 
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
     * 
     **************************************************************************/
    private class HideDialogListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            dataDialog.setVisible( false );
            dataViewButton.setSelected( false );
            editor.setHighlightLength( -1 );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class SelectionListener implements IRangeSelectedListener
    {
        private final JHexFrame frame;

        public SelectionListener( JHexFrame frame )
        {
            this.frame = frame;
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
     * 
     **************************************************************************/
    private class SizeSelectedListener implements ItemActionListener<Integer>
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
