package org.jutils.drawing;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.LineBorder;

import org.jutils.drawing.HistogramView.HistogramConfig;
import org.jutils.ui.*;
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
    public final int pixelDepth;
    /**  */
    public final int maxPixelValue;
    /**
     * The maximum number of bins allowed for the provided {@link #pixelDepth}.
     */
    public final int maxBinCount;

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
    private final JSlider brightnessBar;
    /**  */
    private final IntegerFormField brightnessField;

    /**  */
    private final JSlider contrastBar;
    /**  */
    private final IntegerFormField contrastField;

    /**  */
    private HistogramConfig config;
    /**  */
    private boolean isUpdating;

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
        this.pixelDepth = pixelDepth;
        this.maxPixelValue = ( int )Math.pow( 2, pixelDepth ) - 1;
        this.maxBinCount = pixelDepth > 16 ? MAX_BIN_COUNT : maxPixelValue;

        this.paintable = new HistogramPaintable();
        this.histComp = new PaintingComponent( paintable );

        this.lowButton = new LedColorButton( DEFAULT_LOW_COLOR );
        this.lowBar = new JSlider( JSlider.HORIZONTAL, 0, maxPixelValue, 0 );
        this.lowField = new IntegerFormField( "Low Threshold", null, 6, 0,
            maxPixelValue );

        this.highButton = new LedColorButton( DEFAULT_HIGH_COLOR );
        this.highBar = new JSlider( JSlider.HORIZONTAL, 0, maxPixelValue, 0 );
        this.highField = new IntegerFormField( "High Threshold", null, 6, 0,
            maxPixelValue );

        this.binCountBar = new JSlider( JSlider.HORIZONTAL, 1, maxPixelValue,
            1 );
        this.binCountField = new IntegerFormField( "Bin Count", null, 6, 0,
            maxBinCount );

        this.brightnessBar = new JSlider( JSlider.HORIZONTAL, 0, maxPixelValue,
            0 );
        this.brightnessField = new IntegerFormField( "Brightness", null, 6, 0,
            maxBinCount );

        this.contrastBar = new JSlider( JSlider.HORIZONTAL, 0, maxPixelValue,
            0 );
        this.contrastField = new IntegerFormField( "Contrast", null, 6, 0,
            maxBinCount );

        this.view = createView();

        this.config = null;
        this.isUpdating = false;

        HistogramConfig histCfg = new HistogramConfig();
        histCfg.low = 0;
        histCfg.high = maxPixelValue;
        setData( histCfg );

        lowButton.setUpdater( ( v ) -> {
            config.lowColor = v;
            histComp.repaint();
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

        highBar.addChangeListener( ( e ) -> {
            if( !isUpdating )
            {
                setHighValue( false, true, highBar.getValue() );
            }
        } );
        highField.setUpdater( ( v ) -> {
            setHighValue( true, false, v );
        } );
        highButton.setUpdater( ( v ) -> {
            config.highColor = v;
            histComp.repaint();
        } );

        binCountBar.addChangeListener( ( e ) -> {
            if( !isUpdating )
            {
                isUpdating = true;
                config.binCount = binCountBar.getValue();
                binCountField.setValue( config.binCount );
                isUpdating = false;
            }
        } );
        binCountField.setUpdater( ( v ) -> {
            if( !isUpdating )
            {
                isUpdating = true;
                config.binCount = v;
                binCountBar.setValue( config.binCount );
                isUpdating = false;
            }
        } );

        brightnessBar.addChangeListener( ( e ) -> {
            if( !isUpdating )
            {
                isUpdating = true;
                config.brightness = brightnessBar.getValue();
                brightnessField.setValue( config.brightness );
                isUpdating = false;
            }
        } );
        brightnessField.setUpdater( ( v ) -> {
            if( !isUpdating )
            {
                isUpdating = true;
                config.brightness = v;
                brightnessBar.setValue( config.brightness );
                isUpdating = false;
            }
        } );

        contrastBar.addChangeListener( ( e ) -> {
            if( !isUpdating )
            {
                isUpdating = true;
                config.contrast = contrastBar.getValue();
                contrastField.setValue( config.contrast );
                isUpdating = false;
            }
        } );
        contrastField.setUpdater( ( v ) -> {
            if( !isUpdating )
            {
                isUpdating = true;
                config.contrast = v;
                contrastBar.setValue( config.contrast );
                isUpdating = false;
            }
        } );
    }

    private void setLowValue( boolean setBar, boolean setField, int value )
    {
        config.low = value;

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

        if( config.low > config.high )
        {
            setHighValue( true, true, value );
        }
        histComp.repaint();
    }

    private void setHighValue( boolean setBar, boolean setField, int value )
    {
        config.high = value;

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

        if( config.low > config.high )
        {
            setLowValue( true, true, value );
        }
        histComp.repaint();
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
        panel.add( createThreshPanel(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createThreshPanel()
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

        constraints = new GridBagConstraints( 0, 4, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( fm, 0, 0, fm ), 0, 0 );
        panel.add( new JLabel( "Brightness: " ), constraints );

        brightnessBar.setPreferredSize( lowBar.getPreferredSize() );

        constraints = new GridBagConstraints( 1, 4, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( fm, 0, 0, fm ), 0, 0 );
        panel.add( brightnessBar, constraints );

        constraints = new GridBagConstraints( 2, 4, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( fm, 0, 0, 0 ), 0, 0 );
        panel.add( brightnessField.getView(), constraints );

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

        lowButton.setData( config.lowColor );
        lowBar.setValue( config.low );
        lowField.setValue( config.low );

        highButton.setData( config.highColor );
        highBar.setValue( config.high );
        highField.setValue( config.high );

        binCountBar.setValue( config.binCount );
        binCountField.setValue( config.binCount );

        brightnessBar.setValue( config.brightness );
        brightnessField.setValue( config.brightness );

        contrastBar.setValue( config.contrast );
        contrastField.setValue( config.contrast );

        paintable.setData( config );
    }

    /***************************************************************************
     * The number of pixels found with each value from 0 to
     * {@link #maxBinCount}.
     * @param hist
     **************************************************************************/
    public void setHistogram( int [] hist )
    {
        paintable.setHistogram( hist );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void resetHistogram()
    {
        config.binCount = 255;
        config.low = 0;
        config.high = 255;
        config.contrast = 128;
        config.brightness = 128;

        setData( config );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class HistogramConfig
    {
        public int low;
        public Color lowColor;

        public int high;
        public Color highColor;

        public int binCount;

        public int brightness;
        public int contrast;

        public HistogramConfig()
        {
            this.low = 0;
            this.lowColor = Color.blue;

            this.high = 255 - 1;
            this.highColor = Color.red;

            this.binCount = 255;

            this.brightness = 128;
            this.contrast = 128;
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

            double sig = 34;
            double mean = 128;
            for( int i = 0; i < hist.length; i++ )
            {
                double cnt = 1.0 / ( sig * Math.sqrt( 2 * Math.PI ) ) *
                    Math.exp( -0.5 * Math.pow( ( i - mean ) / sig, 2 ) );
                // LogUtils.printDebug( "hist[%d]: %f", i, cnt );
                hist[i] = ( int )( cnt * 80000 );
            }

            this.setHistogram( hist );

            // LogUtils.printDebug( "here" );
        }

        @Override
        public void paint( JComponent c, Graphics2D g )
        {
            g.setBackground( Color.white );
            g.clearRect( 0, 0, c.getWidth(), c.getHeight() );

            float xs = c.getWidth() / ( float )hist.length;
            int width = 1 + ( int )xs;

            float ys = ( c.getHeight() - 4 ) / ( float )histMax;

            if( config.low > 0 )
            {
                g.setColor( config.lowColor );

                for( int i = 0; i < config.low; i++ )
                {
                    paintBin( c, g, i, xs, ys, width );
                }
            }

            g.setColor( Color.black );
            for( int i = config.low; i <= config.high; i++ )
            {
                paintBin( c, g, i, xs, ys, width );
            }

            if( config.high < 255 )
            {
                g.setColor( config.highColor );
                for( int i = config.high + 1; i < hist.length; i++ )
                {
                    paintBin( c, g, i, xs, ys, width );
                }
            }

            if( config.low > 0 )
            {
                int w = ( int )( config.low * xs );

                g.setColor( config.lowColor );
                g.fillRect( 0, c.getHeight() - 3, w, 2 );
                g.fillRect( w, 0, 2, c.getHeight() );
            }

            if( config.high < 255 )
            {
                int w = ( int )( config.high * xs );

                g.setColor( config.highColor );
                g.fillRect( w, 0, c.getWidth() - w, 2 );
                g.fillRect( w, 0, 2, c.getHeight() );
            }
        }

        private void paintBin( JComponent c, Graphics2D g, int i, float xs,
            float ys, int width )
        {
            int x = ( int )( i * xs );
            int height = ( int )( ys * hist[i] );
            int y = ( int )( c.getHeight() - height );
            g.fillRect( x, y, width, height );
        }

        public void setHistogram( int [] hist )
        {
            this.hist = hist;

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
