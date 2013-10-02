package org.jutils.chart.ui.objects;

import java.awt.Graphics2D;
import java.awt.Point;

import org.jutils.chart.ChartContext;
import org.jutils.chart.ChartUtils;
import org.jutils.chart.data.*;
import org.jutils.chart.ui.IJava2dObject;

public class Series implements IJava2dObject
{
    public final ISeries data;
    public IMarker marker;
    public IMarker highlightMarker;
    public ILine line;

    public ChartContext context;

    public Series( ISeries data )
    {
        this.data = data;

        this.marker = new CircleMarker();
        this.highlightMarker = new CircleMarker();
        this.line = new SolidLine();

        highlightMarker.setRadius( 10 );

        marker.setBorderVisible( false );
    }

    @Override
    public void paint( Graphics2D graphics, int width, int height )
    {
        Point p = new Point();
        Point lastmp = new Point();
        Point lastlp = new Point();
        XYPoint xy;

        ScreenPlotTransformer trans = new ScreenPlotTransformer( context,
            width, height );

        // System.out.println( "w: " + width + ", h: " + height );

        for( int i = 0; i < data.getCount(); i++ )
        {
            xy = data.get( i );

            trans.fromChart( xy, p );

            // p.x = ( int )( vx * 1.0 / width );
            // p.y = ( int )( vy * 1.0 / width );

            // System.out.println( "value[" + i + "] = " + vx + ", " + vy );
            // System.out.println( "location[" + i + "] = " + p.x + ", " + p.y
            // );

            if( line != null && i > 0 )
            {
                if( ChartUtils.distance( lastlp, p ) > 10 )
                {
                    line.setPoints( lastlp, p );

                    line.paint( graphics, width, height );

                    lastlp.x = p.x;
                    lastlp.y = p.y;
                }
            }

            if( marker != null && i > 0 && !p.equals( lastmp ) )
            {
                marker.setLocation( lastmp );

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
