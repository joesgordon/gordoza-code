package org.jutils.apps.jhex;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.Utils;
import org.jutils.ui.*;
import org.jutils.ui.hex.HexEditorFilePanel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JHexFrame extends UFrame
{
    // ------------------------------------------------------------------------
    // Main panel widgets
    // ------------------------------------------------------------------------
    /** The file tree displaying the directories in the given file system. */
    private HexEditorFilePanel editor;
    /**  */
    private JHexOptions options;
    /**  */
    private int bufferSizeIndex;
    /**  */
    private static final String[] choices = new String[] { "Xtra-Small (1kb)",
        "Small (64kb)", "Medium (512 kb)", "Large (1 Mb)" };
    /**  */
    private static final int[] sizes = new int[] { 0x400, 0x10000, 0x80000,
        0x100000 };

    /***************************************************************************
     * Creates a JHex frame.
     **************************************************************************/
    public JHexFrame()
    {
        editor = new HexEditorFilePanel();
        options = JHexOptions.lazyRead();
        bufferSizeIndex = choices.length - 1;

        this.setJMenuBar( createMenuBar() );

        // ---------------------------------------------------------------------
        // Setup frame
        // ---------------------------------------------------------------------
        this.getContentPane().setLayout( new BorderLayout() );

        this.getContentPane().add( editor, BorderLayout.CENTER );
        this.getContentPane().add( new StatusBarPanel().getView(),
            BorderLayout.SOUTH );

        this.addWindowListener( new WindowCloseListener() );
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setTitle( "JEditor" );

        try
        {
            URL url = IconConstants.getIconUrl( IconConstants.BINARY_32 );
            BufferedImage img16 = ImageIO.read( url );
            // setIconImage( img16 );
            setIconImages( Arrays.asList( new Image[] { img16 } ) );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
        }
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

        JMenuBar menubar = new UMenuBar();
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
        openMenuItem.setIcon( IconConstants.getIcon( IconConstants.OPEN_FILE_16 ) );

        saveMenuItem.addActionListener( saveListener );
        saveMenuItem.setIcon( IconConstants.getIcon( IconConstants.SAVE_16 ) );

        exitMenuItem.addActionListener( new ExitListener() );
        exitMenuItem.setIcon( IconConstants.getIcon( IconConstants.CLOSE_16 ) );

        fileMenu.add( openMenuItem );
        fileMenu.add( saveMenuItem );
        fileMenu.add( exitMenuItem );

        gotoMenuItem.addActionListener( gotoListener );
        searchMenu.add( gotoMenuItem );

        findMenuItem.setIcon( IconConstants.getIcon( IconConstants.FIND_16 ) );
        findMenuItem.addActionListener( findListener );
        searchMenu.add( findMenuItem );

        bufferSizeMenuItem.setIcon( IconConstants.getIcon( IconConstants.CONFIG_16 ) );
        bufferSizeMenuItem.addActionListener( new BufferSizeListener() );
        toolsMenu.add( bufferSizeMenuItem );

        menubar.add( fileMenu );
        menubar.add( searchMenu );
        menubar.add( toolsMenu );

        return menubar;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showBufferSizeDialog()
    {
        Object ans = JOptionPane.showInputDialog( this, "Choose buffer size:",
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
     * @param e
     **************************************************************************/
    public void showOpenDialog()
    {
        JFileChooser chooser = new JFileChooser();
        int choice = JFileChooser.CANCEL_OPTION;

        chooser.setCurrentDirectory( options.getLastSavedLocation() );
        choice = chooser.showOpenDialog( this );

        if( choice == JFileChooser.APPROVE_OPTION )
        {
            File f = chooser.getSelectedFile();
            openFile( f );
        }
    }

    public void openFile( File f )
    {
        if( !f.isFile() )
        {
            return;
        }

        File dir = f.getAbsoluteFile().getParentFile();

        options.setLastSavedLocation( dir );
        options.lazySave();

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
        JOptionPane.showMessageDialog( this,
            "This functionality is not yet implemented.",
            "Not Yet Implemented", JOptionPane.INFORMATION_MESSAGE );

        if( "".length() < 1 )
            return;

        JFileChooser chooser = new JFileChooser();
        int choice = JFileChooser.CANCEL_OPTION;

        chooser.setCurrentDirectory( options.getLastSavedLocation() );
        choice = chooser.showSaveDialog( this );

        if( choice == JFileChooser.APPROVE_OPTION )
        {
            File f = chooser.getSelectedFile();
            File dir = f.getParentFile();
            options.setLastSavedLocation( dir );
            options.lazySave();
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
        Object ans = JOptionPane.showInputDialog( this, "Enter hex string:",
            new Integer( 0 ) );
        if( ans != null )
        {
            String strBytes = ans.toString();
            if( ( strBytes.length() & 0x01 ) != 0 )
            {
                JOptionPane.showMessageDialog( this,
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

                    b = Utils.digitFromHex( strBytes.charAt( i * 2 ) ) & 0x0F;
                    bytes[i] = ( byte )( b << 4 );

                    b = Utils.digitFromHex( strBytes.charAt( i * 2 + 1 ) ) & 0x0F;
                    bytes[i] |= ( byte )b;
                }
                System.out.println( Utils.arrayToString( bytes ) );

                // TODO actually find the bytes.
            }
            catch( NumberFormatException ex )
            {
                JOptionPane.showMessageDialog( this, "'" + ans.toString() +
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
        Object ans = JOptionPane.showInputDialog( this,
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
                JOptionPane.showMessageDialog( this, "'" + ans.toString() +
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
                JOptionPane.showMessageDialog( this, ex.getMessage(), "ERROR",
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
            JOptionPane.showMessageDialog( this, ex.getMessage(), "ERROR",
                JOptionPane.ERROR_MESSAGE );
        }
    }

    private class OpenListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            showOpenDialog();
        }
    }

    private class SaveListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            saveFile();
        }
    }

    private class GoToListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            showGotoDialog();
        }
    }

    private class FindListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            showFindDialog();
        }
    }

    private class BufferSizeListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            showBufferSizeDialog();
        }
    }

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
}
