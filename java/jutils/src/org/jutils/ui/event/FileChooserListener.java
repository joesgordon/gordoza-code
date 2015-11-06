package org.jutils.ui.event;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/*******************************************************************************
 * Generic {@link ActionListener} for prompting a user for a file. If file
 * extensions are added, the file returned via the listener is ensured to have
 * the file extension selected.
 ******************************************************************************/
public class FileChooserListener implements ActionListener
{
    /** The parent component of the dialog to be displayed. */
    private final Component parent;
    /** The listener called when the file is selected. */
    private final IFileSelectionListener fileListener;
    /**
     * Indicates that a save dialog will be shown if {@code true}, an open
     * dialog will be shown otherwise.
     */
    private final boolean isSave;
    /**  */
    private final boolean selectMultiple;

    /** The file chooser to be used to generate a dialog. */
    private final JFileChooser chooser;

    /**  */
    private File lastDir;

    /***************************************************************************
     * Creates a listener able to select a single file with the following
     * parameters:
     * @param parent the parent component of the dialog to be displayed.
     * @param title the title of the dialog to be displayed.
     * @param fileListener the listener called when the directory is selected.
     * @param defaultFilename the default filename to be used.
     * @param isSave a save dialog will be shown if {@code true}, an open dialog
     * will be shown otherwise.
     **************************************************************************/
    public FileChooserListener( Component parent, String title,
        IFileSelectionListener fileListener, boolean isSave )
    {
        this( parent, title, fileListener, isSave, false );
    }

    /***************************************************************************
     * Creates a listener with the following parameters:
     * @param parent the parent component of the dialog to be displayed.
     * @param title the title of the dialog to be displayed.
     * @param fileListener the listener called when the directory is selected.
     * @param defaultFilename the default filename to be used.
     * @param isSave a save dialog will be shown if {@code true}, an open dialog
     * will be shown otherwise.
     * @param selectMultiple option to select multiple files (only valid if
     * {@code isSave} is {@code false}.
     **************************************************************************/
    public FileChooserListener( Component parent, String title,
        IFileSelectionListener fileListener, boolean isSave,
        boolean selectMultiple )
    {
        this.parent = parent;
        this.fileListener = fileListener;
        this.isSave = isSave;
        this.selectMultiple = selectMultiple;

        if( selectMultiple && isSave )
        {
            throw new IllegalArgumentException(
                "Cannot select multiple if saving." );
        }

        this.chooser = new JFileChooser();
        chooser.setDialogTitle( title );
        chooser.setMultiSelectionEnabled( false );
        chooser.setAcceptAllFileFilterUsed( false );
    }

    /***************************************************************************
     * Adds an extension to the dialog list of file types.
     * @param description the description of files with the extension
     * @param extensions the extension to be added sans '.'
     **************************************************************************/
    public void addExtension( String description, String... extensions )
    {
        StringBuilder desc = new StringBuilder();

        desc.append( description );
        desc.append( " " );

        for( int i = 0; i < extensions.length; i++ )
        {
            if( i > 0 )
            {
                desc.append( ", " );
            }
            desc.append( "(*." );
            desc.append( extensions[i] );
            desc.append( ')' );
        }

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            desc.toString(), extensions );
        chooser.addChoosableFileFilter( filter );
    }

    public void setAcceptAllFileFilterUsed( boolean used )
    {
        chooser.setAcceptAllFileFilterUsed( used );
    }

    /***************************************************************************
     * Shows a {@link JFileChooser} in response to an action.
     * @param event ignored.
     * @see ActionListener#actionPerformed(ActionEvent)
     **************************************************************************/
    @Override
    public void actionPerformed( ActionEvent event )
    {
        File defaultFile = fileListener.getDefaultFile();
        int choice;
        File [] selected = null;
        FileFilter filter;

        chooser.setSelectedFile( new File( "" ) );

        if( chooser.getChoosableFileFilters().length > 0 )
        {
            chooser.setFileFilter( chooser.getChoosableFileFilters()[0] );
        }

        if( !isSave )
        {
            chooser.setMultiSelectionEnabled( selectMultiple );
        }

        chooser.setFileSelectionMode( JFileChooser.FILES_ONLY );

        if( defaultFile != null )
        {
            chooser.setSelectedFile( defaultFile );
        }
        else
        {
            chooser.setCurrentDirectory( lastDir );
        }

        if( isSave )
        {
            choice = chooser.showSaveDialog( parent );
        }
        else
        {
            choice = chooser.showOpenDialog( parent );
        }

        lastDir = chooser.getCurrentDirectory();

        if( choice == JFileChooser.APPROVE_OPTION )
        {
            if( selectMultiple )
            {
                selected = chooser.getSelectedFiles();
            }
            else
            {
                File f = chooser.getSelectedFile();
                if( f != null )
                {
                    selected = new File[] { f };
                }
            }

            for( int f = 0; f < selected.length; f++ )
            {
                selected[f] = selected[f].getAbsoluteFile();
            }

            if( selected != null && selected.length > 0 )
            {
                if( isSave )
                {
                    filter = chooser.getFileFilter();

                    if( filter != null &&
                        filter instanceof FileNameExtensionFilter )
                    {
                        FileNameExtensionFilter fnef = ( FileNameExtensionFilter )filter;
                        for( int i = 0; i < selected.length; i++ )
                        {
                            selected[i] = ensureExtension( selected[i], fnef );
                        }
                    }
                }

                fileListener.filesChosen( selected );
            }
        }
    }

    /***************************************************************************
     * Ensures that the extension specified by the file filter is suffixed onto
     * the file name of the given file.
     * @return a file with the given extension.
     **************************************************************************/
    private static File ensureExtension( File selected,
        FileNameExtensionFilter fileFilter )
    {
        File file = selected;
        String extension = fileFilter.getExtensions()[0];
        String filename = file.getName();
        int dotIndex = filename.length() - extension.length() - 1;

        if( !( filename.endsWith( extension ) &&
            filename.charAt( dotIndex ) == '.' ) )
        {
            file = new File( file.getParentFile(), filename + "." + extension );
        }

        return file;
    }

    /***************************************************************************
     * Removes all file extensions.
     **************************************************************************/
    public void removeAllExtensions()
    {
        FileFilter [] filters = chooser.getChoosableFileFilters();

        for( FileFilter f : filters )
        {
            chooser.removeChoosableFileFilter( f );
        }
    }
}
