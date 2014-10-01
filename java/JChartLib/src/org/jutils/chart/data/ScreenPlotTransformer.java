package org.jutils.chart.data;

import java.awt.Point;

/*******************************************************************************
 *
 ******************************************************************************/
public class ScreenPlotTransformer
{
    /**  */
    private final ChartContext context;
    /**  */
    private final double xScale;
    /**  */
    private final double yScale;

    /***************************************************************************
     * @param context
     **************************************************************************/
    public ScreenPlotTransformer( ChartContext context )
    {
        this.context = context;
        this.xScale = context.width / context.getXRange();
        this.yScale = context.height / context.getYRange();
    }

    /***************************************************************************
     * @param xy
     * @param p
     **************************************************************************/
    public void fromChart( XYPoint xy, Point p )
    {
        p.x = ( int )( ( xy.x - context.xMin ) * xScale );
        p.y = ( int )( context.height - ( xy.y - context.yMin ) * yScale );
    }

    /***************************************************************************
     * @param p
     * @param xy
     **************************************************************************/
    public void fromScreen( Point p, XYPoint xy )
    {
        xy.x = fromScreenDomain( p.x );
        xy.y = fromScreenRange( p.y );
    }

    /***************************************************************************
     * @param i
     * @return
     **************************************************************************/
    public double fromScreenDomain( int i )
    {
        return i / xScale + context.xMin;
    }

    /***************************************************************************
     * @param i
     * @return
     **************************************************************************/
    public double fromScreenRange( int i )
    {
        return -1 * i / yScale + context.yMin;
    }
}
