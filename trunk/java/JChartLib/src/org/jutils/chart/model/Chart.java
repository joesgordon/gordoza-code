package org.jutils.chart.model;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Chart
{
    /**  */
    public boolean antialias;
    /**  */
    public boolean textAntiAlias;
    /**  */
    public boolean gridlinesVisible;
    /**  */
    public Integer height;
    /**  */
    public Integer width;
    /**  */
    public TextLabel title;
    /**  */
    public TextLabel topBottomLabel;

    // public List<Series> series;
    /**  */
    public final Axis domainAxis;
    /**  */
    public final Axis rangeAxis;

    public Chart()
    {
        gridlinesVisible = true;
        domainAxis = new Axis();
        rangeAxis = new Axis();
    }
}
