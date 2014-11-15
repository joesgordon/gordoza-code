package org.jutils.chart.model;

import java.awt.Font;

public class DimSpace
{
    /**  */
    public final TextLabel title;
    /**  */
    public final TextLabel subtitle;

    /***************************************************************************
     * 
     **************************************************************************/
    public DimSpace()
    {
        this.title = new TextLabel();
        this.subtitle = new TextLabel();

        title.alignment = HorizontalAlignment.CENTER;
        title.font = title.font.deriveFont( 14.0f ).deriveFont( Font.BOLD );

        subtitle.alignment = HorizontalAlignment.CENTER;
        subtitle.font = subtitle.font.deriveFont( 10.0f );
        subtitle.visible = false;
    }
}
