package org.jutils.drawing;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.LineBorder;

import org.jutils.data.ColorMapType;
import org.jutils.drawing.HistogramView.HistogramConfig;
import org.jutils.ui.*;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.ComboNavFormField;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HistogramView implements IDataView<HistogramConfig>
{
    /**  */
    public static final int MAX_BIN_COUNT = 65536;

    private static final Color DEFAULT_LOW_COLOR = Color.blue;

    private static final Color DEFAULT_HIGH_COLOR = Color.red;

    /**  */
    private final JPanel view;

    /**  */
    private final HistogramPaintable paintable;
    /**  */
    private final PaintingComponent histComp;

    /**  */
    private final LedColorButton lowButton;
    /**  */
    private final JSlider lowBar;
    /**  */
    private final IntegerFormField lowField;

    /**  */
    private final LedColorButton highButton;
    /**  */
    private final JSlider highBar;
    /**  */
    private final IntegerFormField highField;

    /**  */
    private final JSlider binCountBar;
    /**  */
    private final IntegerFormField binCountField;

    /**  */
    private final JSlider contrastBar;
    /**  */
    private final IntegerFormField contrastField;

    /**  */
    private final ComboNavFormField<ColorMapType> colorMapField;

    /**  */
    private DepthMetrics pixelMetrics;
    /**  */
    private HistogramConfig config;
    /**  */
    private boolean isUpdating;

    private IUpdater<Threshold> lowUpdater;

    private IUpdater<Threshold> highUpdater;

    private IUpdater<Integer> binUpdater;

    private IUpdater<Integer> contrastUpdater;

    /***************************************************************************
     * 
     **************************************************************************/
    public HistogramView()
    {
        this( 8 );
    }

    /***************************************************************************
     * @param pixelDepth
     **************************************************************************/
    public HistogramView( int pixelDepth )
    {
        this.pixelMetrics = new DepthMetrics( pixelDepth );

        this.paintable = new HistogramPaintable();
        this.histComp = new PaintingComponent( paintable );

        this.lowButton = new LedColorButton( DEFAULT_LOW_COLOR );
        this.lowBar = new JSlider( JSlider.HORIZONTAL, 0,
            pixelMetrics.pixelValueMax, 0 );
        this.lowField = new IntegerFormField( "Low Threshold", null, 6, 0,
            pixelMetrics.pixelValueMax );

        this.highButton = new LedColorButton( DEFAULT_HIGH_COLOR );
        this.highBar = new JSlider( JSlider.HORIZONTAL, 0,
            pixelMetrics.pixelValueMax, 0 );
        this.highField = new IntegerFormField( "High Threshold", null, 6, 0,
            pixelMetrics.pixelValueMax );

        this.binCountBar = new JSlider( JSlider.HORIZONTAL, 1,
            pixelMetrics.maxBinCount, 1 );
        this.binCountField = new IntegerFormField( "Bin Count", null, 6, 0,
            pixelMetrics.maxBinCount );

        this.contrastBar = new JSlider( JSlider.HORIZONTAL, -100, 100, 0 );
        this.contrastField = new IntegerFormField( "Contrast", null, 6, -100,
            100 );

        this.colorMapField = new ComboNavFormField<>( "Color Map",
            ColorMapType.values() );

        this.view = createView();

        this.config = null;
        this.isUpdating = false;
        this.lowUpdater = null;
        this.highUpdater = null;
        this.binUpdater = null;
        this.contrastUpdater = null;

        HistogramConfig histCfg = new HistogramConfig();
        histCfg.lowThreshold.value = 0;
        histCfg.highThreshold.value = pixelMetrics.pixelValueMax;
        setData( histCfg );

        lowButton.setUpdater( ( v ) -> {
            config.lowThreshold.color = v;
            histComp.repaint();
            if( lowUpdater != null )
            {
                lowUpdater.update( config.lowThreshold );
            }
        } );
        lowBar.addChangeListener( ( e ) -> {
            if( !isUpdating )
            {
                setLowValue( false, true, lowBar.getValue() );
            }
        } );
        lowField.setUpdater( ( v ) -> {
            setLowValue( true, false, v );
        } );

        highButton.setUpdater( ( v ) -> {
            config.highThreshold.color = v;
            histComp.repaint();
            if( highUpdater != null )
            {
                highUpdater.update( config.highThreshold );
            }
        } );
        highBar.addChangeListener( ( e ) -> {
            if( !isUpdating )
            {
                setHighValue( false, true, highBar.getValue() );
            }
        } );
        highField.setUpdater( ( v ) -> {
            setHighValue( true, false, v );
        } );

        binCountBar.addChangeListener( ( e ) -> {
            if( !isUpdating )
            {
                isUpdating = true;
                config.binCount = binCountBar.getValue();
                binCountField.setValue( config.binCount );
                isUpdating = false;
                if( binUpdater != null )
                {
                    binUpdater.update( config.binCount );
                }
            }
        } );
        binCountField.setUpdater( ( v ) -> {
            if( !isUpdating )
            {
                isUpdating = true;
                config.binCount = v;
                binCountBar.setValue( config.binCount );
                isUpdating = false;
                if( binUpdater != null )
                {
                    binUpdater.update( config.binCount );
                }
            }
        } );

        contrastBar.addChangeListener( ( e ) -> {
            if( !isUpdating )
            {
                isUpdating = true;
                config.contrast = contrastBar.getValue();
                contrastField.setValue( config.contrast );
                isUpdating = false;
                histComp.repaint();
                if( contrastUpdater != null )
                {
                    contrastUpdater.update( config.contrast );
                }
            }
        } );
        contrastField.setUpdater( ( v ) -> {
            if( !isUpdating )
            {
                isUpdating = true;
                config.contrast = v;
                contrastBar.setValue( config.contrast );
                isUpdating = false;
                histComp.repaint();
                if( contrastUpdater != null )
                {
                    contrastUpdater.update( config.contrast );
                }
            }
        } );
    }

    private void setLowValue( boolean setBar, boolean setField, int value )
    {
        config.lowThreshold.value = value;

        if( setBar )
        {
            isUpdating = true;
            lowBar.setValue( value );
            isUpdating = false;
        }

        if( setField )
        {
            lowField.setValue( value );
        }

        if( config.lowThreshold.value > config.highThreshold.value )
        {
            setHighValue( true, true, value );
        }
        histComp.repaint();
        if( lowUpdater != null )
        {
            lowUpdater.update( config.lowThreshold );
        }
    }

    private void setHighValue( boolean setBar, boolean setField, int value )
    {
        config.highThreshold.value = value;

        if( setBar )
        {
            isUpdating = true;
            highBar.setValue( value );
            isUpdating = false;
        }

        if( setField )
        {
            highField.setValue( value );
        }

        if( config.lowThreshold.value > config.highThreshold.value )
        {
            setLowValue( true, true, value );
        }
        histComp.repaint();
        if( highUpdater != null )
        {
            highUpdater.update( config.highThreshold );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        GridBagConstraints constraints;

        histComp.setBorder( new LineBorder( Color.black ) );
        histComp.setPreferredSize( new Dimension( 100, 100 ) );
        histComp.setMinimumSize( histComp.getPreferredSize() );
        histComp.addMouseListener( new HistMouseListener( this ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( histComp, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( StandardFormView.DEFAULT_FORM_MARGIN, 0, 0, 0 ), 0, 0 );
        panel.add( createFieldsPanel(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createFieldsPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        int fm = StandardFormView.DEFAULT_FIELD_MARGIN;

        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, fm ), 0, 0 );
        panel.add( lowButton.getView(), constraints );

        lowBar.setPreferredSize(
            new Dimension( 200, lowBar.getPreferredSize().height ) );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, fm ), 0, 0 );
        panel.add( lowBar, constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( lowField.getView(), constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( fm, 0, 0, fm ), 0, 0 );
        panel.add( highButton.getView(), constraints );

        highBar.setPreferredSize( lowBar.getPreferredSize() );

        constraints = new GridBagConstraints( 1, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( fm, 0, 0, fm ), 0, 0 );
        panel.add( highBar, constraints );

        constraints = new GridBagConstraints( 2, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( fm, 0, 0, 0 ), 0, 0 );
        panel.add( highField.getView(), constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( fm, 0, 0, fm ), 0, 0 );
        panel.add( new JLabel( "Bin Count: " ), constraints );

        binCountBar.setPreferredSize( lowBar.getPreferredSize() );

        constraints = new GridBagConstraints( 1, 3, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( fm, 0, 0, fm ), 0, 0 );
        panel.add( binCountBar, constraints );

        constraints = new GridBagConstraints( 2, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( fm, 0, 0, 0 ), 0, 0 );
        panel.add( binCountField.getView(), constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, 5, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( fm, 0, 0, fm ), 0, 0 );
        panel.add( new JLabel( "Contrast: " ), constraints );

        contrastBar.setPreferredSize( lowBar.getPreferredSize() );

        constraints = new GridBagConstraints( 1, 5, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( fm, 0, 0, fm ), 0, 0 );
        panel.add( contrastBar, constraints );

        constraints = new GridBagConstraints( 2, 5, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( fm, 0, 0, 0 ), 0, 0 );
        panel.add( contrastField.getView(), constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, 6, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( fm, 0, 0, fm ), 0, 0 );
        panel.add( new JLabel( "Color Map: " ), constraints );

        constraints = new GridBagConstraints( 1, 6, 2, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( fm, 0, 0, 0 ), 0, 0 );
        panel.add( colorMapField.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public HistogramConfig getData()
    {
        return config;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( HistogramConfig config )
    {
        this.config = config;

        lowButton.setData( config.lowThreshold.color );
        lowBar.setValue( config.lowThreshold.value );
        lowField.setValue( config.lowThreshold.value );

        highButton.setData( config.highThreshold.color );
        highBar.setValue( config.highThreshold.value );
        highField.setValue( config.highThreshold.value );

        binCountBar.setValue( config.binCount );
        binCountField.setValue( config.binCount );

        contrastBar.setValue( config.contrast );
        contrastField.setValue( config.contrast );

        colorMapField.setValue( config.colorMap );

        paintable.setData( config );
    }

    /***************************************************************************
     * The number of pixels found with each value from 0 to
     * {@link #maxBinCount}.
     * @param hist
     **************************************************************************/
    public void setHistogram( int [] histogram )
    {
        paintable.setHistogram( histogram );
        histComp.repaint();
    }

    public void setLowUpdater( IUpdater<Threshold> updater )
    {
        this.lowUpdater = updater;
    }

    public void setHighUpdater( IUpdater<Threshold> updater )
    {
        this.highUpdater = updater;
    }

    public void setBinCountUpdater( IUpdater<Integer> updater )
    {
        this.binUpdater = updater;
    }

    public void setContrastUpdater( IUpdater<Integer> updater )
    {
        this.contrastUpdater = updater;
    }

    public void setColorModelUpdater( IUpdater<ColorMapType> updater )
    {
        colorMapField.setUpdater( updater );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void resetHistogram()
    {
        config.reset();

        setData( config );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class DepthMetrics
    {
        /**  */
        public final int pixelDepth;
        /**  */
        public final int pixelValueCnt;
        /**  */
        public final int pixelValueMax;
        /**
         * The maximum number of bins allowed for the provided
         * {@link #pixelDepth}.
         */
        public final int maxBinCount;

        public DepthMetrics( int pixelDepth )
        {
            this.pixelDepth = pixelDepth;
            this.pixelValueCnt = ( int )Math.pow( 2, pixelDepth );
            this.pixelValueMax = pixelValueCnt - 1;
            this.maxBinCount = pixelDepth > 16 ? MAX_BIN_COUNT : pixelValueCnt;
        }
    }

    public static class Threshold
    {
        public int value;
        public Color color;

        public Threshold()
        {
            this( 0, Color.GREEN );
        }

        public Threshold( int value, Color color )
        {
            this.value = value;
            this.color = color;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class HistogramConfig
    {
        public final Threshold lowThreshold;
        public final Threshold highThreshold;

        public int binCount;

        public int contrast;

        public ColorMapType colorMap;

        public HistogramConfig()
        {
            this.lowThreshold = new Threshold( 0, Color.blue );
            this.highThreshold = new Threshold( 255, Color.red );

            this.binCount = 256;

            this.contrast = 0;

            this.colorMap = ColorMapType.GRAYSCALE;
        }

        public void reset()
        {
            this.binCount = 256;
            this.lowThreshold.value = 0;
            this.highThreshold.value = 255;
            this.contrast = 0;
        }

        public double [] calcContrastLine()
        {
            return calcContrastLine( contrast );
        }

        public static double [] calcContrastLine( int contrast )
        {
            double angleMin = Math.atan( 1 / 255.0 );
            double angleMax = Math.atan( 255.0 );

            double angle = angleMax +
                ( contrast - 100 ) * ( angleMax - angleMin ) / 200.0;
            double m = Math.tan( angle );
            double b = 128.0 - m * 128.0;

            return new double[] { m, b };
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class HistogramPaintable implements IPaintable
    {
        private int [] hist;
        private int histMax;
        private HistogramConfig config;

        public HistogramPaintable()
        {
            this.hist = new int[256];
            this.histMax = 0;
        }

        @Override
        public void paint( JComponent c, Graphics2D g )
        {
            int w = c.getWidth();
            int h = c.getHeight();

            g.setBackground( Color.white );
            g.clearRect( 0, 0, w, h );

            float xs = w / ( float )hist.length;
            int width = 1 + ( int )xs;

            float ys = ( h - 4 ) / ( float )histMax;

            float binScale = 255.0f / hist.length;
            int i = 0;

            g.setColor( config.lowThreshold.color );
            for( ; i < config.binCount; i++ )
            {
                int bin = Math.round( binScale * i );
                if( bin < config.lowThreshold.value )
                {
                    paintBin( c, g, i, xs, ys, width );
                }
                else
                {
                    break;
                }
            }

            g.setColor( Color.black );
            for( ; i < config.binCount; i++ )
            {
                int bin = Math.round( binScale * i );

                if( bin < config.highThreshold.value )
                {
                    paintBin( c, g, i, xs, ys, width );
                }
                else
                {
                    break;
                }
            }

            g.setColor( config.highThreshold.color );
            for( ; i < config.binCount; i++ )
            {
                paintBin( c, g, i, xs, ys, width );
            }

            if( config.lowThreshold.value > 0 )
            {
                int xl = ( int )( config.lowThreshold.value * xs );

                g.setColor( config.lowThreshold.color );
                g.fillRect( 0, 0, xl, 2 );
                g.fillRect( xl, 0, 2, h );
            }

            if( config.highThreshold.value < 255 )
            {
                int xh = ( int )( config.highThreshold.value * xs );

                g.setColor( config.highThreshold.color );
                g.fillRect( xh, 0, w - xh, 2 );
                g.fillRect( xh, 0, 2, h );
            }

            g.setColor( Color.gray );
            double [] mb = config.calcContrastLine();
            int cx1 = 0;
            int cy1 = ( int )( mb[1] * h / 256.0 );
            int cx2 = ( int )( hist.length * w / 256.0 );
            int cy2 = ( int )( h / 256.0 * ( mb[0] * hist.length + mb[1] ) );
            g.drawLine( cx1, h - cy1, cx2, h - cy2 );
        }

        private void paintBin( JComponent c, Graphics2D g, int i, float xs,
            float ys, int width )
        {
            try
            {
                int x = ( int )( i * xs );
                int height = ( int )( ys * hist[i] );
                int y = ( int )( c.getHeight() - height );
                g.fillRect( x, y, width, height );
            }
            catch( ArrayIndexOutOfBoundsException ex )
            {
                ex.printStackTrace();
            }
        }

        public void setHistogram( int [] hist )
        {
            this.hist = hist;
            this.histMax = 0;

            for( int i = 0; i < hist.length; i++ )
            {
                histMax = Math.max( histMax, hist[i] );
            }
        }

        public void setData( HistogramConfig config )
        {
            this.config = config;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class HistMouseListener extends MouseAdapter
    {
        /**  */
        private final JPopupMenu menu;

        /**
         * @param view
         */
        public HistMouseListener( HistogramView view )
        {
            this.menu = new JPopupMenu();

            JMenuItem menuItem = new JMenuItem( "Reset" );
            menu.add( menuItem );
            menuItem.addActionListener( ( e ) -> view.resetHistogram() );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseClicked( MouseEvent e )
        {
            if( SwingUtilities.isRightMouseButton( e ) &&
                e.getClickCount() == 1 )
            {
                menu.show( e.getComponent(), e.getX(), e.getY() );
            }
        }
    }
}
