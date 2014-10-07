package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.SwingUtils;
import org.jutils.chart.ChartUtils;
import org.jutils.chart.data.*;
import org.jutils.chart.data.ChartContext.IDimensionCoords;
import org.jutils.chart.model.*;
import org.jutils.chart.ui.IChartWidget;

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
    public final IMarker marker;
    /**  */
    public final IMarker selectedMarker;
    /**  */
    public final CircleBorderMarker highlight;
    /**  */
    public final ILine line;

    /**  */
    public final ChartContext context;
    /**  */
    public boolean trackPoint;

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

        marker.setColor( series.marker.color );
        highlight.setColor( series.highlight.color );
        line.setColor( series.line.color );
        line.setSize( series.line.weight );
        selectedMarker.setColor( SwingUtils.inverseColor( series.marker.color ) );

        trackPoint = true;

        highlight.setRadius( 10 );

        marker.setBorderVisible( false );
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, int x, int y, int width, int height )
    {
        Point p = new Point();
        Point last = new Point( -100, -100 );
        XYPoint xy;
        Bounds b = context.getBounds();

        // LogUtils.printDebug( "series: w: " + width + ", h: " + height );

        Span spanx = series.isPrimaryDomain ? b.primaryDomainSpan
            : b.secondaryDomainSpan;
        // Span spany = series.isPrimaryRange ? context.primaryRangeSpan
        // : context.secondaryRangeSpan;

        IDimensionCoords domain = series.isPrimaryDomain ? context.domain.primary
            : context.domain.secondary;
        IDimensionCoords range = series.isPrimaryRange ? context.range.primary
            : context.range.secondary;

        if( spanx == null )
        {
            return;
        }

        int start = ChartUtils.findNearest( series.data, spanx.min ) - 2;
        int end = ChartUtils.findNearest( series.data, spanx.max ) + 2;

        start = Math.max( start, 0 );
        end = Math.min( end, series.data.getCount() );

        // LogUtils.printDebug( "series: start: " + start + ", end: " + end );

        for( int i = start; i < end; i++ )
        {
            xy = series.data.get( i );

            if( xy.hidden )
            {
                continue;
            }

            p.x = domain.fromCoord( xy.x );
            p.y = range.fromCoord( xy.y );

            if( p.x != last.x || p.y != last.y )
            {
                if( series.line.visible && last.x != -100 )
                {
                    line.setPoints( last, p );

                    line.draw( graphics, 0, 0, width, height );

                }

                if( series.marker.visible )
                {
                    IMarker m = xy.selected ? selectedMarker : marker;

                    m.setLocation( p );

                    m.draw( graphics, 0, 0, width, height );
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
    public Dimension calculateSize()
    {
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearSelected()
    {
        for( XYPoint xy : series.data )
        {
            xy.selected = false;
        }
    }

    /***************************************************************************
     * @param domain
     * @param range
     **************************************************************************/
    public void setSelected( Span domain, Span range )
    {
        for( XYPoint xy : series.data )
        {
            if( domain.min <= xy.x && xy.x <= domain.max && range.min <= xy.y &&
                xy.y <= range.max )
            {
                xy.selected = true;
            }
        }
    }
}
