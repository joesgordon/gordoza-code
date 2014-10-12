package org.jutils.chart.ui;

import java.awt.Component;
import java.io.File;

import org.jutils.chart.data.SaveOptions;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.updater.ItemActionUpdater;
import org.jutils.ui.event.updater.ReflectiveUpdater;
import org.jutils.ui.fields.FileField;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.model.IDataView;
import org.jutils.ui.validators.FileValidator.ExistenceType;

public class SaveView implements IDataView<SaveOptions>
{
    /**  */
    private final FileField outputField;
    /**  */
    private final IntegerFormField widthField;
    /**  */
    private final IntegerFormField heightField;

    /**  */
    private SaveOptions options;

    public SaveView()
    {
        this.outputField = new FileField( ExistenceType.DO_NOT_CHECK, true,
            true );
        this.widthField = new IntegerFormField( "Width", "px" );
        this.heightField = new IntegerFormField( "Height", "px" );

        setData( new SaveOptions() );

        outputField.addChangeListener( new ItemActionUpdater<>(
            new ReflectiveUpdater<File>( this, "options.file" ) ) );
        widthField.setUpdater( new ReflectiveUpdater<Integer>( this,
            "options.size.width" ) );
        heightField.setUpdater( new ReflectiveUpdater<Integer>( this,
            "options.size.height" ) );
    }

    @Override
    public Component getView()
    {
        StandardFormView form = new StandardFormView();

        form.setHorizontalStretch( true );

        form.addField( "File", outputField.getView() );
        form.addField( widthField );
        form.addField( heightField );

        return form.getView();
    }

    @Override
    public SaveOptions getData()
    {
        return options;
    }

    @Override
    public void setData( SaveOptions data )
    {
        this.options = data;

        outputField.setData( data.file );
        widthField.setValue( data.size.width );
        heightField.setValue( data.size.height );
    }
}
