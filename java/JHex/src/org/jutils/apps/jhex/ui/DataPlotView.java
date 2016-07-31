package org.jutils.apps.jhex.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.chart.data.XYPoint;
import org.jutils.chart.model.IDataPoint;
import org.jutils.chart.model.ISeriesData;
import org.jutils.chart.ui.ChartView;
import org.jutils.io.ByteCache;
import org.jutils.io.IStream;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataPlotView implements IView<JComponent>
{
    /**  */
    private final IStream stream;
    /**  */
    private final ChartView chart;
    /**  */
    private final JPanel view;
    /**  */
    private final ByteCache cache;

    /***************************************************************************
     * 
     **************************************************************************/
    public DataPlotView( IStream stream )
    {
        this.stream = stream;
        this.chart = new ChartView();
        this.view = createView();
        this.cache = new ByteCache( 4096 );

        chart.chart.title.visible = false;
        chart.chart.domainAxis.title.visible = true;
        chart.chart.domainAxis.title.text = "Sample Index";
        chart.chart.rangeAxis.title.visible = true;
        chart.chart.rangeAxis.title.text = "Value";

        load( 0L );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( chart.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();
        Icon icon;
        Action action;

        icon = IconConstants.getIcon( IconConstants.BACK_16 );
        action = new ActionAdapter( ( e ) -> loadPrevious(), "Previous", icon );
        SwingUtils.addActionToToolbar( toolbar, action );

        icon = IconConstants.getIcon( IconConstants.FORWARD_16 );
        action = new ActionAdapter( ( e ) -> loadNext(), "Next", icon );
        SwingUtils.addActionToToolbar( toolbar, action );

        return toolbar;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void loadPrevious()
    {
        long pos = cache.getPosition() - cache.getSize();

        if( pos > -1 )
        {
            load( pos );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void loadNext()
    {
        long pos = cache.getPosition() + cache.getSize();

        try
        {
            if( pos < stream.getLength() )
            {
                load( pos );
            }
        }
        catch( IOException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

    /**
     * @param position
     */
    private void load( long position )
    {
        try
        {
            cache.readFromStream( stream, position );

            byte [] data = new byte[cache.remainingWrite()];

            cache.read( data, 0, data.length );

            DataSeries series = new DataSeries( data );

            chart.addSeries( series, "Data", "", false );
        }
        catch( IOException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class DataSeries implements ISeriesData<IDataPoint>
    {
        private byte [] data;

        public DataSeries( byte [] data )
        {
            this.data = data;
        }

        @Override
        public Iterator<IDataPoint> iterator()
        {
            return new SeriesIterator( this );
        }

        @Override
        public int getCount()
        {
            return data.length;
        }

        @Override
        public double getX( int index )
        {
            return index;
        }

        @Override
        public double getY( int index )
        {
            return data[index];
        }

        @Override
        public IDataPoint get( int index )
        {
            return new XYPoint( index, getY( index ) );
        }

        @Override
        public boolean isHidden( int index )
        {
            return false;
        }

        @Override
        public void setHidden( int index, boolean hidden )
        {
        }

        @Override
        public boolean isSelected( int index )
        {
            return false;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SeriesIterator implements Iterator<IDataPoint>
    {
        private int index;
        private final ISeriesData<IDataPoint> series;

        public SeriesIterator( ISeriesData<IDataPoint> series )
        {
            this.series = series;
            this.index = 0;
        }

        @Override
        public boolean hasNext()
        {
            return index < series.getCount();
        }

        @Override
        public IDataPoint next()
        {
            try
            {
                return series.get( index++ );
            }
            catch( ArrayIndexOutOfBoundsException ex )
            {
                throw new NoSuchElementException( ex.getMessage() );
            }
        }
    }
}
