package org.jutils.drawing;

import org.jutils.drawing.HistogramView.HistogramConfig;
import org.jutils.utils.RunningStat;
import org.jutils.utils.RunningStat.Stats;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SingleChannelImage
{
    /**  */
    private final int [] pixels;
    /**  */
    private final int width;
    /**  */
    private final int height;
    /**  */
    private final int pixelDepth;

    /***************************************************************************
     * 
     **************************************************************************/
    public SingleChannelImage()
    {
        this( new int[20 * 20], 20, 20, 8 );

        for( int i = 0; i < pixels.length; i++ )
        {
            pixels[i] = ( int )( i / 255.0 / pixels.length );
        }
    }

    /***************************************************************************
     * @param pixels
     * @param width
     * @param height
     * @param pixelDepth
     **************************************************************************/
    public SingleChannelImage( int [] pixels, int width, int height,
        int pixelDepth )
    {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        this.pixelDepth = pixelDepth;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getWidth()
    {
        return width;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getHeight()
    {
        return height;
    }

    public int getPixelDepth()
    {
        return pixelDepth;
    }

    /***************************************************************************
     * @param x
     * @param y
     * @return
     **************************************************************************/
    public int getPixel( int x, int y )
    {
        return pixels[y * width + x];
    }

    /***************************************************************************
     * @param x
     * @param y
     * @param value
     **************************************************************************/
    public void setPixel( int x, int y, int value )
    {
        pixels[y * width + x] = value;
    }

    /***************************************************************************
     * @param pixels
     **************************************************************************/
    public void setPixels( int [] pixels )
    {
        if( pixels.length == this.pixels.length )
        {
            System.arraycopy( pixels, 0, this.pixels, 0, pixels.length );
        }
        else
        {
            throw new IllegalArgumentException(
                "The image provided is a differnt size than the image stored" );
        }
    }

    /***************************************************************************
     * @param pixels
     **************************************************************************/
    public void setPixels( byte [] pixels )
    {
        if( pixels.length == this.pixels.length )
        {
            for( int i = 0; i < pixels.length; i++ )
            {
                this.pixels[i] = Byte.toUnsignedInt( pixels[i] );
            }
        }
        else
        {
            throw new IllegalArgumentException(
                "The image provided is a differnt size than the image stored" );
        }
    }

    /***************************************************************************
     * @param histConfig
     * @return
     **************************************************************************/
    public ImageStats getStats( HistogramConfig histConfig )
    {
        RunningStat rstat = new RunningStat();
        int [] hist = new int[histConfig.binCount];
        double pixelValueMax = Math.pow( 2.0, pixelDepth ) - 1;
        double scale = ( histConfig.binCount - 1 ) / pixelValueMax;

        for( int i = 0; i < pixels.length; i++ )
        {
            int bin = ( int )( pixels[i] * scale );
            hist[bin]++;
            rstat.account( pixels[i] );
        }

        return new ImageStats( rstat.calcStats(), hist );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class ImageStats
    {
        public final Stats stats;
        public final int [] histogram;

        public ImageStats( Stats stats, int [] histogram )
        {
            this.stats = stats;
            this.histogram = histogram;
        }
    }
}
