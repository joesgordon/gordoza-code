package org.jutils.ui.event;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTextField;

/*******************************************************************************
 * Class be added to a {@link JComponent} when the user drags a file onto the
 * component.
 ******************************************************************************/
public class FileDropTarget extends DropTarget
{
    /** List of listeners to be called when the file is dropped. */
    private final ItemActionListener<IFileDropEvent> droppedListener;

    /***************************************************************************
     * @param droppedListener
     **************************************************************************/
    public FileDropTarget( ItemActionListener<IFileDropEvent> droppedListener )
    {
        this.droppedListener = droppedListener;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public synchronized void drop( DropTargetDropEvent evt )
    {
        try
        {
            evt.acceptDrop( DnDConstants.ACTION_COPY );
            @SuppressWarnings( "unchecked")
            List<File> droppedFiles = ( List<File> )evt.getTransferable().getTransferData(
                DataFlavor.javaFileListFlavor );

            droppedListener.actionPerformed( new ItemActionEvent<IFileDropEvent>(
                this, new DefaultFileDropEvent( evt, droppedFiles ) ) );
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }
    }

    public static interface IFileDropEvent
    {
        public DropTargetDropEvent getEvent();

        public List<File> getFiles();
    }

    private static class DefaultFileDropEvent implements IFileDropEvent
    {
        private final DropTargetDropEvent event;
        private final List<File> files;

        public DefaultFileDropEvent( DropTargetDropEvent event, List<File> files )
        {
            this.event = event;
            this.files = files;
        }

        @Override
        public DropTargetDropEvent getEvent()
        {
            return event;
        }

        @Override
        public List<File> getFiles()
        {
            return files;
        }
    }

    /***************************************************************************
     * Generic implementation of an {@link ItemActionListener} for use in a
     * {@link FileDropTarget}
     **************************************************************************/
    public static class JTextFieldFilesListener implements
        ItemActionListener<IFileDropEvent>
    {
        private final JTextField field;

        public JTextFieldFilesListener( JTextField field )
        {
            this.field = field;
        }

        @Override
        public void actionPerformed( ItemActionEvent<IFileDropEvent> event )
        {
            List<File> files = event.getItem().getFiles();
            StringBuilder paths = new StringBuilder();

            for( int i = 0; i < files.size(); i++ )
            {
                if( i > 0 )
                {
                    paths.append( File.pathSeparator );
                }

                paths.append( files.get( i ).getAbsolutePath() );
            }

            field.setText( paths.toString() );
        }
    }
}
