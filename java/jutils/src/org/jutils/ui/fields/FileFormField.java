package org.jutils.ui.fields;

import java.io.File;

import javax.swing.JComponent;

import org.jutils.io.parsers.ExistenceType;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FileFormField implements IDataFormField<File>
{
    /**  */
    private final String name;
    /**  */
    private final ValidationView view;
    /**  */
    private final FileField field;

    /**  */
    private IUpdater<File> updater;
    /**  */
    private boolean settingData;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public FileFormField( String name )
    {
        this( name, ExistenceType.FILE_ONLY, true, false );
    }

    /***************************************************************************
     * @param name
     * @param existence
     **************************************************************************/
    public FileFormField( String name, ExistenceType existence )
    {
        this( name, existence, true, existence != ExistenceType.FILE_ONLY );
    }

    /***************************************************************************
     * @param name
     * @param existence
     * @param required
     **************************************************************************/
    public FileFormField( String name, ExistenceType existence,
        boolean required )
    {
        this( name, existence, required, true );
    }

    /***************************************************************************
     * Creates a new field with the provided parameters:
     * @param name the name of the field.
     * @param existence type of existence to be checked.
     * @param required if the path can be empty or is required.
     * @param isSave if the path is to be be save to (alt. read from).
     * @see FileField#FileField(ExistenceType, boolean, boolean)
     **************************************************************************/
    public FileFormField( String name, ExistenceType existence,
        boolean required, boolean isSave )
    {
        this.name = name;
        this.field = new FileField( existence, required, isSave );
        this.view = new ValidationView( field );
        this.settingData = false;
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void callUpdater( File file )
    {
        if( !settingData && updater != null )
        {
            updater.update( file );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public File getValue()
    {
        return field.getData();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( File value )
    {
        this.settingData = true;
        field.setData( value );
        this.settingData = false;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<File> updater )
    {
        this.updater = updater;

        field.addChangeListener( ( e ) -> callUpdater( e.getItem() ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<File> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        field.setEditable( editable );
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
     * @param description
     * @param extensions
     **************************************************************************/
    public void addExtension( String description, String... extensions )
    {
        field.addExtension( description, extensions );
    }
}
