package org.jutils.chart.ui.objects;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.data.*;
import org.jutils.chart.data.ChartContext.IDimensionCoords;
import org.jutils.chart.model.*;
import org.jutils.chart.ui.IChartWidget;
import org.jutils.chart.ui.Layer2d;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AxesWidget implements IChartWidget
{
    /**  */
    public final Layer2d axesLayer;
    /**  */
    public final ChartContext context;

    /**  */
    public Chart chart;

    /**  */
    private final BasicStroke majorStroke;
    /**  */
    private final BasicStroke gridStroke;
    /**  */
    private int weight;
    /**  */
    private final TextLabel domainLabel;
    /**  */
    private final TextWidget domainText;
    /**  */
    private final TextLabel rangeLabel;
    /**  */
    private final TextWidget rangeText;

    /**  */
    private static final int MAJOR_TICK_LEN = 12;

    /***************************************************************************
     * @param context
     **************************************************************************/
    public AxesWidget( ChartContext context )
    {
        this.context = context;
        this.weight = 2;
        this.axesLayer = new Layer2d();
        this.majorStroke = new BasicStroke( weight, BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND );
        this.gridStroke = new BasicStroke( 1.0f, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 10.0f, new float[] { 2.0f }, 0.0f );

        this.domainLabel = new TextLabel(
            new Font( "Helvetica", Font.PLAIN, 12 ) );
        this.domainText = new TextWidget( domainLabel );

        this.rangeLabel = new TextLabel( new Font( "Helvetica", Font.PLAIN, 12 ) );
        this.rangeText = new TextWidget( rangeLabel );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, int x, int y, int width, int height )
    {
        // LogUtils.printDebug( "axes: w: " + width + ", h: " + height );

        // ---------------------------------------------------------------------
        // Draw axes layer.
        // ---------------------------------------------------------------------
        if( axesLayer.repaint )
        {
            Graphics2D g2d = axesLayer.setSize( width, height );

            axesLayer.clear();

            // g2d.setColor( Color.red );
            // g2d.setStroke( new BasicStroke( 1.0f ) );
            // g2d.drawRect( 0, 0, width - 1, height - 1 );

            // -----------------------------------------------------------------
            // Calculate label widths
            // -----------------------------------------------------------------
            Insets textSpace = calculateLabelInsets();

            context.x = x + textSpace.left;
            context.y = y + textSpace.top;

            context.width = width - weight / 2 - textSpace.left -
                textSpace.right;
            context.height = height - weight / 2 - textSpace.top -
                textSpace.bottom;

            context.width = Math.max( 0, context.width );
            context.height = Math.max( 0, context.height );

            context.latchCoords();

            // LogUtils.printDebug( "axes: w: " + w + ", h: " + h );
            // LogUtils.printDebug( "axes: xr: " + context.getXRange() );
            // LogUtils.printDebug( "axes: xmin: " + context.xMin );
            // LogUtils.printDebug( "axes: xmax: " + context.xMax );
            // LogUtils.printDebug( "axes: width: " + context.width );

            // -----------------------------------------------------------------
            // Calculate ticks.
            // -----------------------------------------------------------------
            List<Tick> domainTicks = genTicks( textSpace.left, context.width,
                chart.domainAxis.majorSectionCount, context.domain.primary,
                false );
            List<Tick> rangeTicks = genTicks( textSpace.top, context.height,
                chart.rangeAxis.majorSectionCount, context.range.primary, true );
            List<Tick> secDomainTicks = genTicks( textSpace.left,
                context.width, chart.domainAxis.majorSectionCount,
                context.domain.secondary, false );
            List<Tick> secRangeTicks = genTicks( textSpace.top, context.height,
                chart.rangeAxis.majorSectionCount, context.range.secondary,
                true );
            Tick t;

            // Collections.reverse( rangeTicks );
            // Collections.reverse( secRangeTicks );

            // -----------------------------------------------------------------
            // Draw grid lines.
            // -----------------------------------------------------------------
            if( chart.options.gridlinesVisible )
            {
                g2d.setColor( Color.lightGray );
                g2d.setStroke( gridStroke );

                for( int i = 1; i < domainTicks.size() - 1; i++ )
                {
                    t = domainTicks.get( i );
                    g2d.drawLine( t.offset, textSpace.top + MAJOR_TICK_LEN,
                        t.offset, textSpace.top + context.height -
                            MAJOR_TICK_LEN );
                }

                for( int i = 1; i < rangeTicks.size() - 1; i++ )
                {
                    t = rangeTicks.get( i );

                    g2d.drawLine( textSpace.left + MAJOR_TICK_LEN, t.offset,
                        textSpace.left + context.width - MAJOR_TICK_LEN,
                        t.offset );
                }
            }

            // -----------------------------------------------------------------
            // Draw axes.
            // -----------------------------------------------------------------
            g2d.setColor( Color.black );
            g2d.setStroke( majorStroke );

            g2d.drawRect( textSpace.left, textSpace.top, context.width,
                context.height );

            // -----------------------------------------------------------------
            // Draw domain ticks and labels.
            // -----------------------------------------------------------------
            drawDomainTicks( g2d, domainTicks, textSpace );

            drawDomainLabels( g2d, domainTicks, textSpace.top + context.height +
                2, textSpace.bottom );

            // -----------------------------------------------------------------
            // Draw secondary range ticks and labels.
            // -----------------------------------------------------------------
            if( !secDomainTicks.isEmpty() )
            {
                drawDomainLabels( g2d, secDomainTicks, 0, textSpace.top );
            }

            // -----------------------------------------------------------------
            // Draw range ticks and labels.
            // -----------------------------------------------------------------
            drawRangeTicks( g2d, rangeTicks, textSpace );

            drawRangeLabels( g2d, rangeTicks, -2, textSpace.left, false );

            // -----------------------------------------------------------------
            // Draw secondary range ticks and labels.
            // -----------------------------------------------------------------
            if( !secRangeTicks.isEmpty() )
            {
                drawRangeLabels( g2d, secRangeTicks, width - textSpace.right +
                    2, textSpace.right, true );
            }

            axesLayer.repaint = false;
        }

        axesLayer.paint( graphics, x, y );
    }

    /***************************************************************************
     * @param g2d
     * @param ticks
     * @param textSpace
     **************************************************************************/
    private void drawDomainTicks( Graphics2D g2d, List<Tick> ticks,
        Insets textSpace )
    {
        Tick t;

        for( int i = 1; i < ticks.size() - 1; i++ )
        {
            t = ticks.get( i );

            g2d.drawLine( t.offset, textSpace.top, t.offset, textSpace.top +
                MAJOR_TICK_LEN );
            g2d.drawLine( t.offset, textSpace.top + context.height -
                MAJOR_TICK_LEN, t.offset, textSpace.top + context.height );
        }
    }

    /***************************************************************************
     * @param g2d
     * @param ticks
     * @param y
     * @param h
     **************************************************************************/
    private void drawDomainLabels( Graphics2D g2d, List<Tick> ticks, int y,
        int h )
    {
        for( Tick t : ticks )
        {
            drawDomainLabel( g2d, t, y, h );
        }
    }

    /***************************************************************************
     * @param g2d
     * @param ticks
     * @param textSpace
     **************************************************************************/
    private void drawRangeTicks( Graphics2D g2d, List<Tick> ticks,
        Insets textSpace )
    {
        Tick t;

        for( int i = 1; i < ticks.size() - 1; i++ )
        {
            t = ticks.get( i );

            g2d.drawLine( textSpace.left, t.offset, textSpace.left +
                MAJOR_TICK_LEN, t.offset );
            g2d.drawLine( textSpace.left + context.width, t.offset,
                textSpace.left + context.width - MAJOR_TICK_LEN, t.offset );
        }
    }

    /***************************************************************************
     * @param g2d
     * @param ticks
     * @param x
     * @param w
     * @param leftAlign
     **************************************************************************/
    private void drawRangeLabels( Graphics2D g2d, List<Tick> ticks, int x,
        int w, boolean leftAlign )
    {
        for( Tick t : ticks )
        {
            drawRangeLabel( g2d, t, x, w, leftAlign );
        }
    }

    /***************************************************************************
     * @param offset
     * @param dist
     * @param sectionCount
     * @param min
     * @param max
     * @return
     **************************************************************************/
    private static List<Tick> genTicks( int offset, int dist, int sectionCount,
        IDimensionCoords coords, boolean range )
    {
        List<Tick> ticks = new ArrayList<>();

        if( coords == null )
        {
            return ticks;
        }

        Span span = coords.getSpan();

        ticks.add( new Tick( offset, range ? span.max : span.min ) );

        for( int i = 1; i < sectionCount; i++ )
        {
            double d = range ? span.max - i * span.range / sectionCount
                : span.min + i * span.range / sectionCount;
            int off = offset + coords.fromCoord( d );
            ticks.add( new Tick( off, d ) );
        }

        ticks.add( new Tick( offset + dist, range ? span.min : span.max ) );

        return ticks;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Insets calculateLabelInsets()
    {
        Insets textSpace = new Insets( 0, 0, 0, 0 );

        Bounds b = context.getBounds();

        Dimension dMinSize;
        Dimension dMaxSize;
        Dimension rMinSize;
        Dimension rMaxSize;

        domainLabel.text = String.format( "%.3f", b.primaryDomainSpan.min );
        dMinSize = domainText.calculateSize();
        domainLabel.text = String.format( "%.3f", b.primaryDomainSpan.max );
        dMaxSize = domainText.calculateSize();

        rangeLabel.text = String.format( "%.3f", b.primaryRangeSpan.min );
        rMinSize = rangeText.calculateSize();
        rangeLabel.text = String.format( "%.3f", b.primaryRangeSpan.max );
        rMaxSize = rangeText.calculateSize();

        textSpace.left = Math.max( dMinSize.width / 2, rMaxSize.width );
        textSpace.left = Math.max( textSpace.left, rMinSize.width );
        textSpace.right = dMaxSize.width / 2;
        textSpace.bottom = Math.max( dMinSize.height, rMinSize.height / 2 );
        textSpace.top = rMaxSize.height / 2;

        if( b.secondaryDomainSpan != null )
        {
            domainLabel.text = String.format( "%.3f", b.secondaryDomainSpan.min );
            dMinSize = domainText.calculateSize();
            domainLabel.text = String.format( "%.3f", b.secondaryDomainSpan.max );
            dMaxSize = domainText.calculateSize();

            textSpace.left = Math.max( textSpace.left, dMinSize.width / 2 );
            textSpace.right = Math.max( textSpace.right, dMaxSize.width / 2 );
            textSpace.top = Math.max( textSpace.top, dMinSize.height );

            textSpace.top += 4;
        }

        if( b.secondaryRangeSpan != null )
        {
            rangeLabel.text = String.format( "%.3f", b.secondaryRangeSpan.min );
            rMinSize = rangeText.calculateSize();
            rangeLabel.text = String.format( "%.3f", b.secondaryRangeSpan.max );
            rMaxSize = rangeText.calculateSize();

            textSpace.bottom = Math.max( textSpace.bottom, rMinSize.height / 2 );
            textSpace.right = Math.max( textSpace.right, rMinSize.width );
            textSpace.right = Math.max( textSpace.right, rMaxSize.width );
            textSpace.top = Math.max( textSpace.top, rMaxSize.height / 2 );

            textSpace.right += 4;
        }

        textSpace.left += 4;
        textSpace.bottom += 4;

        return textSpace;
    }

    /***************************************************************************
     * @param trans
     * @param g2d
     * @param x
     * @param y
     * @param h
     **************************************************************************/
    private void drawDomainLabel( Graphics2D g2d, Tick t, int y, int h )
    {
        domainLabel.text = String.format( "%.3f", t.value );
        int tw = domainText.calculateSize().width;
        domainText.repaint();
        domainText.draw( g2d, t.offset - tw / 2, y, tw, h );
    }

    /***************************************************************************
     * @param trans
     * @param g2d
     * @param x
     * @param leftAlign
     * @param y
     * @param h
     **************************************************************************/
    private void drawRangeLabel( Graphics2D g2d, Tick t, int x, int w,
        boolean leftAlign )
    {
        rangeLabel.text = String.format( "%.3f", t.value );
        Dimension d = rangeText.calculateSize();
        int tw = d.width;
        int h = d.height;
        int tx = leftAlign ? x : x + w - tw;
        rangeText.repaint();
        rangeText.draw( g2d, tx, t.offset - h / 2, tw, h );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension calculateSize()
    {
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class Tick
    {
        public final int offset;
        public final double value;

        public Tick( int off, double v )
        {
            this.offset = off;
            this.value = v;
        }
    }
}
