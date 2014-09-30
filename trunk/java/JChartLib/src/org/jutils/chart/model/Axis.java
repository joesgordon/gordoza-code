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
    public TextLabel title;
    /**  */
    public TextLabel subtitle;
    /**  */
    public boolean dockZero;

    public Axis()
    {
        this.majorSectionCount = 10;
    }
}
