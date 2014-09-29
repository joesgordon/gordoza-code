package org.jutils.chart.ui.objects;

import java.awt.Graphics2D;
import java.awt.Point;

import org.jutils.chart.data.*;
import org.jutils.chart.model.*;
import org.jutils.chart.ui.IChadget;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SeriesWidget implements IChadget
{
    /**  */
    public final Series series;

    /**  */
    public final IMarker marker;
    /**  */
    public final IMarker highlight;
    /**  */
    public final ILine line;

    /**  */
    public ChartContext context;

    /***************************************************************************
     * @param data
     **************************************************************************/
    public SeriesWidget( Series series )
    {
        this.series = series;

        this.marker = new CircleMarker();
        this.highlight = new CircleMarker();
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
    public void paint( Graphics2D graphics, int width, int height )
    {
        Point p = new Point();
        Point lastmp = new Point();
        Point lastlp = new Point();
        XYPoint xy;

        ScreenPlotTransformer trans = new ScreenPlotTransformer( context );

        // LogUtils.printDebug( "w: " + width + ", h: " + height );

        for( int i = 0; i < series.data.getCount(); i++ )
        {
            xy = series.data.get( i );

            trans.fromChart( xy, p );

            if( line != null && i > 0 )
            {
                if( !p.equals( lastlp ) )
                {
                    line.setPoints( lastlp, p );

                    line.paint( graphics, width, height );

                    lastlp.x = p.x;
                    lastlp.y = p.y;
                }
            }

            if( marker != null && ( i == 0 || ( i > 0 && !p.equals( lastmp ) ) ) )
            {
                marker.setLocation( p );

                marker.paint( graphics, width, height );

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

            marker.paint( graphics, width, height );

            lastmp.x = p.x;
            lastmp.y = p.y;
        }
    }
}
