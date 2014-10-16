package org.jutils.chart.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jutils.chart.model.Series;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataView implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JTabbedPane seriesTabs;
    /**  */
    private final List<DataSeriesView> seriesViews;

    /***************************************************************************
     * 
     **************************************************************************/
    public DataView()
    {
        this.seriesTabs = new JTabbedPane();
        this.view = createView();
        this.seriesViews = new ArrayList<>();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel view = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 4,
                4, 4, 4 ), 0, 0 );
        view.add( seriesTabs, constraints );

        return view;
    }

    /***************************************************************************
     * @param series
     **************************************************************************/
    public void addSeries( Series series )
    {
        addSeries( series, seriesTabs.getTabCount() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void removeAllSeries()
    {
        seriesTabs.removeAll();
        seriesViews.clear();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * @param series
     * @param index
     **************************************************************************/
    public void addSeries( Series series, int index )
    {
        DataSeriesView view = new DataSeriesView();

        seriesViews.add( view );

        view.setData( series );

        if( index > seriesTabs.getTabCount() )
        {
            index = seriesTabs.getTabCount();
        }

        seriesTabs.insertTab( series.title.text, null, view.getView(), null,
            index );
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    public void remove( int index )
    {
        seriesTabs.removeTabAt( index );
        DataSeriesView view = seriesViews.remove( index );

        view.setData( null );
    }

    /***************************************************************************
     * @param seriesIdx
     * @param pointIdx
     **************************************************************************/
    public void setSelected( int seriesIdx, int pointIdx )
    {
        DataSeriesView view = seriesViews.get( seriesIdx );

        view.setSelected( pointIdx );
    }
}
