package org.jutils.chart.ui;

import java.awt.Component;

import javax.swing.JPanel;

import org.jutils.chart.model.Series;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SeriesView implements IDataView<Series>
{
    /**  */
    private final JPanel view;
    /**  */
    private final TextLabelField titleField;

    /**  */
    private Series series;

    /***************************************************************************
     * 
     **************************************************************************/
    public SeriesView()
    {
        this.titleField = new TextLabelField( "Title" );

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();

        form.setHorizontalStretch( true );

        form.addField( titleField );

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

        titleField.setValue( data.title );
    }
}
