package org.jutils.ui.fields;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.io.parsers.ExistenceType;
import org.jutils.io.parsers.FileParser;
import org.jutils.ui.*;
import org.jutils.ui.event.*;
import org.jutils.ui.event.FileChooserListener.IFileSelected;
import org.jutils.ui.event.FileDropTarget.JTextFieldFilesListener;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.model.IDataView;
import org.jutils.ui.validation.*;
import org.jutils.ui.validators.DataTextValidator;
import org.jutils.ui.validators.ITextValidator;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FileField implements IDataView<File>, IValidationField
{
    /**  */
    private final JPanel view;
    /**  */
    private final IconTextField textField;
    /**  */
    private final ValidationTextComponentField<JTextField> field;
    /**  */
    private final JButton button;
    /**  */
    private final ItemActionList<File> changeListeners;
    /**  */
    private final FileChooserListener fileListener;
    /**  */
    private final FileIcon icon;

    /***************************************************************************
     * Creates a File view according to the parameters provided:
     * @param existence type of existence to be checked: file/dir/either/none.
     * @param required if the path can be empty or is required.
     * @param isSave if the path is to be be save to (alt. read from).
     * @param showButton denotes whether the browse button should be shown.
     **************************************************************************/
    public FileField( ExistenceType existence, boolean required, boolean isSave,
        boolean showButton )
    {
        this.changeListeners = new ItemActionList<>();

        this.field = new ValidationTextComponentField<>( new JTextField() );
        this.textField = new IconTextField( field.getView() );
        this.button = new JButton();
        this.icon = new FileIcon();

        this.fileListener = createFileListener( existence, isSave );
        this.view = createView( existence, required, showButton );

        textField.setIcon( icon );

        // textField.setIcon( icon );

        field.getView().setColumns( 20 );

        field.setText( "" );

        field.addValidityChanged( ( v ) -> {
            if( !v.isValid )
            {
                icon.setDefaultIcon();
            }
        } );
    }

    /***************************************************************************
     * @param existence
     * @param isSave
     * @return
     **************************************************************************/
    private FileChooserListener createFileListener( ExistenceType existence,
        boolean isSave )
    {
        FileChooserListener fcl = null;

        if( existence != ExistenceType.DIRECTORY_ONLY )
        {
            IFileSelected ifs = ( f ) -> setData( f );
            fcl = new FileChooserListener( field.getView(), "Choose File",
                isSave, ifs, () -> getDefaultFile() );
        }

        return fcl;
    }

    /***************************************************************************
     * @param existence type of existence to be checked: file/dir/either/none.
     * @param required if the path can be empty or is required.s
     * @param isSave if the path is to be be save to (alt. read from).
     * @param showButton denotes whether the browse button should be shown.
     * @return
     **************************************************************************/
    private JPanel createView( ExistenceType existence, boolean required,
        boolean showButton )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        ActionListener browseListener;

        if( existence == ExistenceType.DIRECTORY_ONLY )
        {
            IFileSelected ifs = ( f ) -> setData( f );
            browseListener = new DirectoryChooserListener( panel,
                "Choose Directory", ifs, () -> getDefaultFile() );
        }
        else
        {
            browseListener = fileListener;
        }

        ITextValidator validator = new DataTextValidator<File>(
            new FileParser( existence, required ), new FileUpdater( this ) );

        // LogUtils.printDebug( "Setting validator" );
        field.setValidator( validator );
        field.getView().setDropTarget( new FileDropTarget(
            new JTextFieldFilesListener( field.getView(), existence ) ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( textField.getView(), constraints );

        if( showButton )
        {
            button.setIcon(
                IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 ) );
            button.addActionListener( browseListener );
            button.setToolTipText( "Browse (Right-click to open path)" );

            constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 0, 4, 0, 0 ), 0, 0 );
            panel.add( button, constraints );

            button.addMouseListener( new MenuListener( this, panel ) );
        }

        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public File getData()
    {
        File file;
        String path = field.getText();

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
     * {@inheritDoc}
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

        field.setText( text );
        icon.setFile( file );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return field.getValidity();
    }

    /***************************************************************************
     * Sets the field as editable according to the provided boolean.
     * @param editable {@code true} if the user can edit the control;
     * {@code false} otherwise.
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        button.setEnabled( editable );
        field.setEditable( editable );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addChangeListener( ItemActionListener<File> l )
    {
        changeListeners.addListener( l );
    }

    /***************************************************************************
     * Adds the provided extension description and extensions to the file
     * dialog.
     * @param description the description of the file type denoted by the
     * extension list.
     * @param extensions the list of extensions of a file type.
     * @throws IllegalStateException if this field was initialized with
     * {@link ExistenceType#DIRECTORY_ONLY}.
     * @see FileChooserListener#addExtension(String, String...)
     **************************************************************************/
    public void addExtension( String description, String... extensions )
        throws IllegalStateException
    {
        if( fileListener == null )
        {
            throw new IllegalStateException(
                "Cannot add extensions to a chooser that is directory only" );
        }

        fileListener.addExtension( description, extensions );
    }

    /***************************************************************************
     * Returns the last selected file for the listener.
     **************************************************************************/
    private File getDefaultFile()
    {
        File f = getData();

        while( f != null && !f.exists() )
        {
            f = f.getParentFile();
        }

        return f;
    }

    /***************************************************************************
     * Notifies the listeners that the file has been updated.
     **************************************************************************/
    private static class FileUpdater implements IUpdater<File>
    {
        private final FileField view;

        /**
         * @param view
         */
        public FileUpdater( FileField view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void update( File file )
        {
            // LogUtils.printDebug( "File changed to " + file.getAbsolutePath()
            // );
            view.icon.setFile( file );
            view.changeListeners.fireListeners( view, file );
        }
    }

    /***************************************************************************
     * Displays the context menu on the button on right-click.
     **************************************************************************/
    private static class MenuListener extends MouseAdapter
    {
        private final FileField field;
        private final FileContextMenu menu;

        public MenuListener( FileField field, Component parent )
        {
            this.field = field;
            this.menu = new FileContextMenu( parent );
        }

        @Override
        public void mouseClicked( MouseEvent e )
        {
            if( SwingUtilities.isRightMouseButton( e ) &&
                e.getClickCount() == 1 )
            {
                Component c = e.getComponent();
                int x = c.getWidth() / 2; // e.getX();
                int y = c.getHeight() / 2; // e.getY();

                File file = field.getData();

                menu.show( file, c, x, y );
            }
        }
    }
}
