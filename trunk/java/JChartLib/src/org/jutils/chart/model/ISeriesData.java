package org.jutils.chart.model;

import org.jutils.chart.data.XYPoint;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface ISeriesData extends Iterable<XYPoint>
{
    /***************************************************************************
     * @return
     **************************************************************************/
    public int getCount();

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    public double getX( int index );

    /***************************************************************************
     * @return
     **************************************************************************/
    public XYPoint getMin();

    /***************************************************************************
     * @return
     **************************************************************************/
    public XYPoint getMax();

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    public double getY( int index );

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    public XYPoint get( int index );

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    public boolean isHidden( int index );
}
