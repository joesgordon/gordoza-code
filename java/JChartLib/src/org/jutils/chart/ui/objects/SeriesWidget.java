package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.SwingUtils;
import org.jutils.chart.ChartUtils;
import org.jutils.chart.data.ChartContext;
import org.jutils.chart.data.ChartContext.IAxisCoords;
import org.jutils.chart.model.*;
import org.jutils.chart.ui.IChartWidget;
import org.jutils.chart.ui.Layer2d;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SeriesWidget implements IChartWidget
{
    /**  */
    public final Chart chart;
    /**  */
    public final Series series;

    /**  */
    public final CircleMarker marker;
    /**  */
    public final CircleMarker selectedMarker;
    /**  */
    public final CircleBorderMarker highlight;
    /**  */
    public SimpleLine line;

    /**  */
    public final ChartContext context;
    /**  */
    public boolean trackPoint;
    /**  */
    public Point highlightLocation;

    /***************************************************************************
     * @param data
     **************************************************************************/
    public SeriesWidget( Chart chart, Series series, ChartContext context )
    {
        this.chart = chart;
        this.series = series;
        this.context = context;

        this.marker = new CircleMarker();
        this.selectedMarker = new CircleMarker();
        this.highlight = new CircleBorderMarker();
        this.line = new SimpleLine();

        marker.color = series.marker.color;
        highlight.color = series.highlight.color;
        line.color = series.line.color;
        selectedMarker.color = SwingUtils.inverseColor( series.marker.color );

        trackPoint = true;

        highlight.setSize( 10 );
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point location, Dimension size )
    {
        if( !series.visible )
        {
            return;
        }

        marker.color = series.marker.color;
        marker.setSize( series.marker.weight );
        highlight.color = series.highlight.color;
        line.color = series.line.color;
        selectedMarker.color = SwingUtils.inverseColor( series.marker.color );

        Point p = new Point();
        Point last = new Point( -100, -100 );
        IDataPoint dp;

        // LogUtils.printDebug( "series: " + series.name + " weight: " +
        // series.marker.weight );

        IAxisCoords domain = series.isPrimaryDomain ? context.domainCoords
            : context.secDomainCoords;
        IAxisCoords range = series.isPrimaryRange ? context.rangeCoords
            : context.secRangeCoords;

        Interval spanx = domain.getBounds();
        // Span spany = series.isPrimaryRange ? context.primaryRangeSpan
        // : context.secondaryRangeSpan;

        if( spanx == null )
        {
            return;
        }

        int start = ChartUtils.findNearest( series.data, spanx.min ) - 2;
        int end = ChartUtils.findNearest( series.data, spanx.max ) + 2;

        start = Math.max( start, 0 );
        end = Math.min( end, series.data.getCount() );

        // LogUtils.printDebug( "series: start: " + start + ", end: " + end );

        Layer2d markerLayer = new Layer2d();
        Layer2d selectedLayer = new Layer2d();
        int d = series.marker.weight + 2;
        int r = d / 2 + 1;

        if( series.marker.visible )
        {
            Graphics2D g2d;
            Point mp = new Point( r, r );

            g2d = markerLayer.setSize( d, d );
            marker.draw( g2d, mp );

            g2d = selectedLayer.setSize( d, d );
            selectedMarker.draw( g2d, mp );
        }

        for( int i = start; i < end; i++ )
        {
            dp = series.data.get( i );

            if( dp.isHidden() )
            {
                continue;
            }

            p.x = domain.fromCoord( dp.getX() );
            p.y = range.fromCoord( dp.getY() );

            if( p.x != last.x || p.y != last.y )
            {
                if( series.line.visible && last.x != -100 )
                {
                    line.draw( graphics, last, p );
                }

                if( series.marker.visible )
                {
                    Layer2d l2d = dp.isSelected() ? selectedLayer : markerLayer;

                    // m.draw( graphics, p, size );
                    l2d.paint( graphics, p.x - r, p.y - r );
                }

                last.x = p.x;
                last.y = p.y;
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension calculateSize( Dimension canvasSize )
    {
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearSelected()
    {
        for( IDataPoint xy : series.data )
        {
            xy.setSelected( false );
        }
    }

    /***************************************************************************
     * @param domain
     * @param range
     **************************************************************************/
    public void setSelected( Interval domain, Interval range )
    {
        for( IDataPoint xy : series.data )
        {
            if( domain.min <= xy.getX() && xy.getX() <= domain.max &&
                range.min <= xy.getY() && xy.getY() <= range.max )
            {
                xy.setSelected( true );
            }
        }
    }

    public void setHighlightLocation( Point point )
    {
        this.highlightLocation = point;
    }
}
