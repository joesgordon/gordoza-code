package org.jutils.chart.ui;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.jutils.chart.data.DefaultSeries;
import org.jutils.chart.data.XYPoint;
import org.jutils.chart.model.Series;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.TitleView;
import org.jutils.ui.event.updater.CheckBoxUpdater;
import org.jutils.ui.event.updater.ReflectiveUpdater;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SeriesView implements IDataView<Series>
{
    /**  */
    private final JPanel view;
    /**  */
    private final StringFormField titleField;
    /**  */
    private final JCheckBox visibleField;
    /**  */
    private final JCheckBox primaryDomainField;
    /**  */
    private final JCheckBox primaryRangeField;
    /**  */
    private final MarkerStyleView markerView;
    /**  */
    private final MarkerStyleView highlightView;
    /**  */
    private final LineStyleView lineView;

    /**  */
    private Series series;

    /***************************************************************************
     * 
     **************************************************************************/
    public SeriesView()
    {
        this.titleField = new StringFormField( "Title" );
        this.visibleField = new JCheckBox();
        this.primaryDomainField = new JCheckBox();
        this.primaryRangeField = new JCheckBox();
        this.markerView = new MarkerStyleView();
        this.highlightView = new MarkerStyleView();
        this.lineView = new LineStyleView();

        this.view = createView();

        setData( new Series( new DefaultSeries( new ArrayList<XYPoint>() ) ) );

        titleField.setUpdater(
            new ReflectiveUpdater<String>( this, "series.name" ) );

        visibleField.addActionListener( new CheckBoxUpdater(
            new ReflectiveUpdater<Boolean>( this, "series.visible" ) ) );

        primaryDomainField.addActionListener(
            new CheckBoxUpdater( new ReflectiveUpdater<Boolean>( this,
                "series.isPrimaryDomain" ) ) );

        primaryRangeField.addActionListener( new CheckBoxUpdater(
            new ReflectiveUpdater<Boolean>( this, "series.isPrimaryRange" ) ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( titleField );
        form.addField( "Visible", visibleField );
        form.addField( "Is Primary Domain", primaryDomainField );
        form.addField( "Is Primary Range", primaryRangeField );
        form.addField( null,
            new TitleView( "Marker", markerView.getView() ).getView() );
        form.addField( null,
            new TitleView( "Highlight", highlightView.getView() ).getView() );
        form.addField( null,
            new TitleView( "Line", lineView.getView() ).getView() );

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
    public Series getData()
    {
        return series;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Series data )
    {
        this.series = data;

        titleField.setValue( data.name );
        visibleField.setSelected( data.visible );
        primaryDomainField.setSelected( data.isPrimaryDomain );
        primaryRangeField.setSelected( data.isPrimaryRange );
        markerView.setData( data.marker );
        highlightView.setData( data.highlight );
        lineView.setData( data.line );
    }
}
