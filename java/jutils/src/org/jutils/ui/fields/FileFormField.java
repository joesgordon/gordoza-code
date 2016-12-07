package org.jutils.ui.fields;

import java.io.File;

import javax.swing.JComponent;

import org.jutils.io.parsers.ExistenceType;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.event.updater.ItemActionUpdater;
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
     * @param name
     * @param existence
     * @param required
     * @param isSave
     **************************************************************************/
    public FileFormField( String name, ExistenceType existence,
        boolean required, boolean isSave )
    {
        this.name = name;
        this.field = new FileField( existence, required, isSave );
        this.view = new ValidationView( field );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public File getValue()
    {
        return field.getData();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( File value )
    {
        field.setData( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<File> updater )
    {
        this.updater = updater;

        field.addChangeListener( new ItemActionUpdater<>( updater ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<File> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        field.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.addValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.removeValidityChanged( l );
    }

    /***************************************************************************
     * 
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
