package org.jutils.ui.fields;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.ui.event.*;
import org.jutils.ui.event.FileDropTarget.JTextFieldFilesListener;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.model.IDataView;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validators.*;
import org.jutils.ui.validators.FileValidator.ExistenceType;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FileField implements IDataView<File>, IValidationField
{
    /**  */
    private final JPanel view;
    /**  */
    private final ValidationTextField fileField;
    /**  */
    private final ItemActionList<File> changeListeners;

    /***************************************************************************
     * Creates a File view with an {@link ExistenceType} of FILE_ONLY, required,
     * and opens files.
     **************************************************************************/
    public FileField()
    {
        this( ExistenceType.FILE_ONLY, true, false );
    }

    /***************************************************************************
     * Creates a File view with the provided {@link ExistenceType}, required,
     * and opens file if the existence is {@link ExistenceType#FILE_ONLY}.
     * @param existence
     **************************************************************************/
    public FileField( ExistenceType existence )
    {
        this( existence, true, existence != ExistenceType.FILE_ONLY );
    }

    /***************************************************************************
     * Creates a File view with the provided {@link ExistenceType} and required,
     * and saves files.
     * @param existence
     * @param required
     **************************************************************************/
    public FileField( ExistenceType existence, boolean required )
    {
        this( existence, required, true );
    }

    /***************************************************************************
     * Creates a File view with the provided {@link ExistenceType}, required,
     * and save boolean.
     * @param existence
     * @param required
     * @param isSave
     **************************************************************************/
    public FileField( ExistenceType existence, boolean required,
        boolean isSave )
    {
        this.changeListeners = new ItemActionList<>();

        this.fileField = new ValidationTextField();
        this.view = createView( existence, required, isSave );

        fileField.getView().setColumns( 20 );

        fileField.setText( "" );
    }

    /***************************************************************************
     * @param isSave
     * @param existence
     * @return
     **************************************************************************/
    private JPanel createView( ExistenceType existence, boolean required,
        boolean isSave )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        JButton button;
        ActionListener browseListener;

        if( existence == ExistenceType.DIRECTORY_ONLY )
        {
            browseListener = new DirectoryChooserListener( panel,
                "Choose Directory", new FileBrowseListener( this ) );
        }
        else
        {
            browseListener = new FileChooserListener( panel, "Choose File",
                new FileBrowseListener( this ), isSave );
        }

        ITextValidator validator = new DataTextValidator<File>(
            new FileValidator( existence, required ), new FileUpdater( this ) );

        // LogUtils.printDebug( "Setting validator" );
        fileField.setValidator( validator );
        fileField.getView().setDropTarget( new FileDropTarget(
            new JTextFieldFilesListener( fileField.getView(), existence ) ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( fileField.getView(), constraints );

        button = new JButton(
            IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        button.addActionListener( browseListener );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 0, 4, 0, 0 ), 0, 0 );
        panel.add( button, constraints );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public File getData()
    {
        File file;
        String path = fileField.getText();

        if( path.isEmpty() )
        {
            file = null;
        }
        else
        {
            file = new File( path ).getAbsoluteFile();
        }

        return file;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( File file )
    {
        String text = "";

        if( file != null )
        {
            text = file.getAbsolutePath();
        }

        // LogUtils.printDebug( "Setting data to: \"" + text + "\"" );

        fileField.setText( text );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        fileField.addValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        fileField.removeValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isValid()
    {
        return fileField.isValid();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getInvalidationReason()
    {
        return fileField.getInvalidationReason();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValidBackground( Color bg )
    {
        fileField.setValidBackground( bg );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setInvalidBackground( Color bg )
    {
        fileField.setInvalidBackground( bg );
    }

    /***************************************************************************
     * @param b
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        fileField.setEditable( editable );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addChangeListener( ItemActionListener<File> l )
    {
        changeListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileBrowseListener implements IFileSelectionListener
    {
        private final FileField view;

        public FileBrowseListener( FileField view )
        {
            this.view = view;
        }

        @Override
        public File getDefaultFile()
        {
            File f = view.getData();

            while( f != null && !f.exists() )
            {
                f = f.getParentFile();
            }

            return f;
        }

        @Override
        public void filesChosen( File [] files )
        {
            view.setData( files[0] );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileUpdater implements IUpdater<File>
    {
        private final FileField view;

        public FileUpdater( FileField view )
        {
            this.view = view;
        }

        @Override
        public void update( File file )
        {
            // LogUtils.printDebug( "File changed to " + file.getAbsolutePath()
            // );
            view.changeListeners.fireListeners( view, file );
        }
    }
}
