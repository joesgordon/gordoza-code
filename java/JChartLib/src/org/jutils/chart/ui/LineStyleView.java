package org.jutils.chart.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.*;

import org.jutils.chart.model.LineStyle;
import org.jutils.chart.model.LineType;
import org.jutils.ui.ColorButtonView;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.updater.*;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.model.IDataView;

/***************************************************************************
 * 
 **************************************************************************/
public class LineStyleView implements IDataView<LineStyle>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JCheckBox visibleField;
    /**  */
    private final JComboBox<LineType> shapeField;
    /**  */
    private final IntegerFormField weightField;
    /**  */
    private final ColorButtonView colorField;

    /**  */
    private LineStyle line;

    /***************************************************************************
     * 
     **************************************************************************/
    public LineStyleView()
    {
        this.visibleField = new JCheckBox();
        this.shapeField = new JComboBox<>( LineType.values() );
        this.weightField = new IntegerFormField( "Weight", 1, 10 );
        this.colorField = new ColorButtonView();

        this.view = createView();

        setData( new LineStyle() );

        visibleField.addActionListener( new CheckBoxUpdater(
            new ReflectiveUpdater<Boolean>( this, "line.visible" ) ) );
        shapeField.addItemListener( new ComboBoxUpdater<>(
            new ReflectiveUpdater<>( this, "line.type" ) ) );
        weightField.setUpdater(
            new ReflectiveUpdater<Integer>( this, "line.weight" ) );
        colorField.addUpdateListener( new ItemActionUpdater<>(
            new ReflectiveUpdater<Color>( this, "line.color" ) ) );
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
    public LineStyle getData()
    {
        return line;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( LineStyle data )
    {
        this.line = data;

        visibleField.setSelected( data.visible );
        shapeField.setSelectedItem( data.type );
        weightField.setValue( data.weight );
        colorField.setData( data.color );
    }
}
