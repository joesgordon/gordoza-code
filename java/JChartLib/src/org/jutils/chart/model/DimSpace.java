package org.jutils.chart.model;

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

        title.font = title.font.deriveFont( 14.0f );

        subtitle.font = subtitle.font.deriveFont( 10.0f );
        subtitle.visible = false;
    }
}
