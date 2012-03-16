package org.jutils.ui;

import java.awt.*;

import javax.swing.Icon;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ColorIcon implements Icon
{
    /**  */
    private int height;
    /**  */
    private int width;
    /**  */
    private Color color;

    public ColorIcon( Color color )
    {
        this( color, 16 );
    }

    public ColorIcon( Color color, int size )
    {
        this( color, size, size );
    }

    public ColorIcon( Color color, int width, int height )
    {
        setColor( color );
        setIconWidth( width );
        this.height = height;
    }

    public void setColor( Color color )
    {
        this.color = color;
    }

    public void setIconHeight( int height )
    {
        this.height = height;
    }

    public int getIconHeight()
    {
        return height;
    }

    public void setIconWidth( int width )
    {
        this.width = width;
    }

    public int getIconWidth()
    {
        return width;
    }

    public void paintIcon( Component c, Graphics g, int x, int y )
    {
        g.setColor( color );
        g.fill3DRect( x, y, getIconWidth(), getIconHeight(), true );
    }
}
