package org.jutils.chart;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartContext
{
    /**  */
    public double xMin;
    /**  */
    public double xMax;
    /**  */
    public double yMin;
    /**  */
    public double yMax;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartContext()
    {
        xMin = -5.0;
        xMax = 5.0;
        yMin = -5.0;
        yMax = 5.0;
    }

    public double getXRange()
    {
        return xMax - xMin;
    }

    public double getYRange()
    {
        return yMax - yMin;
    }
}
