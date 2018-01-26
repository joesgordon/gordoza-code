package org.jutils.drawing;

import java.awt.*;

import javax.swing.JComponent;

import org.jutils.data.ColorMapType;
import org.jutils.drawing.HistogramView.HistogramConfig;
import org.jutils.drawing.HistogramView.Threshold;
import org.jutils.ui.IPaintable;
import org.jutils.ui.PaintingComponent;
import org.jutils.ui.model.IDataView;

public class SingleChannelImageView implements IDataView<SingleChannelImage>
{
    private final PaintingComponent view;
    private final SciPaintable paintable;
    private final ColorModelFactory modelFactory;

    private SingleChannelImage image;
    private IColorModel colorModel;

    public SingleChannelImageView()
    {
        this.paintable = new SciPaintable( this );
        this.view = new PaintingComponent( paintable );
        this.modelFactory = new ColorModelFactory();

        this.image = new SingleChannelImage();
        setColorModel( ColorMapType.GRAYSCALE );
    }

    @Override
    public JComponent getView()
    {
        return view;
    }

    @Override
    public SingleChannelImage getData()
    {
        return image;
    }

    @Override
    public void setData( SingleChannelImage image )
    {
        Dimension ps = new Dimension( image.getWidth(), image.getHeight() );

        view.setPreferredSize( ps );
        view.setMinimumSize( ps );
        view.setMaximumSize( ps );
        view.repaint();
        view.validate();

        this.image = image;

        paintable.setImage( image );
    }

    public void setLowThreshold( Threshold t )
    {
        colorModel.setLowThreshold( t.value, t.color );
        view.repaint();
    }

    public void setHighThreshold( Threshold t )
    {
        colorModel.setHighThreshold( t.value, t.color );
        view.repaint();
    }

    public void setColorModel( ColorMapType mapType )
    {
        HistogramConfig histConfig = new HistogramConfig();
        if( colorModel != null )
        {
            histConfig = this.colorModel.getHistogramConfig();
        }
        this.colorModel = modelFactory.get( mapType, image.getPixelDepth() );
        this.colorModel.setHistogramConfig( histConfig );
        view.repaint();
    }

    private static final class SciPaintable implements IPaintable
    {
        private final SingleChannelImageView view;

        private SingleChannelImage image;

        public SciPaintable( SingleChannelImageView view )
        {
            this.view = view;
            this.setImage( new SingleChannelImage() );
        }

        public void setImage( SingleChannelImage image )
        {
            this.image = image;
        }

        @Override
        public void paint( JComponent c, Graphics2D g )
        {
            int w = c.getWidth();
            int h = c.getHeight();

            g.setBackground( Color.WHITE );
            g.clearRect( 0, 0, w, h );

            for( int x = 0; x < image.getWidth(); x++ )
            {
                for( int y = 0; y < image.getHeight(); y++ )
                {
                    int pixel = image.getPixel( x, y );
                    Color clr = view.colorModel.getColor( pixel );

                    g.setColor( clr );
                    g.fillRect( x, y, 1, 1 );
                }
            }
        }
    }
}
