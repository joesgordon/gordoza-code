package org.jutils.drawing;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import org.jutils.SwingUtils;
import org.jutils.data.ColorMapType;
import org.jutils.drawing.HistogramView.HistogramConfig;
import org.jutils.drawing.HistogramView.Threshold;
import org.jutils.io.StringPrintStream;
import org.jutils.ui.IPaintable;
import org.jutils.ui.PaintingComponent;
import org.jutils.ui.model.IDataView;
import org.jutils.utils.RunningStat.Stats;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SingleChannelImageView implements IDataView<SingleChannelImage>
{
    /**  */
    private final JPanel view;

    /**  */
    private final ImagePaintable imgPaintable;
    /**  */
    private final PaintingComponent imgView;

    /**  */
    private final StripPaintable xStripPaintable;
    /**  */
    private final PaintingComponent xStripView;

    /**  */
    private final StripPaintable yStripPaintable;
    /**  */
    private final PaintingComponent yStripView;

    /**  */
    private final ZoomPaintable zoomPaintable;
    /**  */
    private final PaintingComponent zoomView;
    /**  */
    private final JLabel zoomLabel;

    /**  */
    private final JTextArea statsField;

    /**  */
    private final ColorModelFactory modelFactory;

    /**  */
    private SingleChannelImage image;
    /**  */
    private SingleChannelImage xStripImage;
    /**  */
    private SingleChannelImage yStripImage;
    /**  */
    private SingleChannelImage zoomImage;
    /**  */
    private IColorModel colorModel;
    /**  */
    private int zoomPixelSize;
    /**  */
    private int zoomSize;

    /***************************************************************************
     * 
     **************************************************************************/
    public SingleChannelImageView()
    {
        this.imgPaintable = new ImagePaintable( this );
        this.imgView = new PaintingComponent( imgPaintable );

        this.xStripPaintable = new StripPaintable( this, true );
        this.xStripView = new PaintingComponent( xStripPaintable );

        this.yStripPaintable = new StripPaintable( this, false );
        this.yStripView = new PaintingComponent( yStripPaintable );

        this.zoomPaintable = new ZoomPaintable( this );
        this.zoomView = new PaintingComponent( zoomPaintable );
        this.zoomLabel = new JLabel( "N/A" );

        this.statsField = new JTextArea();

        this.modelFactory = new ColorModelFactory();
        this.view = createView();

        setData( new SingleChannelImage() );
        setZoom( 7, 32, 8 );
        setColorModel( ColorMapType.GRAYSCALE );

        ImageMouseListener mouseListener = new ImageMouseListener( this );
        imgView.addMouseListener( mouseListener );
        imgView.addMouseMotionListener( mouseListener );

        SwingUtils.addKeyListener( view, "control alt UP", true,
            ( e ) -> mouseListener.updateZoom( 0, 1 ), "Zoom Up" );
        SwingUtils.addKeyListener( view, "control alt LEFT", true,
            ( e ) -> mouseListener.updateZoom( -1, 0 ), "Zoom Left" );
        SwingUtils.addKeyListener( view, "control alt DOWN", true,
            ( e ) -> mouseListener.updateZoom( 0, -1 ), "Zoom Down" );
        SwingUtils.addKeyListener( view, "control alt RIGHT", true,
            ( e ) -> mouseListener.updateZoom( 1, 0 ), "Zoom Right" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 3, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createImagePanel(), constraints );

        zoomView.setBorder( new LineBorder( Color.darkGray ) );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.NONE,
            new Insets( 0, 10, 0, 0 ), 0, 0 );
        panel.add( zoomView, constraints );

        constraints = new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.NONE,
            new Insets( 0, 10, 0, 0 ), 0, 0 );
        panel.add( zoomLabel, constraints );

        statsField.setBorder( new EtchedBorder() );
        statsField.setEditable( false );
        statsField.setFont( SwingUtils.getFixedFont( 12 ) );

        constraints = new GridBagConstraints( 1, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
            new Insets( 2, 10, 0, 0 ), 0, 0 );
        panel.add( statsField, constraints );

        return panel;
    }

    private Component createImagePanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        imgView.setBorder( new LineBorder( Color.darkGray ) );
        imgView.setFocusable( true );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( yStripView, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( imgView, constraints );

        constraints = new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( xStripView, constraints );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public SingleChannelImage getData()
    {
        return image;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( SingleChannelImage image )
    {
        this.image = image;

        Dimension ps;

        imgPaintable.setImage( image );

        ps = new Dimension( image.getWidth(), image.getHeight() );

        imgView.setPreferredSize( ps );
        imgView.setMinimumSize( ps );
        imgView.setMaximumSize( ps );
        imgView.invalidate();
        imgView.repaint();

        this.xStripImage = new SingleChannelImage( image.getWidth(), 30,
            image.getPixelDepth() );

        ps = new Dimension( xStripImage.getWidth(), xStripImage.getHeight() );

        xStripView.setPreferredSize( ps );
        xStripView.setMinimumSize( ps );
        xStripView.setMaximumSize( ps );
        xStripView.invalidate();
        xStripView.repaint();

        this.yStripImage = new SingleChannelImage( 30, image.getHeight(),
            image.getPixelDepth() );

        ps = new Dimension( yStripImage.getWidth(), yStripImage.getHeight() );

        yStripView.setPreferredSize( ps );
        yStripView.setMinimumSize( ps );
        yStripView.setMaximumSize( ps );
        yStripView.invalidate();
        yStripView.repaint();

        view.invalidate();
        view.repaint();
    }

    /***************************************************************************
     * @param stats
     **************************************************************************/
    public void setStats( Stats stats )
    {
        try( StringPrintStream text = new StringPrintStream() )
        {
            text.println( "    Range = (%.0f, %.0f) = %.0f", stats.min,
                stats.max, stats.getRange() );
            text.println( "    Mean = %.3f", stats.mean );
            text.print( "  Stddev = %.3f", stats.getStddev() );

            statsField.setText( text.toString() );
        }
    }

    /***************************************************************************
     * @param t
     **************************************************************************/
    public void setLowThreshold( Threshold t )
    {
        colorModel.setLowThreshold( t.value, t.color );
        imgView.repaint();
    }

    /***************************************************************************
     * @param t
     **************************************************************************/
    public void setHighThreshold( Threshold t )
    {
        colorModel.setHighThreshold( t.value, t.color );
        imgView.repaint();
    }

    /***************************************************************************
     * @param contrast
     **************************************************************************/
    public void setContrast( int contrast )
    {
        colorModel.setContrast( contrast );
        imgView.repaint();
    }

    /***************************************************************************
     * @param mapType
     **************************************************************************/
    public void setColorModel( ColorMapType mapType )
    {
        HistogramConfig histConfig = new HistogramConfig();
        if( colorModel != null )
        {
            histConfig = this.colorModel.getHistogramConfig();
        }
        this.colorModel = modelFactory.get( mapType, image.getPixelDepth() );
        this.colorModel.setHistogramConfig( histConfig );
        imgView.repaint();
    }

    private void setZoom( int zoomSize, int pixelSize, int pixelDepth )
    {
        this.zoomSize = zoomSize;
        this.zoomPixelSize = pixelSize;

        int zoomPixelArea = zoomSize * pixelSize;
        int totalArea = zoomPixelArea * zoomPixelArea;
        this.zoomImage = new SingleChannelImage( new int[totalArea],
            zoomPixelArea, zoomPixelArea, pixelDepth );
        this.zoomPaintable.setImage( zoomImage );

        Dimension dim = new Dimension( zoomPixelArea, zoomPixelArea );

        this.zoomView.setPreferredSize( dim );
        this.zoomView.setMinimumSize( dim );
        this.zoomView.setMaximumSize( dim );
    }

    private void captureZoom( int x, int y )
    {
        int offset = ( zoomSize + 1 ) / 2;

        int xs = x - offset;
        int ys = y - offset;

        // int xmax = image.getWidth() - zoomSize;
        // int ymax = image.getHeight() - zoomSize;

        // xs = Math.max( xs, 0 );
        // ys = Math.max( ys, 0 );
        //
        // xs = Math.min( xs, xmax );
        // ys = Math.min( ys, ymax );

        for( int xi = 0; xi < zoomSize; xi++ )
        {
            for( int yi = 0; yi < zoomSize; yi++ )
            {
                int pixel = 0;
                int xp = xs + xi;
                int yp = ys + yi;

                if( xp > -1 && yp > -1 && xp < image.getWidth() &&
                    yp < image.getHeight() )
                {
                    pixel = image.getPixel( xp, yp );
                }
                int xoff = xi * zoomPixelSize;
                int yoff = yi * zoomPixelSize;

                for( int xz = 0; xz < zoomPixelSize; xz++ )
                {
                    for( int yz = 0; yz < zoomPixelSize; yz++ )
                    {
                        zoomImage.setPixel( xz + xoff, yz + yoff, pixel );
                        // LogUtils.printDebug(
                        // "setting zoom pixel at %d,%d to %d", xz + xoff,
                        // yz + yoff, pixel );
                    }
                }
            }
        }

        zoomLabel.setText( x + ", " + y + " = " + image.getPixel( x, y ) );
        zoomView.repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ImagePaintable implements IPaintable
    {
        protected final SingleChannelImageView view;

        private SingleChannelImage image;

        public ImagePaintable( SingleChannelImageView view )
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

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ZoomPaintable extends ImagePaintable
    {
        public ZoomPaintable( SingleChannelImageView view )
        {
            super( view );
        }

        @Override
        public void paint( JComponent c, Graphics2D g )
        {
            super.paint( c, g );

            int w = c.getWidth();
            int h = c.getHeight();

            int x;
            int y;

            g.setColor( Color.yellow );

            x = w / 2 - 1;
            y = 0;
            g.fillRect( x, y, 2, h );

            x = 0;
            y = h / 2 - 1;
            g.fillRect( x, y, w, 2 );

            x = w / 2 - 1 - view.zoomPixelSize / 2;
            y = h / 2 - 1 - view.zoomPixelSize / 2;
            g.setStroke( new BasicStroke( 2, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_MITER ) );
            g.drawRect( x, y, view.zoomPixelSize + 2, view.zoomPixelSize + 2 );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class StripPaintable implements IPaintable
    {
        private final SingleChannelImageView view;
        private final boolean horiz;

        public StripPaintable( SingleChannelImageView view, boolean horizontal )
        {
            this.view = view;
            this.horiz = horizontal;
        }

        @Override
        public void paint( JComponent c, Graphics2D g )
        {
            // TODO Auto-generated method stub
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ImageMouseListener extends MouseAdapter
    {
        private final SingleChannelImageView view;

        private boolean captureZoom;
        private int lastX;
        private int lastY;

        public ImageMouseListener( SingleChannelImageView view )
        {
            this.view = view;
            this.captureZoom = true;
        }

        @Override
        public void mouseClicked( MouseEvent e )
        {
            captureZoom = !captureZoom;
            if( captureZoom )
            {
                lastX = e.getX();
                lastY = e.getY();
                view.captureZoom( lastX, lastY );
            }
            else
            {
                view.imgView.requestFocus();
            }
        }

        @Override
        public void mouseMoved( MouseEvent e )
        {
            if( captureZoom )
            {
                lastX = e.getX();
                lastY = e.getY();
                view.captureZoom( lastX, lastY );
            }
        }

        @Override
        public void mouseExited( MouseEvent e )
        {
        }

        private void updateZoom( int x, int y )
        {
            if( !captureZoom )
            {
                lastX += x;
                lastY -= y;
                view.captureZoom( lastX, lastY );
            }
        }
    }
}
