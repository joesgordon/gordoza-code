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
    public Integer width;
    /**  */
    public Integer height;
    /**  */
    public final TextLabel title;
    /**  */
    public final TextLabel topBottomLabel;

    // public List<Series> series;
    /**  */
    public final Axis domainAxis;
    /**  */
    public final Axis rangeAxis;

    /***************************************************************************
     * 
     **************************************************************************/
    public Chart()
    {
        this.antialias = true;
        this.textAntiAlias = true;
        this.gridlinesVisible = true;
        this.width = null;
        this.height = null;
        this.title = new TextLabel();
        this.topBottomLabel = new TextLabel();
        this.domainAxis = new Axis();
        this.rangeAxis = new Axis();

        topBottomLabel.font = topBottomLabel.font.deriveFont( 10.0f );
        topBottomLabel.visible = false;
    }
}
