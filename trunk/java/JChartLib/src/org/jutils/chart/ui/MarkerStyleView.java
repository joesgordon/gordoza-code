package org.jutils.chart.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.*;

import org.jutils.chart.model.MarkerStyle;
import org.jutils.chart.model.MarkerType;
import org.jutils.ui.ColorButtonView;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.updater.*;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.model.IDataView;

public class MarkerStyleView implements IDataView<MarkerStyle>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JCheckBox visibleField;
    /**  */
    private final JComboBox<MarkerType> shapeField;
    /**  */
    private final IntegerFormField weightField;
    /**  */
    private final ColorButtonView colorField;

    /**  */
    private MarkerStyle marker;

    /***************************************************************************
     * 
     **************************************************************************/
    public MarkerStyleView()
    {
        this.visibleField = new JCheckBox();
        this.shapeField = new JComboBox<>( MarkerType.values() );
        this.weightField = new IntegerFormField( "Size", 1, 20 );
        this.colorField = new ColorButtonView();

        this.view = createView();

        setData( new MarkerStyle() );

        visibleField.addActionListener( new CheckBoxUpdater(
            new ReflectiveUpdater<Boolean>( this, "marker.visible" ) ) );
        shapeField.addItemListener( new ComboBoxUpdater<>(
            new ReflectiveUpdater<>( this, "marker.type" ) ) );
        weightField.setUpdater( new ReflectiveUpdater<Integer>( this,
            "marker.weight" ) );
        colorField.addUpdateListener( new ItemActionUpdater<>(
            new ReflectiveUpdater<Color>( this, "marker.color" ) ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( "Visible", visibleField );
        form.addField( "Shape", shapeField );
        form.addField( weightField );
        form.addField( "Color", colorField.getView() );

        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public MarkerStyle getData()
    {
        return marker;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( MarkerStyle data )
    {
        this.marker = data;

        visibleField.setSelected( data.visible );
        shapeField.setSelectedItem( data.type );
        weightField.setValue( data.weight );
        colorField.setData( data.color );
    }
}
