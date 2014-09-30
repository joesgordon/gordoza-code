package org.jutils.chart.model;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Axis
{
    /**  */
    public int majorSectionCount;
    /**  */
    public int minorSectionCount;
    /**  */
    public final TextLabel title;
    /**  */
    public final TextLabel subtitle;
    /**  */
    public boolean dockZero;

    public Axis()
    {
        this.majorSectionCount = 10;
        this.title = new TextLabel();
        this.subtitle = new TextLabel();
        this.dockZero = false;

        subtitle.visible = false;
    }
}
