package org.jutils.chart.model;

import java.awt.Color;
import java.awt.Font;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TextLabel
{
    /**  */
    public boolean visible;
    /**  */
    public String text;
    /**  */
    public Font font;
    /**  */
    public Color color;

    /***************************************************************************
     * 
     **************************************************************************/
    public TextLabel()
    {
        this.visible = true;
        this.text = "Title";
        this.font = new Font( "Helvetica", Font.PLAIN, 24 );
        this.color = Color.black;
    }
}
