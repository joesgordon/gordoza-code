package org.jutils.chart.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import org.jutils.chart.data.QuadSide;
import org.jutils.chart.model.Legend;
import org.jutils.ui.*;
import org.jutils.ui.event.updater.*;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LegendPropertiesView implements IDataView<Legend>
{
    /**  */
    private final StandardFormView form;

    /**  */
    private final JCheckBox visibleField;
    /**  */
    private final BorderPropertiesView borderField;
    /**  */
    private final JComboBox<QuadSide> sideField;
    /**  */
    private final ColorButtonView colorField;

    /**  */
    private Legend legend;

    /***************************************************************************
     * 
     **************************************************************************/
    public LegendPropertiesView()
    {
        this.visibleField = new JCheckBox();
        this.borderField = new BorderPropertiesView();
        this.sideField = new JComboBox<>( QuadSide.values() );
        this.colorField = new ColorButtonView();

        this.form = createView();

        setData( new Legend() );

        visibleField.addActionListener( new CheckBoxUpdater(
            new ReflectiveUpdater<Boolean>( this, "legend.visible" ) ) );
        sideField.addItemListener( new ComboBoxUpdater<>(
            new ReflectiveUpdater<>( this, "legend.side" ) ) );
        colorField.addUpdateListener( new ItemActionUpdater<>(
            new ReflectiveUpdater<Color>( this, "legend.fill" ) ) );

    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private StandardFormView createView()
    {
        StandardFormView form = new StandardFormView();
        TitleView bordervView = new TitleView( "Border",
            borderField.getView() );

        form.addField( "Visible", visibleField );
        form.addField( null, bordervView.getView() );
        form.addField( "Side", sideField );
        form.addField( "Color", colorField.getView() );

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
    public Legend getData()
    {
        return legend;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Legend legend )
    {
        this.legend = legend;

        visibleField.setSelected( legend.visible );
        borderField.setData( legend.border );
        sideField.setSelectedItem( legend.side );
        colorField.setData( legend.fill );
    }
}
