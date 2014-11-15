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
    public boolean dockZero;
    /**  */
    public final DimSpace primary;
    /**  */
    public final DimSpace secondary;

    /***************************************************************************
     * 
     **************************************************************************/
    public Axis()
    {
        this.majorSectionCount = 10;
        this.minorSectionCount = 4;
        this.dockZero = false;
        this.primary = new DimSpace();
        this.secondary = new DimSpace();

        primary.title.visible = false;
        primary.title.text = "primary";

        secondary.title.visible = false;
        secondary.title.text = "secondary";
    }
}
