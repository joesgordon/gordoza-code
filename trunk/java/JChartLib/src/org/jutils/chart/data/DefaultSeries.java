package org.jutils.chart.data;

import java.util.*;

import org.jutils.chart.model.ISeriesData;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DefaultSeries implements ISeriesData
{
    /**  */
    private final List<XYPoint> points;
    /**  */
    private final XYPoint min;
    /**  */
    private final XYPoint max;

    /***************************************************************************
     * @param points
     **************************************************************************/
    public DefaultSeries( List<XYPoint> points )
    {
        this.points = new ArrayList<XYPoint>( points );
        this.min = new XYPoint( 0.0, 0.0 );
        this.max = new XYPoint( 1.0, 1.0 );

        boolean minxSet = false;
        boolean maxxSet = false;
        boolean minySet = false;
        boolean maxySet = false;

        for( XYPoint p : points )
        {
            if( !Double.isNaN( p.x ) )
            {
                if( minxSet )
                {
                    min.x = Math.min( min.x, p.x );
                }
                else
                {
                    min.x = p.x;
                    minxSet = true;
                }

                if( maxxSet )
                {
                    max.x = Math.max( max.x, p.x );
                }
                else
                {
                    max.x = p.x;
                    maxxSet = true;
                }
            }

            if( !Double.isNaN( p.y ) )
            {
                if( minySet )
                {
                    min.y = Math.min( min.y, p.y );
                }
                else
                {
                    min.y = p.y;
                    minySet = true;
                }

                if( maxySet )
                {
                    max.y = Math.max( max.y, p.y );
                }
                else
                {
                    max.y = p.y;
                    maxySet = true;
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getCount()
    {
        return points.size();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public double getX( int index )
    {
        return points.get( index ).x;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public double getY( int index )
    {
        return points.get( index ).y;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Iterator<XYPoint> iterator()
    {
        return points.iterator();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public XYPoint get( int index )
    {
        return points.get( index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public XYPoint getMin()
    {
        return min;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public XYPoint getMax()
    {
        return max;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isHidden( int index )
    {
        // TODO Auto-generated method stub
        return false;
    }
}
