package org.jutils.chart.ui.objects;

import java.awt.*;
import java.util.List;

import org.jutils.chart.data.*;
import org.jutils.chart.model.Chart;
import org.jutils.chart.model.TextLabel;
import org.jutils.chart.ui.IChartWidget;
import org.jutils.chart.ui.Layer2d;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AxesWidget implements IChartWidget
{
    /**  */
    public final Chart chart;
    /**  */
    public final ChartContext context;

    /**  */
    public final Layer2d axesLayer;

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
    private final TextWidget domainTitle;
    /**  */
    private final TextWidget rangeTitle;
    /**  */
    private final TextWidget sdomainTitle;
    /**  */
    private final TextWidget srangeTitle;

    /**  */
    private static final int MAJOR_TICK_LEN = 12;

    /***************************************************************************
     * @param context
     **************************************************************************/
    public AxesWidget( ChartContext context, Chart chart )
    {
        this.chart = chart;
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

        this.domainTitle = new TextWidget( chart.domainAxis.primary.title );
        this.sdomainTitle = new TextWidget( chart.domainAxis.secondary.title );

        this.rangeTitle = new TextWidget( chart.rangeAxis.primary.title,
            TextDirection.RIGHT );
        this.srangeTitle = new TextWidget( chart.rangeAxis.secondary.title,
            TextDirection.LEFT );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension calculateSize( Dimension canvasSize )
    {
        return null;
    }

    /***************************************************************************
     * Draws in the following order <ol> <li>chart legend</li> <li>grid
     * lines</li> <li>axes</li> <li>tick labels</li> <li>ticks and
     * labels</li></ol>
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point location, Dimension size )
    {
        Point p = new Point( location );
        Dimension d = new Dimension( size );
        Insets textSpace = calculateLabelInsets( size );

        // ---------------------------------------------------------------------
        // Draw secondary domain title
        // ---------------------------------------------------------------------
        if( chart.domainAxis.secondary.title.visible &&
            context.domain.secondary != null )
        {
            d = sdomainTitle.calculateSize( size );

            d.width = size.width - ( textSpace.left + textSpace.right );

            p.x = textSpace.left + location.x;
            p.y = location.y;

            sdomainTitle.draw( graphics, p, d );

            location.y += d.height;
            size.height -= d.height;
        }

        // ---------------------------------------------------------------------
        // Draw primary domain title
        // ---------------------------------------------------------------------
        if( chart.domainAxis.primary.title.visible )
        {
            d = domainTitle.calculateSize( size );

            d.width = size.width - ( textSpace.left + textSpace.right );

            p.x = textSpace.left + location.x;
            p.y = location.y + size.height - d.height;

            domainTitle.draw( graphics, p, d );

            // LogUtils.printDebug( "Label is: " + domainTitle.label.text );

            size.height -= d.height;
        }

        // ---------------------------------------------------------------------
        // Draw secondary range title
        // ---------------------------------------------------------------------
        if( chart.rangeAxis.secondary.title.visible &&
            context.range.secondary != null )
        {
            d = srangeTitle.calculateSize( size );

            int h = size.height - ( textSpace.top + textSpace.bottom );

            p.x = location.x + size.width - d.width;
            p.y = location.y + textSpace.top + ( h - d.width ) / 2;

            d.height = h;

            srangeTitle.draw( graphics, p, d );

            size.width -= d.width;
        }

        // ---------------------------------------------------------------------
        // Draw primary range title
        // ---------------------------------------------------------------------
        if( chart.rangeAxis.primary.title.visible )
        {
            d = rangeTitle.calculateSize( size );

            int h = size.height - ( textSpace.top + textSpace.bottom );

            p.x = location.x;
            p.y = location.y + textSpace.top + ( h - d.width ) / 2;

            d.height = h;

            rangeTitle.draw( graphics, p, d );

            location.x += d.width;
            size.width -= d.width;
        }

        drawTicksAndAxes( graphics, location, size, textSpace );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void repaint()
    {
        axesLayer.repaint = true;

        domainTitle.repaint();
        sdomainTitle.repaint();
        rangeTitle.repaint();
        srangeTitle.repaint();
    }

    /***************************************************************************
     * @param graphics
     * @param location
     * @param size
     * @param textSpace
     **************************************************************************/
    private void drawTicksAndAxes( Graphics2D graphics, Point location,
        Dimension size, Insets textSpace )
    {
        int x = location.x;
        int y = location.y;
        int width = size.width;
        int height = size.height;

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
            // Calculate context
            // -----------------------------------------------------------------
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
            TickGen gen = new TickGen();
            List<Tick> domainTicks = gen.genTicks( textSpace.left,
                context.width, context.domain.primary, false );
            List<Tick> secDomainTicks = gen.genTicks( textSpace.left,
                context.width, context.domain.secondary, false );

            List<Tick> rangeTicks = gen.genTicks( textSpace.top,
                context.height, context.range.primary, true );
            List<Tick> secRangeTicks = gen.genTicks( textSpace.top,
                context.height, context.range.secondary, true );

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

                for( int i = 0; i < domainTicks.size(); i++ )
                {
                    t = domainTicks.get( i );

                    g2d.drawLine( t.offset, textSpace.top, t.offset,
                        textSpace.top + context.height );
                }

                for( int i = 0; i < rangeTicks.size(); i++ )
                {
                    t = rangeTicks.get( i );

                    g2d.drawLine( textSpace.left, t.offset, textSpace.left +
                        context.width, t.offset );
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
            // Draw major ticks.
            // -----------------------------------------------------------------
            drawMajorTicks( g2d, domainTicks, rangeTicks, secDomainTicks,
                secRangeTicks, textSpace );

            // -----------------------------------------------------------------
            // Draw domain labels.
            // -----------------------------------------------------------------
            drawDomainLabels( g2d, domainTicks, textSpace.top + context.height +
                2, textSpace.bottom, size );

            // -----------------------------------------------------------------
            // Draw secondary range ticks and labels.
            // -----------------------------------------------------------------
            if( !secDomainTicks.isEmpty() )
            {
                drawDomainLabels( g2d, secDomainTicks, 0, textSpace.top, size );
            }

            // -----------------------------------------------------------------
            // Draw range ticks and labels.
            // -----------------------------------------------------------------
            drawRangeLabels( g2d, rangeTicks, -2, textSpace.left, false, size );

            // -----------------------------------------------------------------
            // Draw secondary range ticks and labels.
            // -----------------------------------------------------------------
            if( !secRangeTicks.isEmpty() )
            {
                drawRangeLabels( g2d, secRangeTicks, width - textSpace.right +
                    2, textSpace.right, true, size );
            }

            axesLayer.repaint = false;
        }

        axesLayer.paint( graphics, x, y );
    }

    /***************************************************************************
     * @param g2d
     * @param domainTicks
     * @param secRangeTicks
     * @param secDomainTicks
     * @param textSpace
     **************************************************************************/
    private void drawMajorTicks( Graphics2D g2d, List<Tick> domainTicks,
        List<Tick> rangeTicks, List<Tick> secDomainTicks,
        List<Tick> secRangeTicks, Insets textSpace )
    {
        secDomainTicks = secDomainTicks.isEmpty() ? domainTicks
            : secDomainTicks;
        secRangeTicks = secRangeTicks.isEmpty() ? rangeTicks : secRangeTicks;

        for( Tick t : domainTicks )
        {
            g2d.drawLine( t.offset, textSpace.top + context.height -
                MAJOR_TICK_LEN, t.offset, textSpace.top + context.height );
        }

        for( Tick t : secDomainTicks )
        {
            g2d.drawLine( t.offset, textSpace.top, t.offset, textSpace.top +
                MAJOR_TICK_LEN );
        }

        for( Tick t : rangeTicks )
        {
            g2d.drawLine( textSpace.left, t.offset, textSpace.left +
                MAJOR_TICK_LEN, t.offset );
        }

        for( Tick t : secRangeTicks )
        {
            g2d.drawLine( textSpace.left + context.width, t.offset,
                textSpace.left + context.width - MAJOR_TICK_LEN, t.offset );
        }
    }

    /***************************************************************************
     * @param g2d
     * @param ticks
     * @param y
     * @param h
     **************************************************************************/
    private void drawDomainLabels( Graphics2D g2d, List<Tick> ticks, int y,
        int h, Dimension canvasSize )
    {
        for( Tick t : ticks )
        {
            drawDomainLabel( g2d, t, y, h, canvasSize );
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
        int w, boolean leftAlign, Dimension canvasSize )
    {
        for( Tick t : ticks )
        {
            drawRangeLabel( g2d, t, x, w, leftAlign, canvasSize );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Insets calculateLabelInsets( Dimension canvasSize )
    {
        Insets textSpace = new Insets( 0, 0, 0, 0 );

        Bounds b = context.getBounds();

        Dimension dMinSize;
        Dimension dMaxSize;
        Dimension rMinSize;
        Dimension rMaxSize;

        domainLabel.text = getTickText( b.primaryDomainSpan.min );
        dMinSize = domainText.calculateSize( canvasSize );
        domainLabel.text = getTickText( b.primaryDomainSpan.max );
        dMaxSize = domainText.calculateSize( canvasSize );

        rangeLabel.text = getTickText( b.primaryRangeSpan.min );
        rMinSize = rangeText.calculateSize( canvasSize );
        rangeLabel.text = getTickText( b.primaryRangeSpan.max );
        rMaxSize = rangeText.calculateSize( canvasSize );

        textSpace.left = Math.max( dMinSize.width / 2, rMaxSize.width );
        textSpace.left = Math.max( textSpace.left, rMinSize.width );
        textSpace.right = dMaxSize.width / 2;
        textSpace.bottom = Math.max( dMinSize.height, rMinSize.height / 2 );
        textSpace.top = rMaxSize.height / 2;

        if( b.secondaryDomainSpan != null )
        {
            domainLabel.text = getTickText( b.secondaryDomainSpan.min );
            dMinSize = domainText.calculateSize( canvasSize );
            domainLabel.text = getTickText( b.secondaryDomainSpan.max );
            dMaxSize = domainText.calculateSize( canvasSize );

            textSpace.left = Math.max( textSpace.left, dMinSize.width / 2 );
            textSpace.right = Math.max( textSpace.right, dMaxSize.width / 2 );
            textSpace.top = Math.max( textSpace.top, dMinSize.height );

            textSpace.top += 4;
        }

        if( b.secondaryRangeSpan != null )
        {
            rangeLabel.text = getTickText( b.secondaryRangeSpan.min );
            rMinSize = rangeText.calculateSize( canvasSize );
            rangeLabel.text = getTickText( b.secondaryRangeSpan.max );
            rMaxSize = rangeText.calculateSize( canvasSize );

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
    private void drawDomainLabel( Graphics2D g2d, Tick t, int y, int h,
        Dimension canvasSize )
    {
        domainLabel.text = t.label;
        int tw = domainText.calculateSize( canvasSize ).width;
        Point p = new Point( t.offset - tw / 2, y );
        Dimension d = new Dimension( tw, h );
        domainText.repaint();
        domainText.draw( g2d, p, d );
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public static String getTickText( double value )
    {
        // double abs = Math.abs( value );
        // boolean useScientific = abs != 0.0 && ( abs > 999999999 || abs <
        // 0.001 );
        // String fmt = useScientific ? "%.10E" : "%.3f";
        //
        // return String.format( fmt, value );

        return String.format( "%.3f", value );
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
        boolean leftAlign, Dimension canvasSize )
    {
        rangeLabel.text = t.label;
        Dimension d = rangeText.calculateSize( canvasSize );
        int tw = d.width;
        int h = d.height;
        int tx = leftAlign ? x : x + w - tw;
        Point p = new Point( tx, t.offset - h / 2 );

        rangeText.repaint();
        rangeText.draw( g2d, p, d );
    }
}
