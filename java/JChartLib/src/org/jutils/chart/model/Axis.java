package org.jutils.chart.model;

import java.awt.Font;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Axis
{
    /**  */
    public final TextLabel title;
    /**  */
    public final TextLabel subtitle;
    /**  */
    public boolean autoTicks;
    /**  */
    public double tickStart;
    /**  */
    public double tickEnd;
    /**  */
    public double tickWidth;
    /**  */
    public double minorTickWidth;
    /**  */
    public boolean dockZero;

    /***************************************************************************
     * 
     **************************************************************************/
    public Axis()
    {
        this.title = new TextLabel();
        this.subtitle = new TextLabel();
        this.autoTicks = true;
        this.tickStart = -5.0;
        this.tickEnd = 5.0;
        this.tickWidth = 1.0;
        this.minorTickWidth = 0.25;
        this.dockZero = false;

        title.alignment = HorizontalAlignment.CENTER;
        title.font = title.font.deriveFont( 14.0f ).deriveFont( Font.BOLD );
        title.visible = false;
        title.text = "primary";

        subtitle.alignment = HorizontalAlignment.CENTER;
        subtitle.font = subtitle.font.deriveFont( 10.0f );
        subtitle.visible = false;
    }
}
