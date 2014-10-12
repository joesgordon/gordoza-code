package org.jutils.ui.event;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.SwingUtilities;

import org.jutils.ui.DirectoryChooser;

/*******************************************************************************
 * Generic {@link ActionListener} for prompting a user for a directory.
 ******************************************************************************/
public class DirectoryChooserListener implements ActionListener
{
    /** The parent component of the dialog to be displayed. */
    private final Component parent;
    /** The title of the dialog to be displayed. */
    private final String title;
    /** The listener called when the directory is selected. */
    private final IFileSelectionListener dirListener;

    /***************************************************************************
     * Creates a new listener with the values:
     * @param parent the parent component of the dialog to be displayed.
     * @param title the title of the dialog to be displayed.
     * @param dirListener the listener called when the directory is selected.
     **************************************************************************/
    public DirectoryChooserListener( Component parent, String title,
        IFileSelectionListener dirListener )
    {
        this.parent = parent;
        this.title = title;
        this.dirListener = dirListener;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void actionPerformed( ActionEvent e )
    {
        File lastFile = dirListener.getDefaultFile();
        DirectoryChooser chooser = new DirectoryChooser(
            ( Frame )SwingUtilities.getWindowAncestor( parent ) );

        chooser.setTitle( title );

        if( lastFile != null )
        {
            chooser.setSelectedPaths( lastFile.getAbsolutePath() );
        }

        chooser.setSize( 400, 500 );
        chooser.setVisible( true );

        File [] selected = chooser.getSelected();

        if( selected != null )
        {
            boolean eachIsDir = true;

            for( File file : selected )
            {
                if( !file.isDirectory() )
                {
                    eachIsDir = false;
                    break;
                }
            }

            if( eachIsDir )
            {
                dirListener.filesChosen( selected );
            }
        }
    }
}
