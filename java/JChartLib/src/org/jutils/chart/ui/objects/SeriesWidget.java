package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.chart.ChartUtils;
import org.jutils.chart.data.*;
import org.jutils.chart.data.ChartContext.IDimensionCoords;
import org.jutils.chart.model.*;
import org.jutils.chart.ui.IChartWidget;
import org.jutils.io.LogUtils;

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
        this.highlight = new CircleBorderMarker();
        this.line = new SimpleLine();

        this.marker.setColor( series.marker.color );
        this.highlight.setColor( series.highlight.color );
        this.line.setColor( series.line.color );
        this.line.setSize( series.line.weight );

        this.trackPoint = true;

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
        Point lastmp = new Point();
        Point lastlp = new Point();
        XYPoint xy;

        // LogUtils.printDebug( "series: w: " + width + ", h: " + height );

        Span spanx = series.isPrimaryDomain ? context.primaryDomainSpan
            : context.secondaryDomainSpan;
        // Span spany = series.isPrimaryRange ? context.primaryRangeSpan
        // : context.secondaryRangeSpan;

        IDimensionCoords domain = series.isPrimaryDomain ? context.domain.primary
            : context.domain.secondary;
        IDimensionCoords range = series.isPrimaryRange ? context.range.primary
            : context.range.secondary;

        int start = ChartUtils.findNearest( series.data, spanx.min ) - 2;
        int end = ChartUtils.findNearest( series.data, spanx.max ) + 2;

        start = Math.max( start, 0 );
        end = Math.min( end, series.data.getCount() - 1 );

        LogUtils.printDebug( "series: start: " + start + ", end: " + end );

        for( int i = start; i < end; i++ )
        {
            xy = series.data.get( i );

            p.x = domain.fromCoord( xy.x );
            p.y = range.fromCoord( xy.y );

            if( line != null && i > 0 )
            {
                if( !p.equals( lastlp ) )
                {
                    line.setPoints( lastlp, p );

                    line.draw( graphics, 0, 0, width, height );

                    lastlp.x = p.x;
                    lastlp.y = p.y;
                }
            }

            if( marker != null && ( i == 0 || ( i > 0 && !p.equals( lastmp ) ) ) )
            {
                marker.setLocation( p );

                marker.draw( graphics, 0, 0, width, height );

                lastmp.x = p.x;
                lastmp.y = p.y;
            }

            if( i == 0 )
            {
                lastmp.x = p.x;
                lastmp.y = p.y;

                lastlp.x = p.x;
                lastlp.y = p.y;
            }
        }

        if( marker != null )
        {
            marker.setLocation( lastmp );

            marker.draw( graphics, 0, 0, width, height );

            lastmp.x = p.x;
            lastmp.y = p.y;
        }
    }

    @Override
    public Dimension calculateSize()
    {
        return null;
    }
}
