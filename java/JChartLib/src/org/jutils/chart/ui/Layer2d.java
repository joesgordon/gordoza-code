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
        repaint = true;

        createImage( 100, 100 );
    }

    /***************************************************************************
     * @param width
     * @param height
     **************************************************************************/
    private void createImage( int width, int height )
    {
        img = Utils.createTransparentImage( width, height );

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
     * @param width
     * @param height
     * @return
     **************************************************************************/
    public Graphics2D setSize( int width, int height )
    {
        if( width != img.getWidth() || height != img.getHeight() )
        {
            createImage( width, height );
        }

        return getGraphics();
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
