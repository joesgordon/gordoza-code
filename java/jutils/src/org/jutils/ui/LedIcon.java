package org.jutils.ui;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

import org.jutils.Utils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LedIcon implements Icon
{
    /**  */
    private int height;
    /**  */
    private int width;
    /**  */
    private Color color;
    /**  */
    private BufferedImage img;

    /***************************************************************************
     * @param color
     **************************************************************************/
    public LedIcon( Color color )
    {
        this( color, 16 );
    }

    /***************************************************************************
     * @param color
     * @param size
     **************************************************************************/
    public LedIcon( Color color, int size )
    {
        this( color, size, size );
    }

    /***************************************************************************
     * @param color
     * @param width
     * @param height
     **************************************************************************/
    public LedIcon( Color color, int width, int height )
    {
        this.color = color;
        this.width = width;
        this.height = height;

        drawImage();
    }

    private void drawImage()
    {
        // img = new BufferedImage( size.width, size.height,
        // BufferedImage.TYPE_INT_RGB );
        img = Utils.createTransparentImage( width, height );

        Graphics2D graphics = img.createGraphics();

        graphics.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON );
        graphics.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

        graphics.setColor( this.color );
        graphics.fillOval( 0, 0, width, height );
        // g.setColor( Color.black );
        // g2.setStroke( new BasicStroke( 1.25f ) );
        // g.drawOval( x, y, getIconWidth(), getIconHeight() );

        graphics.dispose();
    }

    /***************************************************************************
     * @param color
     **************************************************************************/
    public void setColor( Color color )
    {
        this.color = color;
        drawImage();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Color getColor()
    {
        return this.color;
    }

    /***************************************************************************
     * @param height
     **************************************************************************/
    public void setIconHeight( int height )
    {
        this.height = height;
        drawImage();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getIconHeight()
    {
        return height;
    }

    /***************************************************************************
     * @param width
     **************************************************************************/
    public void setIconWidth( int width )
    {
        this.width = width;
        drawImage();
    }

    /***************************************************************************
     * @param width
     **************************************************************************/
    public void setIconSize( int size )
    {
        this.height = size;
        this.width = size;
        drawImage();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getIconWidth()
    {
        return width;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void paintIcon( Component c, Graphics g, int x, int y )
    {
        Graphics2D g2 = ( Graphics2D )g;

        g2.drawImage( img, x, y, null );
    }
}
