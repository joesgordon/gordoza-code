package org.jutils.chart.ui;

import java.awt.Component;

import javax.swing.JCheckBox;

import org.jutils.chart.model.Axis;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.updater.*;
import org.jutils.ui.fields.DoubleFormField;
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
    private final JCheckBox autoTicksField;
    /**  */
    private final DoubleFormField tickStartField;
    /**  */
    private final DoubleFormField tickEndField;
    /**  */
    private final DoubleFormField tickWidthField;
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
        this.autoTicksField = new JCheckBox();
        this.tickStartField = new DoubleFormField( "Tick Start" );
        this.tickEndField = new DoubleFormField( "Tick End" );
        this.tickWidthField = new DoubleFormField( "Tick Width" );
        this.dockZeroField = new JCheckBox();

        this.form = createView();

        setData( new Axis() );

        autoTicksField.addActionListener(
            new CheckBoxUpdater( new AutoTicksUpdater( this,
                new ReflectiveUpdater<Boolean>( this, "axis.autoTicks" ) ) ) );
        tickStartField.setUpdater(
            new ReflectiveUpdater<Double>( this, "axis.tickStart" ) );
        tickEndField.setUpdater(
            new ReflectiveUpdater<Double>( this, "axis.tickEnd" ) );
        tickWidthField.setUpdater(
            new ReflectiveUpdater<Double>( this, "axis.tickWidth" ) );
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
        form.addField( "Auto Ticks", autoTicksField );
        form.addField( tickStartField );
        form.addField( tickEndField );
        form.addField( tickWidthField );
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
        autoTicksField.setSelected( data.autoTicks );
        tickStartField.setValue( data.tickStart );
        tickEndField.setValue( data.tickEnd );
        tickWidthField.setValue( data.tickWidth );
        dockZeroField.setSelected( data.dockZero );

        setAutoTicksEnabled( data.autoTicks );
    }

    /***************************************************************************
     * @param autoEnabled
     **************************************************************************/
    public void setAutoTicksEnabled( Boolean autoEnabled )
    {
        tickStartField.setEditable( !autoEnabled );
        tickEndField.setEditable( !autoEnabled );
        tickWidthField.setEditable( !autoEnabled );
        dockZeroField.setEnabled( autoEnabled );
        dockZeroField.setSelected( autoEnabled && dockZeroField.isSelected() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class AutoTicksUpdater implements IUpdater<Boolean>
    {
        private final AxisPropertiesView view;
        private final IUpdater<Boolean> updater;

        public AutoTicksUpdater( AxisPropertiesView view,
            IUpdater<Boolean> updater )
        {
            this.view = view;
            this.updater = updater;
        }

        @Override
        public void update( Boolean autoEnabled )
        {
            view.setAutoTicksEnabled( autoEnabled );

            updater.update( autoEnabled );
        }
    }
}
