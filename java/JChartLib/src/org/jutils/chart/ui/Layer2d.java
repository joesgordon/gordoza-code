package org.jutils.chart.ui;

import java.awt.*;
import java.awt.image.BufferedImage;

import org.jutils.Utils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Layer2d
{
    /**  */
    private final Dimension size;

    /**  */
    public BufferedImage img;
    /**  */
    private Graphics2D graphics;

    /**  */
    public boolean repaint;

    /***************************************************************************
     * 
     **************************************************************************/
    public Layer2d()
    {
        this.repaint = true;
        this.size = new Dimension( 100, 100 );

        createImage();
    }

    /***************************************************************************
     * @param width
     * @param height
     **************************************************************************/
    private void createImage()
    {
        img = Utils.createTransparentImage( size.width, size.height );

        graphics = img.createGraphics();

        graphics.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON );
        graphics.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Graphics2D getGraphics()
    {
        if( size.width != img.getWidth() || size.height != img.getHeight() )
        {
            createImage();
        }

        return graphics;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clear()
    {
        graphics.setComposite( AlphaComposite.Clear );
        graphics.fillRect( 0, 0, img.getWidth(), img.getHeight() );
        graphics.setComposite( AlphaComposite.SrcOver );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void repaint()
    {
        repaint = true;
    }

    /***************************************************************************
     * @param width
     * @param height
     * @return
     **************************************************************************/
    public void setSize( Dimension size )
    {
        this.size.width = size.width;
        this.size.height = size.height;
    }

    /***************************************************************************
     * @param graphics
     **************************************************************************/
    public void paint( Graphics2D graphics )
    {
        paint( graphics, 0, 0 );
    }

    /***************************************************************************
     * @param graphics
     * @param x
     * @param y
     **************************************************************************/
    public void paint( Graphics2D graphics, int x, int y )
    {
        graphics.drawImage( img, x, y, null );
    }
}
