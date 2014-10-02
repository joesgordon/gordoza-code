package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.chart.data.ChartContext;
import org.jutils.chart.data.XYPoint;
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
    public final CircleBorderMarker highlight;
    /**  */
    public final ILine line;

    /**  */
    public ChartContext context;

    /***************************************************************************
     * @param data
     **************************************************************************/
    public SeriesWidget( Chart chart, Series series )
    {
        this.chart = chart;
        this.series = series;

        this.marker = new CircleMarker();
        this.highlight = new CircleBorderMarker();
        this.line = new SimpleLine();

        this.marker.setColor( series.marker.color );
        this.highlight.setColor( series.highlight.color );
        this.line.setColor( series.line.color );
        this.line.setSize( series.line.weight );

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

        for( int i = 0; i < series.data.getCount(); i++ )
        {
            xy = series.data.get( i );

            p.x = context.domain.primary.fromCoord( xy.x );
            p.y = context.range.primary.fromCoord( xy.y );

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
