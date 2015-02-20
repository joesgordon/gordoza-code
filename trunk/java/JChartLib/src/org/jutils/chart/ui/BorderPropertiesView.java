package org.jutils.chart.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;

import org.jutils.chart.model.WidgetBorder;
import org.jutils.ui.ColorButtonView;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.updater.CheckBoxUpdater;
import org.jutils.ui.event.updater.ReflectiveUpdater;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.model.IDataView;
import org.jutils.ui.validation.UpdaterItemListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BorderPropertiesView implements IDataView<WidgetBorder>
{
    /**  */
    private final StandardFormView form;

    /**  */
    private final JCheckBox visibleField;
    /**  */
    private final ColorButtonView colorField;
    /**  */
    private final IntegerFormField thicknessField;

    /**  */
    private WidgetBorder border;

    /***************************************************************************
     * 
     **************************************************************************/
    public BorderPropertiesView()
    {
        this.visibleField = new JCheckBox();
        this.colorField = new ColorButtonView();
        this.thicknessField = new IntegerFormField( "Thickness" );

        this.form = createView();

        setData( new WidgetBorder() );

        visibleField.addActionListener( new CheckBoxUpdater(
            new ReflectiveUpdater<Boolean>( this, "border.visible" ) ) );
        colorField.addUpdateListener( new UpdaterItemListener<Color>(
            new ReflectiveUpdater<Color>( this, "border.color" ) ) );
        thicknessField.setUpdater( new ReflectiveUpdater<Integer>( this,
            "border.thickness" ) );

    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private StandardFormView createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( "Visible", visibleField );
        form.addField( "Color", colorField.getView() );
        form.addField( thicknessField );

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
    public WidgetBorder getData()
    {
        return border;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( WidgetBorder border )
    {
        this.border = border;

        visibleField.setSelected( border.visible );
        colorField.setData( border.color );
        thicknessField.setValue( border.thickness );
    }
}
