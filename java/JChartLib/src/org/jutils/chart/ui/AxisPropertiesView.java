package org.jutils.chart.ui;

import java.awt.Component;

import javax.swing.JCheckBox;

import org.jutils.chart.model.Axis;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.updater.CheckBoxUpdater;
import org.jutils.ui.event.updater.ReflectiveUpdater;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AxisPropertiesView implements IDataView<Axis>
{
    /**  */
    private final StandardFormView form;

    /**  */
    private final TextLabelField titleField;
    /**  */
    private final TextLabelField subtitleField;
    /**  */
    private final IntegerFormField majorSectionField;
    /**  */
    private final IntegerFormField minorSectionField;
    /**  */
    private final JCheckBox dockZeroField;

    /**  */
    private Axis axis;

    /***************************************************************************
     * 
     **************************************************************************/
    public AxisPropertiesView()
    {
        this.titleField = new TextLabelField( "Title" );
        this.subtitleField = new TextLabelField( "Subtitle" );
        this.majorSectionField = new IntegerFormField( "Major Section Count",
            1, 20 );
        this.minorSectionField = new IntegerFormField( "Minor Section Count",
            1, 4 );
        this.dockZeroField = new JCheckBox();

        this.form = createView();

        setData( new Axis() );

        majorSectionField.setUpdater( new ReflectiveUpdater<Integer>( this,
            "axis.majorSectionCount" ) );
        minorSectionField.setUpdater( new ReflectiveUpdater<Integer>( this,
            "axis.minorSectionCount" ) );
        dockZeroField.addActionListener( new CheckBoxUpdater(
            new ReflectiveUpdater<Boolean>( this, "axis.dockZero" ) ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private StandardFormView createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( titleField );
        form.addField( subtitleField );
        form.addField( majorSectionField );
        form.addField( minorSectionField );
        form.addField( "Dock Zero", dockZeroField );

        return form;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Axis getData()
    {
        return axis;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Axis data )
    {
        this.axis = data;

        titleField.setValue( data.title );
        subtitleField.setValue( data.subtitle );
        majorSectionField.setValue( data.majorSectionCount );
        minorSectionField.setValue( data.minorSectionCount );
        dockZeroField.setSelected( data.dockZero );
    }
}
