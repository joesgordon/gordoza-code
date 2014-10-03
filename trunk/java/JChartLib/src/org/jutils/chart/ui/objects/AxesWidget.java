package org.jutils.chart.ui.objects;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.data.ChartContext;
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
        this.domainLabel = new TextLabel();

        domainLabel.font = new Font( "Helvetica", Font.PLAIN, 12 );

        this.domainText = new TextWidget( domainLabel );

        this.rangeLabel = new TextLabel();

        rangeLabel.font = new Font( "Helvetica", Font.PLAIN, 12 );

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

            context.latchCoords();

            // LogUtils.printDebug( "axes: w: " + w + ", h: " + h );
            // LogUtils.printDebug( "axes: xr: " + context.getXRange() );
            // LogUtils.printDebug( "axes: xmin: " + context.xMin );
            // LogUtils.printDebug( "axes: xmax: " + context.xMax );
            // LogUtils.printDebug( "axes: width: " + context.width );

            // -----------------------------------------------------------------
            // Calculate ticks.
            // -----------------------------------------------------------------
            List<Tick> domainTicks = genDomainTicks( textSpace.left,
                context.width, chart.domainAxis.majorSectionCount,
                context.domain.primary );
            List<Tick> rangeTicks = genRangeTicks( textSpace.top,
                context.height, chart.rangeAxis.majorSectionCount,
                context.range.primary );
            Tick t;

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
            t = domainTicks.get( 0 );
            drawDomainLabel( g2d, t, textSpace.top + context.height + 2,
                textSpace.bottom );

            for( int i = 1; i < domainTicks.size() - 1; i++ )
            {
                t = domainTicks.get( i );

                g2d.drawLine( t.offset, textSpace.top, t.offset, textSpace.top +
                    MAJOR_TICK_LEN );
                g2d.drawLine( t.offset, textSpace.top + context.height -
                    MAJOR_TICK_LEN, t.offset, textSpace.top + context.height );

                drawDomainLabel( g2d, t, textSpace.top + context.height + 2,
                    textSpace.bottom );
            }

            t = domainTicks.get( domainTicks.size() - 1 );
            drawDomainLabel( g2d, t, textSpace.top + context.height + 2,
                textSpace.bottom );

            // -----------------------------------------------------------------
            // Draw range ticks and labels.
            // -----------------------------------------------------------------
            t = rangeTicks.get( 0 );
            drawRangeLabel( g2d, t, textSpace.left );

            for( int i = 1; i < chart.domainAxis.majorSectionCount; i++ )
            {
                t = rangeTicks.get( i );

                g2d.drawLine( textSpace.left, t.offset, textSpace.left +
                    MAJOR_TICK_LEN, t.offset );
                g2d.drawLine( textSpace.left + context.width, t.offset,
                    textSpace.left + context.width - MAJOR_TICK_LEN, t.offset );

                drawRangeLabel( g2d, t, textSpace.left );
            }
            t = rangeTicks.get( rangeTicks.size() - 1 );
            drawRangeLabel( g2d, t, textSpace.left );

            axesLayer.repaint = false;
        }

        axesLayer.paint( graphics, x, y );
    }

    /***************************************************************************
     * @param offset
     * @param dist
     * @param sectionCount
     * @param min
     * @param max
     * @return
     **************************************************************************/
    private static List<Tick> genDomainTicks( int offset, int dist,
        int sectionCount, IDimensionCoords coords )
    {
        List<Tick> ticks = new ArrayList<>();
        Span span = coords.getSpan();

        double sectionSize = coords.getSpan().range / sectionCount;

        ticks.add( new Tick( offset, span.min ) );

        for( int i = 1; i < sectionCount; i++ )
        {
            double d = span.min + i * sectionSize;
            int off = offset + coords.fromCoord( d );
            ticks.add( new Tick( off, d ) );
        }

        ticks.add( new Tick( offset + dist, span.max ) );

        return ticks;
    }

    /***************************************************************************
     * @param offset
     * @param dist
     * @param sectionCount
     * @param min
     * @param max
     * @return
     **************************************************************************/
    private static List<Tick> genRangeTicks( int offset, int dist,
        int sectionCount, IDimensionCoords coords )
    {
        List<Tick> ticks = new ArrayList<>();
        Span span = coords.getSpan();

        double sectionSize = coords.getSpan().range / sectionCount;

        ticks.add( new Tick( offset, span.max ) );

        for( int i = 1; i < sectionCount; i++ )
        {
            double d = span.max - i * sectionSize;
            int off = offset + coords.fromCoord( d );
            ticks.add( new Tick( off, d ) );
        }

        ticks.add( new Tick( offset + dist, span.min ) );

        return ticks;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Insets calculateLabelInsets()
    {
        Insets textSpace = new Insets( 0, 0, 0, 0 );

        domainLabel.text = String.format( "%.3f", context.primaryDomainSpan.min );
        Dimension xMinSize = domainText.calculateSize();
        domainLabel.text = String.format( "%.3f", context.primaryDomainSpan.max );
        Dimension xMaxSize = domainText.calculateSize();

        rangeLabel.text = String.format( "%.3f", context.primaryRangeSpan.min );
        Dimension yMinSize = rangeText.calculateSize();
        rangeLabel.text = String.format( "%.3f", context.primaryRangeSpan.max );
        Dimension yMaxSize = rangeText.calculateSize();

        textSpace.left = Math.max( xMinSize.width / 2, yMinSize.width ) + 4;
        textSpace.right = xMaxSize.width / 2;
        textSpace.bottom = Math.max( xMinSize.height, yMinSize.height / 2 ) + 4;
        textSpace.top = yMaxSize.width / 2;

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
     * @param y
     * @param h
     **************************************************************************/
    private void drawRangeLabel( Graphics2D g2d, Tick t, int w )
    {
        rangeLabel.text = String.format( "%.3f", t.value );
        Dimension d = rangeText.calculateSize();
        int tw = d.width;
        int h = d.height;
        rangeText.repaint();
        rangeText.draw( g2d, w - tw - 2, t.offset - h / 2, tw, h );
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
