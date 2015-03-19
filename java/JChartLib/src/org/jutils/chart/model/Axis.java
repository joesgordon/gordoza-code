package org.jutils.chart.model;

import java.awt.Font;

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
    public final TextLabel title;
    /**  */
    public final TextLabel subtitle;

    /***************************************************************************
     * 
     **************************************************************************/
    public Axis()
    {
        this.majorSectionCount = 10;
        this.minorSectionCount = 4;
        this.dockZero = false;
        this.title = new TextLabel();
        this.subtitle = new TextLabel();

        title.alignment = HorizontalAlignment.CENTER;
        title.font = title.font.deriveFont( 14.0f ).deriveFont( Font.BOLD );
        title.visible = false;
        title.text = "primary";

        subtitle.alignment = HorizontalAlignment.CENTER;
        subtitle.font = subtitle.font.deriveFont( 10.0f );
        subtitle.visible = false;
    }
}
