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
            int dropAction = evt.getDropAction();
            DropActionType action = getAction( dropAction );
            Object xferData;
            DefaultFileDropEvent dfde;
            ItemActionEvent<IFileDropEvent> iae;

            evt.acceptDrop( dropAction );

            xferData = evt.getTransferable().getTransferData(
                DataFlavor.javaFileListFlavor );
            @SuppressWarnings( "unchecked")
            List<File> droppedFiles = ( List<File> )xferData;

            dfde = new DefaultFileDropEvent( evt, droppedFiles, action );
            iae = new ItemActionEvent<IFileDropEvent>( this, dfde );

            droppedListener.actionPerformed( iae );
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }
    }

    private static DropActionType getAction( int action )
    {
        switch( action )
        {
            case DnDConstants.ACTION_LINK:
                return DropActionType.LINK;
            case DnDConstants.ACTION_COPY:
                return DropActionType.COPY;
            case DnDConstants.ACTION_MOVE:
                return DropActionType.MOVE;
            default:
                return DropActionType.MOVE;
        }
    }

    public static interface IFileDropEvent
    {
        public DropTargetDropEvent getEvent();

        public List<File> getFiles();

        public DropActionType getActionType();
    }

    public static enum DropActionType
    {
        LINK,
        COPY,
        MOVE;
    }

    private static class DefaultFileDropEvent implements IFileDropEvent
    {
        private final DropTargetDropEvent event;
        private final List<File> files;
        private final DropActionType action;

        public DefaultFileDropEvent( DropTargetDropEvent event,
            List<File> files, DropActionType action )
        {
            this.event = event;
            this.files = files;
            this.action = action;
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

        @Override
        public DropActionType getActionType()
        {
            return action;
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
        private final boolean directoryOnly;

        public JTextFieldFilesListener( JTextField field )
        {
            this( field, false );
        }

        public JTextFieldFilesListener( JTextField field, boolean directoryOnly )
        {
            this.directoryOnly = directoryOnly;
            this.field = field;
        }

        @Override
        public void actionPerformed( ItemActionEvent<IFileDropEvent> event )
        {
            List<File> files = event.getItem().getFiles();
            StringBuilder paths = new StringBuilder();

            for( int i = 0; i < files.size(); i++ )
            {
                File file = files.get( i );

                if( directoryOnly && !file.isDirectory() )
                {
                    continue;
                }

                if( paths.length() > 0 )
                {
                    paths.append( File.pathSeparator );
                }

                paths.append( files.get( i ).getAbsolutePath() );
            }

            field.setText( paths.toString() );
        }
    }
}
