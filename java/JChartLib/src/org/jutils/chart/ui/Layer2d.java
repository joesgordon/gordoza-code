package org.jutils.chart.ui;

import java.awt.*;
import java.awt.image.BufferedImage;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Layer2d
{
    private BufferedImage img;
    private Graphics2D graphics;

    public boolean repaint;

    public Layer2d()
    {
        repaint = true;

        createImage( 100, 100 );
    }

    private void createImage( int width, int height )
    {
        img = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(
            width, height, Transparency.TRANSLUCENT );

        graphics = img.createGraphics();
    }

    public Graphics2D getGraphics()
    {
        return graphics;
    }

    public void clear()
    {
        graphics.setComposite( AlphaComposite.Clear );
        graphics.fillRect( 0, 0, img.getWidth(), img.getHeight() );
        graphics.setComposite( AlphaComposite.SrcOver );
    }

    public Graphics2D setSize( int width, int height )
    {
        if( width != img.getWidth() || height != img.getHeight() )
        {
            createImage( width, height );
        }

        return getGraphics();
    }

    public void paint( Graphics2D graphics )
    {
        paint( graphics, 0, 0 );
    }

    public void paint( Graphics2D graphics, int x, int y )
    {
        graphics.drawImage( img, x, y, null );
    }
}
