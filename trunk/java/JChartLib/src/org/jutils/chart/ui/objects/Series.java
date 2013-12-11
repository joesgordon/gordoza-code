package org.jutils.chart.ui.objects;

import java.awt.Graphics2D;
import java.awt.Point;

import org.jutils.chart.data.*;
import org.jutils.chart.model.*;
import org.jutils.chart.ui.IChadget;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Series implements IChadget
{
    /**  */
    public final ISeriesData data;

    /**  */
    public IMarker marker;
    /**  */
    public IMarker highlightMarker;
    /**  */
    public ILine line;

    /**  */
    public ChartContext context;
    /**  */
    public String name;

    /***************************************************************************
     * @param data
     **************************************************************************/
    public Series( ISeriesData data )
    {
        this.data = data;

        this.marker = new CircleMarker();
        this.highlightMarker = new CircleMarker();
        this.line = new SimpleLine();

        highlightMarker.setRadius( 10 );

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

        // System.out.println( "w: " + width + ", h: " + height );

        for( int i = 0; i < data.getCount(); i++ )
        {
            xy = data.get( i );

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
