package org.jutils.drawing;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.*;

/***************************************************************************
 *
 **************************************************************************/
public class BufferedScImage
{
    /**  */
    private final BufferedImage buffImage;
    /**  */
    private final int [] buff;
    /**  */
    private final SingleChannelImage image;

    /**  */
    private IColorModel colorModel;

    /***************************************************************************
     * @param width the width of the new image.
     * @param height the height of the new image.
     * @param pixelDepth the number of bits per pixel in the new image.
     **************************************************************************/
    public BufferedScImage( int width, int height, int pixelDepth )
    {
        this( new SingleChannelImage( width, height, pixelDepth ) );
    }

    /***************************************************************************
     * @param image the image to be buffered.
     **************************************************************************/
    public BufferedScImage( SingleChannelImage image )
    {
        this.image = image;
        this.buff = new int[image.getPixels().length];
        this.colorModel = null;

        colorModel = ColorModelFactory.createGrayscaleMap(
            image.getPixelDepth() );

        BufferedImage gsImage;

        int [] masks = new int[] { 0x00FF0000, 0x00FF00, 0x00FF };
        ColorModel mdl = new DirectColorModel( 32, masks[0], masks[1],
            masks[2] );
        SinglePixelPackedSampleModel sampleModel = new SinglePixelPackedSampleModel(
            DataBuffer.TYPE_INT, image.getWidth(), image.getHeight(), masks );
        DataBuffer dataBuf = new DataBufferInt( buff, buff.length );
        WritableRaster raster = WritableRaster.createWritableRaster(
            sampleModel, dataBuf, new Point( 0, 0 ) );

        gsImage = new BufferedImage( mdl, raster, false, null );

        // gsImage = new BufferedImage( image.getWidth(), image.getHeight(),
        // BufferedImage.TYPE_INT_RGB );

        // LogUtils.printDebug( "%s %s",
        // gsImage.getColorModel().getClass().toString(),
        // gsImage.getSampleModel().getClass() );

        this.buffImage = gsImage;

        setPixels( image.getPixels() );
    }

    /***************************************************************************
     * @param g the graphics on which this image is to be drawn.
     * @param observer object to be notified as more of the image is converted.
     **************************************************************************/
    public void draw( Graphics g, ImageObserver observer )
    {
        draw( g, 0, 0, observer );
    }

    /***************************************************************************
     * @param g the graphics on which this image is to be drawn.
     * @param x the x coordinate at which the image is to be drawn.
     * @param y the y coordinate at which the image is to be drawn.
     * @param observer object to be notified as more of the image is converted.
     **************************************************************************/
    public void draw( Graphics g, int x, int y, ImageObserver observer )
    {
        g.drawImage( buffImage, x, y, observer );
    }

    /***************************************************************************
     * @param pixels the new pixels to represent this image.
     **************************************************************************/
    public void setPixels( int [] pixels )
    {
        image.setPixels( pixels );

        updateBufferedImage();

        // wr.setPixels( 0, 0, image.getWidth(), image.getHeight(),
        // image.getPixels() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void updateBufferedImage()
    {
        int [] raw = image.getPixels();
        int [] bp = new int[raw.length];

        for( int i = 0; i < raw.length; i++ )
        {
            int pixel = raw[i];
            pixel = colorModel.getColorValue( pixel );
            bp[i] = pixel;
        }

        WritableRaster wr = buffImage.getRaster();
        wr.setDataElements( 0, 0, getWidth(), getHeight(), bp );
    }

    /**
     * @return the width of this image.
     */
    public int getWidth()
    {
        return image.getWidth();
    }

    /**
     * @return the height of this image.
     */
    public int getHeight()
    {
        return image.getHeight();
    }

    /**
     * @return the bits per pixel in this image.
     */
    public int getPixelDepth()
    {
        return image.getPixelDepth();
    }

    /**
     * @param colorModel the color model to be used when drawing this image.
     */
    public void setModel( IColorModel colorModel )
    {
        if( colorModel == null )
        {
            this.colorModel = ColorModelFactory.createGrayscaleMap(
                image.getPixelDepth() );
        }
        else
        {
            this.colorModel = colorModel;
        }
    }

    /**
     * @return the buffered image
     */
    public BufferedImage getBufferedImage()
    {
        return buffImage;
    }

    /**
     * @param x
     * @param y
     * @return
     */
    public int getPixel( int x, int y )
    {
        int idx = y * image.getWidth() + x;
        return buff[idx];
    }

    /**
     * @param x
     * @param y
     * @return
     */
    public int getRawPixel( int x, int y )
    {
        return image.getPixel( x, y );
    }
}
