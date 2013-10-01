package org.jutils.chart.ui.objects;

import java.awt.Graphics2D;
import java.awt.Point;

import org.jutils.chart.*;
import org.jutils.chart.ui.IJava2dObject;

public class Series implements IJava2dObject
{
    public final ISeries data;
    public IMarker marker;
    public IJava2dObject line;

    public ChartContext context;

    public Series( ISeries data )
    {
        this.data = data;

        this.marker = new CircleMarker();

        marker.setBorderVisible( false );
    }

    @Override
    public void paint( Graphics2D graphics, int width, int height )
    {
        double vx;
        double vy;

        Point p;

        // System.out.println( "w: " + width + ", h: " + height );

        for( int i = 0; i < data.getCount(); i++ )
        {
            vx = data.getX( i );
            vy = data.getY( i );

            p = ChartUtils.valueToChartCoords( vx, vy, width, height, context );

            // p.x = ( int )( vx * 1.0 / width );
            // p.y = ( int )( vy * 1.0 / width );

            // System.out.println( "[" + i + "] = " + vx + ", " + vy );
            // System.out.println( "[" + i + "] = " + p.x + ", " + p.y );

            marker.setX( p.x );
            marker.setY( p.y );

            marker.paint( graphics, width, height );
        }

        marker.setX( width / 2 );
        marker.setY( height / 2 );

        // marker.paint( graphics, width, height );
    }
}
