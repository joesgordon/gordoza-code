package org.jutils.chart.ui.objects;

import java.awt.Graphics2D;
import java.awt.Point;

import org.jutils.chart.*;
import org.jutils.chart.ui.IJava2dObject;

public class Series implements IJava2dObject
{
    public final ISeries data;
    public IMarker marker;
    public IMarker highlightMarker;
    public IJava2dObject line;

    public ChartContext context;

    public Series( ISeries data )
    {
        this.data = data;

        this.marker = new CircleMarker();
        this.highlightMarker = new CircleMarker();

        marker.setBorderVisible( false );
    }

    @Override
    public void paint( Graphics2D graphics, int width, int height )
    {
        double vx;
        double vy;

        Point p = new Point();

        // System.out.println( "w: " + width + ", h: " + height );

        for( int i = 0; i < data.getCount(); i++ )
        {
            vx = data.getX( i );
            vy = data.getY( i );

            ChartUtils.valueToChartCoords( vx, vy, width, height, context, p );

            // p.x = ( int )( vx * 1.0 / width );
            // p.y = ( int )( vy * 1.0 / width );

            // System.out.println( "value[" + i + "] = " + vx + ", " + vy );
            // System.out.println( "location[" + i + "] = " + p.x + ", " + p.y
            // );

            marker.setLocation( p );

            marker.paint( graphics, width, height );
        }
    }
}
