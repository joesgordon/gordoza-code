package org.jutils.chart.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jutils.chart.model.*;
import org.jutils.ui.TitleView;
import org.jutils.ui.model.IDataView;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PropertiesView implements IView<JPanel>
{
    /**  */
    private static final String DEFAULT_TITLE = "<- Select a category to the left";
    /**  */
    private static final int FIRST_SERIES_INDEX = 7;

    /**  */
    private final JPanel view;

    /**  */
    private final DefaultListModel<IListNode<?>> navModel;
    /**  */
    private final JList<IListNode<?>> list;

    /**  */
    private final TitleView rightView;

    /**  */
    private final ChartPropertiesView chartPropView;
    /**  */
    private final LegendPropertiesView legendPropView;
    /**  */
    private final AxisPropertiesView axisPropView;

    /**  */
    private final List<SeriesPropertiesView> seriesViews;

    /**  */
    private Chart chart;

    /***************************************************************************
     * 
     **************************************************************************/
    public PropertiesView( Chart chart )
    {
        this.chart = chart;

        this.navModel = new DefaultListModel<>();
        this.list = new JList<>( navModel );
        this.rightView = new TitleView();

        this.chartPropView = new ChartPropertiesView();
        this.legendPropView = new LegendPropertiesView();
        this.axisPropView = new AxisPropertiesView();

        this.view = createView();

        this.seriesViews = new ArrayList<>();

        createNavList();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel view = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        JScrollPane listPane = new JScrollPane( list );

        list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        list.addListSelectionListener( new NavSelected( this ) );

        listPane.setPreferredSize( new Dimension( 200, 200 ) );
        listPane.setMinimumSize( new Dimension( 200, 200 ) );

        rightView.setTitle( DEFAULT_TITLE );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        view.add( listPane, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        view.add( rightView.getView(), constraints );

        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void createNavList()
    {
        ListNode<?> node;

        node = new ListNode<Chart>( "Chart", chart, chartPropView );
        navModel.addElement( node );

        node = new ListNode<Legend>( "Legend", chart.legend, legendPropView );
        navModel.addElement( node );

        node = new ListNode<Axis>( "Domain", chart.domainAxis, axisPropView );
        navModel.addElement( node );

        node = new ListNode<Axis>( "Range", chart.rangeAxis, axisPropView );
        navModel.addElement( node );

        node = new ListNode<Axis>( "Secondary Domain", chart.secDomainAxis,
            axisPropView );
        navModel.addElement( node );

        node = new ListNode<Axis>( "Secondary Range", chart.secRangeAxis,
            axisPropView );
        navModel.addElement( node );

        node = new ListNode<String>( "Series", "", null );
        navModel.addElement( node );
    }

    /***************************************************************************
     * @param series
     **************************************************************************/
    public void addSeries( Series series )
    {
        addSeries( series, seriesViews.size() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void removeAllSeries()
    {
        seriesViews.clear();

        while( navModel.size() > FIRST_SERIES_INDEX )
        {
            navModel.remove( FIRST_SERIES_INDEX );
        }
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
     * 
     **************************************************************************/
    public void clear()
    {
        list.removeAll();
        seriesViews.clear();
    }

    /***************************************************************************
     * @param series
     * @param index
     **************************************************************************/
    public void addSeries( Series series, int index )
    {
        SeriesPropertiesView view = new SeriesPropertiesView();

        view.setData( series );

        IListNode<?> node = new SeriesNode( series, view );
        navModel.addElement( node );

        seriesViews.add( view );
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    public void remove( int index )
    {
        SeriesPropertiesView view = seriesViews.remove( index );

        view.setData( null );

        navModel.remove( index + FIRST_SERIES_INDEX );
    }

    /***************************************************************************
     * @param seriesIdx
     * @param pointIdx
     **************************************************************************/
    public void setSelected( int seriesIdx, int pointIdx )
    {
        SeriesPropertiesView view = seriesViews.get( seriesIdx );

        view.setSelected( pointIdx );
    }

    /***************************************************************************
     * @param T
     **************************************************************************/
    private static interface IListNode<T>
    {
        public IView<?> getView();

        public String toString();
    }

    /***************************************************************************
     * @param T
     **************************************************************************/
    private static class ListNode<T> implements IListNode<T>
    {
        private final String name;
        private final T data;
        private final IDataView<T> view;

        public ListNode( String name, T data, IDataView<T> view )
        {
            this.name = name;
            this.data = data;
            this.view = view;
        }

        public IView<?> getView()
        {
            if( view != null )
            {
                view.setData( data );
            }

            return view;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    private static class SeriesNode implements IListNode<Series>
    {
        private final Series s;
        private final IDataView<Series> view;

        public SeriesNode( Series s, IDataView<Series> view )
        {
            this.s = s;
            this.view = view;
        }

        @Override
        public IView<?> getView()
        {
            return view;
        }

        @Override
        public String toString()
        {
            return "        " + s.name;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class NavSelected implements ListSelectionListener
    {
        private final PropertiesView view;

        public NavSelected( PropertiesView view )
        {
            this.view = view;
        }

        @Override
        public void valueChanged( ListSelectionEvent e )
        {
            if( !e.getValueIsAdjusting() )
            {
                String title = DEFAULT_TITLE;
                Component comp = null;

                if( !view.list.getSelectionModel().isSelectionEmpty() )
                {
                    IListNode<?> node = view.list.getSelectedValue();

                    IView<?> view = node.getView();

                    title = node.toString();

                    if( view != null )
                    {
                        comp = view.getView();
                    }
                }

                this.view.rightView.setTitle( title.trim() );
                this.view.rightView.setComponent( comp );
            }
        }
    }
}
