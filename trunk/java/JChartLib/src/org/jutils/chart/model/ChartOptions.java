package org.jutils.chart.model;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartOptions
{
    /**  */
    public boolean antialias;
    /**  */
    public boolean textAntiAlias;
    /**  */
    public boolean gridlinesVisible;
    /**  */
    public Integer width;
    /**  */
    public Integer height;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartOptions()
    {
        this.antialias = true;
        this.textAntiAlias = true;
        this.gridlinesVisible = true;
        this.width = null;
        this.height = null;
    }
}
