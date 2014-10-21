package org.jutils.chart.ui;

import java.awt.Component;

import javax.swing.*;

import org.jutils.chart.model.LineStyle;
import org.jutils.chart.model.LineType;
import org.jutils.ui.ColorButtonView;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.model.IDataView;

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
    private LineStyle marker;

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
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();

        form.setHorizontalStretch( true );

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
        return marker;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( LineStyle data )
    {
        this.marker = data;

        visibleField.setSelected( data.visible );
        shapeField.setSelectedItem( data.type );
        weightField.setValue( data.weight );
        colorField.setData( data.color );
    }
}
