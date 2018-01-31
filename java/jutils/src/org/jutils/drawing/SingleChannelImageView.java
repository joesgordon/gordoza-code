package org.jutils.drawing;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.*;

import org.jutils.SwingUtils;
import org.jutils.data.ColorMapType;
import org.jutils.drawing.HistogramView.HistogramConfig;
import org.jutils.drawing.HistogramView.Threshold;
import org.jutils.io.StringPrintStream;
import org.jutils.ui.IPaintable;
import org.jutils.ui.PaintingComponent;
import org.jutils.ui.model.IDataView;
import org.jutils.ui.model.IView;
import org.jutils.utils.RunningStat.Stats;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SingleChannelImageView implements IDataView<SingleChannelImage>
{
    /**  */
    private final JPanel view;

    /**  */
    private final ImageView imgView;
    /**  */
    private final StripView xStripView;
    /**  */
    private final StripView yStripView;

    /**  */
    private final ZoomView zoomArea;
    /**  */
    private final JLabel zoomLabel;
    /**  */
    private final JTextArea statsField;

    /**  */
    private final ColorModelFactory modelFactory;

    /**  */
    private SingleChannelImage image;
    /**  */
    private BufferedScImage buffImage;
    /**  */
    private IColorModel colorModel;

    /***************************************************************************
     * 
     **************************************************************************/
    public SingleChannelImageView()
    {
        this.imgView = new ImageView();
        this.xStripView = new StripView( this, true );
        this.yStripView = new StripView( this, false );

        this.zoomLabel = new JLabel( "N/A" );
        this.zoomArea = new ZoomView( zoomLabel );

        this.statsField = new JTextArea();

        this.modelFactory = new ColorModelFactory();
        this.view = createView();

        this.colorModel = modelFactory.get( ColorMapType.GRAYSCALE, 8 );

        setData( new SingleChannelImage() );
        zoomArea.setZoom( 7, 32, 8 );

        ImageMouseListener mouseListener = new ImageMouseListener( this );
        imgView.getView().addMouseListener( mouseListener );
        imgView.getView().addMouseMotionListener( mouseListener );

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
     * @return the panel that contains all this view's items.
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 3, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createImagePanel(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.NONE,
            new Insets( 0, 10, 0, 0 ), 0, 0 );
        panel.add( zoomArea.getView(), constraints );

        constraints = new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.NONE,
            new Insets( 0, 10, 0, 0 ), 0, 0 );
        panel.add( zoomLabel, constraints );

        statsField.setBorder(
            new TitledBorder( new EtchedBorder(), "Image Stats" ) );
        statsField.setEditable( false );
        statsField.setFont( SwingUtils.getFixedFont( 12 ) );

        constraints = new GridBagConstraints( 1, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
            new Insets( 2, 10, 0, 0 ), 0, 0 );
        panel.add( statsField, constraints );

        return panel;
    }

    /**
     * @return the panel containing the image and strip views.
     */
    private Component createImagePanel()
    {
        // JPanel panel = new JPanel( new GridBagLayout() );
        // GridBagConstraints constraints;

        JScrollPane pane = new JScrollPane( imgView.getView() );
        pane.getVerticalScrollBar().setUnitIncrement( 12 );

        pane.setRowHeaderView( yStripView.getView() );
        pane.setColumnHeaderView( xStripView.getView() );

        // constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
        // GridBagConstraints.CENTER, GridBagConstraints.NONE,
        // new Insets( 0, 0, 0, 0 ), 0, 0 );
        // panel.add( yStripView, constraints );

        // constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
        // GridBagConstraints.CENTER, GridBagConstraints.NONE,
        // new Insets( 0, 0, 0, 0 ), 0, 0 );
        // panel.add( imgView, constraints );

        // constraints = new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
        // GridBagConstraints.CENTER, GridBagConstraints.NONE,
        // new Insets( 0, 0, 0, 0 ), 0, 0 );
        // panel.add( xStripView, constraints );

        return pane;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public SingleChannelImage getData()
    {
        return image;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( SingleChannelImage image )
    {
        this.image = image;

        this.buffImage = new BufferedScImage( image, colorModel );
        imgView.setImage( buffImage );

        xStripView.update( image );
        yStripView.update( image );

        // view.invalidate();
        // view.repaint();
    }

    /***************************************************************************
     * @param stats
     **************************************************************************/
    public void setStats( Stats stats )
    {
        try( StringPrintStream text = new StringPrintStream() )
        {
            text.println( "    Size = %d x %d", image.getWidth(),
                image.getHeight() );
            text.println( "   Range = (%.0f, %.0f) = %.0f", stats.min,
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
        buffImage.updateBufferedImage();
        imgView.getView().repaint();
    }

    /***************************************************************************
     * @param t
     **************************************************************************/
    public void setHighThreshold( Threshold t )
    {
        colorModel.setHighThreshold( t.value, t.color );
        buffImage.updateBufferedImage();
        imgView.getView().repaint();
    }

    /***************************************************************************
     * @param contrast
     **************************************************************************/
    public void setContrast( int contrast )
    {
        colorModel.setContrast( contrast );
        buffImage.updateBufferedImage();
        imgView.getView().repaint();
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
        buffImage.setModel( colorModel );
        buffImage.updateBufferedImage();
        imgView.getView().repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ImageView implements IView<JComponent>
    {
        private final PaintingComponent view;
        private final ImagePaintable paintable;

        public ImageView()
        {
            this.paintable = new ImagePaintable();
            this.view = new PaintingComponent( paintable );

            // view.setBorder( new LineBorder( Color.darkGray ) );
            view.setFocusable( true );
        }

        public void setImage( BufferedScImage image )
        {
            Dimension ps;

            paintable.setImage( image );

            ps = new Dimension( image.getWidth(), image.getHeight() );

            view.setPreferredSize( ps );
            view.setMinimumSize( ps );
            view.setMaximumSize( ps );
            view.invalidate();
            view.repaint();
        }

        @Override
        public JComponent getView()
        {
            return view;
        }

        public void setCursor( int x, int y )
        {
            paintable.setCursor( x, y );
            view.repaint();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ImagePaintable implements IPaintable
    {
        /**  */
        private BufferedScImage image;

        /**  */
        private int mousex;
        /**  */
        private int mousey;

        /**
         * 
         */
        public ImagePaintable()
        {
            this.setImage( new BufferedScImage( new SingleChannelImage() ) );
            this.mousex = -1;
            this.mousey = -1;
        }

        public void setCursor( int x, int y )
        {
            this.mousex = x;
            this.mousey = y;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void paint( JComponent c, Graphics2D g )
        {
            int w = c.getWidth();
            int h = c.getHeight();

            image.draw( g, c );

            g.setColor( Color.yellow );

            if( mousex > -1 )
            {
                g.drawLine( 0, mousey, w, mousey );
                g.drawLine( mousex, 0, mousex, h );
            }
        }

        /**
         * @param img
         */
        public void setImage( BufferedScImage img )
        {
            this.image = img;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ImageMouseListener extends MouseAdapter
    {
        /**  */
        private final SingleChannelImageView view;

        /**  */
        private boolean captureZoom;
        /**  */
        private int lastX;
        /**  */
        private int lastY;

        /**
         * @param view
         */
        public ImageMouseListener( SingleChannelImageView view )
        {
            this.view = view;
            this.captureZoom = true;
        }

        /**
         * {@inheritDoc}
         */
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
                view.imgView.getView().requestFocus();
            }
        }

        /**
         * {@inheritDoc}
         */
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

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseExited( MouseEvent e )
        {
        }

        /**
         * @param x
         * @param y
         */
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

    /***************************************************************************
     * 
     **************************************************************************/
    private static class StripPaintable implements IPaintable
    {
        /**  */
        private final SingleChannelImageView view;
        /**  */
        private final boolean isHorizontal;

        /**
         * @param view
         * @param horizontal
         */
        public StripPaintable( SingleChannelImageView view, boolean horizontal )
        {
            this.view = view;
            this.isHorizontal = horizontal;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void paint( JComponent c, Graphics2D g )
        {
            // TODO Auto-generated method stub
        }
    }

    /**
     *
     */
    private static final class StripView implements IView<JComponent>
    {
        /**  */
        private static final int STRIP_THICKNESS = 12 + 80;

        /**  */
        private final StripPaintable paintable;
        /**  */
        private final PaintingComponent view;

        /**  */
        private SingleChannelImage image;

        /**
         * @param view
         * @param horizontal
         */
        public StripView( SingleChannelImageView view, boolean horizontal )
        {
            this.paintable = new StripPaintable( view, horizontal );
            this.view = new PaintingComponent( paintable );
        }

        /**
         * @param image
         */
        public void update( SingleChannelImage image )
        {
            int width = paintable.isHorizontal ? image.getWidth()
                : STRIP_THICKNESS;
            int height = paintable.isHorizontal ? STRIP_THICKNESS
                : image.getHeight();
            Dimension ps = new Dimension( width, height );

            this.image = new SingleChannelImage( width, height,
                image.getPixelDepth() );

            view.setPreferredSize( ps );
            view.setMinimumSize( ps );
            view.setMaximumSize( ps );
            view.invalidate();
            view.repaint();
        }

        @Override
        public JComponent getView()
        {
            return view;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ZoomPaintable implements IPaintable
    {
        /**  */
        private int zoomPixelSize;
        private SingleChannelImage image;

        /**
         * @param view
         */
        public ZoomPaintable()
        {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void paint( JComponent c, Graphics2D g )
        {
            int w = c.getWidth();
            int h = c.getHeight();

            for( int x = 0; x < image.getWidth(); x++ )
            {
                for( int y = 0; y < image.getHeight(); y++ )
                {
                    int pixel = image.getPixel( x, y );
                    g.setColor( new Color( pixel ) );
                    g.fillRect( x * zoomPixelSize, y * zoomPixelSize,
                        zoomPixelSize, zoomPixelSize );
                }
            }

            int boxx = w / 2 - 1 - zoomPixelSize / 2;
            int boxy = h / 2 - 1 - zoomPixelSize / 2;

            int x;
            int y;

            g.setColor( Color.yellow );

            x = w / 2 - 1;
            y = 0;
            g.fillRect( x, y, 2, boxy );

            x = 0;
            y = h / 2 - 1;
            g.fillRect( x, y, boxx, 2 );

            x = w / 2 - 1;
            y = boxy + zoomPixelSize + 2;
            g.fillRect( x, y, 2, boxy );

            x = boxx + zoomPixelSize + 2;
            y = h / 2 - 1;
            g.fillRect( x, y, boxx, 2 );

            g.setStroke( new BasicStroke( 2, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_MITER ) );
            g.drawRect( boxx, boxy, zoomPixelSize + 2, zoomPixelSize + 2 );
        }

        /**
         * @param zoomPixelSize
         */
        public void setZoomPixelSize( int zoomPixelSize )
        {
            this.zoomPixelSize = zoomPixelSize;
        }

        public void setImage( SingleChannelImage image )
        {
            this.image = image;
        }
    }

    private static final class ZoomView implements IView<JComponent>
    {
        /**  */
        private final ZoomPaintable zoomPaintable;
        /**  */
        private final PaintingComponent zoomView;
        /**  */
        private final JLabel zoomLabel;

        /**  */
        private SingleChannelImage zoomImage;
        /**  */
        private int zoomPixelSize;
        /**  */
        private int zoomSize;

        public ZoomView( JLabel zoomLabel )
        {
            this.zoomPaintable = new ZoomPaintable();
            this.zoomView = new PaintingComponent( zoomPaintable );
            this.zoomLabel = zoomLabel;

            zoomView.setBorder( new LineBorder( Color.darkGray ) );
        }

        @Override
        public JComponent getView()
        {
            return zoomView;
        }

        /***************************************************************************
         * @param zoomSize
         * @param pixelSize
         * @param pixelDepth
         **************************************************************************/
        private void setZoom( int zoomSize, int pixelSize, int pixelDepth )
        {
            this.zoomSize = zoomSize;
            this.zoomPixelSize = pixelSize;

            int imageArea = zoomSize * zoomSize;
            this.zoomImage = new SingleChannelImage( new int[imageArea],
                zoomSize, zoomSize, pixelDepth );
            this.zoomPaintable.setImage( zoomImage );

            int zoomSideLen = pixelSize * zoomSize;

            Dimension dim = new Dimension( zoomSideLen, zoomSideLen );

            this.zoomView.setPreferredSize( dim );
            this.zoomView.setMinimumSize( dim );
            this.zoomView.setMaximumSize( dim );
            this.zoomPaintable.setZoomPixelSize( pixelSize );
        }

        /***************************************************************************
         * @param x
         * @param y
         **************************************************************************/
        private void captureZoom( int x, int y, BufferedScImage buffImage )
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

                    if( xp > -1 && yp > -1 && xp < buffImage.getWidth() &&
                        yp < buffImage.getHeight() )
                    {
                        pixel = buffImage.getPixel( xp, yp );
                    }

                    zoomImage.setPixel( xi, yi, pixel );
                }
            }

            int coordY = buffImage.getHeight() - y;

            zoomLabel.setText(
                x + ", " + coordY + " = " + buffImage.getRawPixel( x, y ) );
            zoomView.repaint();
        }
    }

    public void captureZoom( int x, int y )
    {
        zoomArea.captureZoom( x, y, buffImage );
        imgView.setCursor( x, y );
    }
}
