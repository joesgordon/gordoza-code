package org.jutils.ui;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

import org.jutils.*;
import org.jutils.io.IOUtils;
import org.jutils.io.StringPrintStream;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.utils.IGetter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FileContextMenu
{
    /**  */
    private final Component parent;
    /**  */
    private final FileIcon icon;
    /**  */
    private final JPopupMenu menu;
    /**  */
    private final JMenuItem openPathMenuItem;
    /**  */
    private final JMenuItem openParentMenuItem;
    /**  */
    private final JMenuItem copyPathMenuItem;
    /**  */
    private final JMenuItem copyNameMenuItem;
    /**  */
    private final JMenuItem copyParentPathMenuItem;
    /**  */
    private final JMenuItem copyParentNameMenuItem;
    /**  */
    private final JMenuItem infoMenuItem;

    /**  */
    private File file;

    /***************************************************************************
     * @param parent
     **************************************************************************/
    public FileContextMenu( Component parent )
    {
        this.parent = parent;
        this.icon = new FileIcon();
        this.openPathMenuItem = new JMenuItem();
        this.openParentMenuItem = new JMenuItem();
        this.copyPathMenuItem = new JMenuItem();
        this.copyNameMenuItem = new JMenuItem();
        this.copyParentPathMenuItem = new JMenuItem();
        this.copyParentNameMenuItem = new JMenuItem();
        this.infoMenuItem = new JMenuItem();
        this.menu = createMenu();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPopupMenu createMenu()
    {
        JPopupMenu menu = new JPopupMenu();
        ActionListener l;
        Action a;

        l = ( e ) -> openPath();
        a = createAction( l, "Open Path", IconConstants.OPEN_FILE_16 );
        openPathMenuItem.setAction( a );
        menu.add( openPathMenuItem );

        l = ( e ) -> openParent();
        a = createAction( l, "Open Parent", IconConstants.OPEN_FOLDER_16 );
        openParentMenuItem.setAction( a );
        menu.add( openParentMenuItem );

        menu.addSeparator();

        l = ( e ) -> copyString( () -> file.getAbsolutePath() );
        a = createAction( l, "Copy Path", IconConstants.EDIT_COPY_16 );
        copyPathMenuItem.setAction( a );
        menu.add( copyPathMenuItem );

        l = ( e ) -> copyString( () -> file.getName() );
        a = createAction( l, "Copy Name", IconConstants.EDIT_COPY_16 );
        copyNameMenuItem.setAction( a );
        menu.add( copyNameMenuItem );

        menu.addSeparator();

        l = ( e ) -> copyString( () -> file.getParent() );
        a = createAction( l, "Copy Parent Path", IconConstants.EDIT_COPY_16 );
        copyParentPathMenuItem.setAction( a );
        menu.add( copyParentPathMenuItem );

        l = ( e ) -> copyString( () -> file.getParentFile().getName() );
        a = createAction( l, "Copy Parent Name", IconConstants.EDIT_COPY_16 );
        copyParentNameMenuItem.setAction( a );
        menu.add( copyParentNameMenuItem );

        menu.addSeparator();

        l = ( e ) -> showInfo();
        a = createAction( l, "File Info", IconConstants.CONFIG_16 );
        infoMenuItem.setAction( a );
        menu.add( infoMenuItem );

        return menu;
    }

    /***************************************************************************
     * @param file
     * @param y
     * @param x
     * @param c
     * @return
     * @see JPopupMenu#show(Component, int, int)
     **************************************************************************/
    public void show( File file, Component c, int x, int y )
    {
        this.file = file;

        File parent = file == null ? null : file.getParentFile();

        icon.setFile( file );

        openParentMenuItem.setIcon( icon );

        openPathMenuItem.setEnabled( file != null && file.exists() );
        openParentMenuItem.setEnabled(
            file != null && parent != null && parent.exists() );

        copyPathMenuItem.setEnabled( file != null );
        copyNameMenuItem.setEnabled( file != null );

        copyParentPathMenuItem.setEnabled( file != null );
        copyParentNameMenuItem.setEnabled( file != null && parent != null );

        infoMenuItem.setEnabled( file != null );

        menu.show( c, x, y );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void copyString( IGetter<String> strGetter )
    {
        String str = strGetter.get();
        if( str != null )
        {
            Utils.setClipboardText( str );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showInfo()
    {
        VerboseMessageView msgView = new VerboseMessageView();
        SimpleDateFormat fmt = new SimpleDateFormat(
            "MM/dd/yyyy HH:mm:ss.SSS" );

        try( StringPrintStream msg = new StringPrintStream() )
        {
            msg.println( "  Can Execute: %s", file.canRead() );
            msg.println( "     Can Read: %s", file.canRead() );
            msg.println( "    Can Write: %s", file.canRead() );
            msg.println( "    Is Hidden: %s", file.isHidden() );
            msg.println( "       Exists: %s", file.exists() );
            msg.println( " Is Directory: %s", file.isDirectory() );
            msg.println( "      Is File: %s", file.isFile() );
            msg.println( "  File Length: %s",
                IOUtils.byteCount( file.length() ) );
            msg.println( "Last Modified: %s",
                fmt.format( new Date( file.lastModified() ) ) );

            msg.println();

            msg.println( "--- Volume Info ---" );
            msg.println( "   Free Space: %s",
                IOUtils.byteCount( file.getFreeSpace() ) );
            msg.println( "  Total Space: %s",
                IOUtils.byteCount( file.getTotalSpace() ) );
            msg.println( " Usable Space: %s",
                IOUtils.byteCount( file.getUsableSpace() ) );

            msgView.setMessages( file.getName(), msg.toString() );
        }

        msgView.show( parent, "File Info", 350, 400 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void openPath()
    {
        if( file != null )
        {
            openPath( file );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void openParent()
    {
        if( file != null )
        {
            openPath( file.getParentFile() );
        }
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void openPath( File file )
    {
        if( !file.exists() )
        {
            String [] choices = new String[] { "Open Parent", "Cancel" };
            String choice = SwingUtils.showConfirmMessage( parent,
                "File does not exist. Open existing parent?",
                "File does not exist", choices, choices[0] );

            if( choices[0].equals( choice ) )
            {
                File parent = IOUtils.getExistingDir( file.getAbsolutePath() );

                if( parent == null )
                {
                    JOptionPane.showMessageDialog( this.parent,
                        "No parent exists for file:" + Utils.NEW_LINE +
                            file.getAbsolutePath(),
                        "Error Opening File", JOptionPane.ERROR_MESSAGE );
                    return;
                }

                file = parent;
            }
            else
            {
                return;
            }
        }

        try
        {
            Desktop.getDesktop().open( file );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( parent,
                "Could not open file externally:" + Utils.NEW_LINE +
                    file.getAbsolutePath(),
                "Error Opening File", JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static Action createAction( ActionListener l, String name,
        String iconStr )
    {
        Icon icon = IconConstants.getIcon( iconStr );

        return new ActionAdapter( l, name, icon );
    }
}
