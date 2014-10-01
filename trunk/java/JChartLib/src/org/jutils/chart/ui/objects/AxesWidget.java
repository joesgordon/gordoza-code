package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.chart.data.ChartContext;
import org.jutils.chart.data.ScreenPlotTransformer;
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
    public final Layer2d axesLayer;
    /**  */
    public final ChartContext context;

    /**  */
    public Chart chart;
    /**  */
    public int plotX;
    /**  */
    public int plotY;
    /**  */
    public int plotWidth;
    /**  */
    public int plotHeight;

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
            BasicStroke.JOIN_MITER, 10.0f, new float[] { 8.0f }, 0.0f );
        this.domainLabel = new TextLabel();

        domainLabel.font = new Font( "Helvetica", Font.PLAIN, 12 );

        this.domainText = new TextWidget( domainLabel );
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
            ScreenPlotTransformer trans = new ScreenPlotTransformer( context );

            domainLabel.text = String.format( "%.3f", context.xMin );
            int textLeft = domainText.calculateSize().width / 2;

            domainLabel.text = String.format( "%.3f", context.xMax );
            int textRight = domainText.calculateSize().width / 2;

            int th = domainText.calculateSize().height + 4;

            context.height -= th;

            plotX = x + textLeft;
            plotY = y;
            plotWidth = width - weight / 2 - textLeft - textRight;
            plotHeight = height - weight / 2 - th;

            axesLayer.clear();

            // LogUtils.printDebug( "axes: w: " + w + ", h: " + h );

            if( chart.gridlinesVisible )
            {
                g2d.setColor( Color.lightGray );
                g2d.setStroke( gridStroke );

                for( int i = 1; i < chart.domainAxis.majorSectionCount; i++ )
                {
                    int tickx = textLeft +
                        ( int )( plotWidth * ( double )i / chart.domainAxis.majorSectionCount );

                    g2d.drawLine( tickx, 12, tickx, plotHeight - 12 );
                }

                for( int i = 1; i < chart.rangeAxis.majorSectionCount; i++ )
                {
                    int ticky = ( int )( plotHeight * ( double )i / chart.rangeAxis.majorSectionCount );

                    g2d.drawLine( textLeft + 12, ticky, textLeft + plotWidth -
                        12, ticky );
                }
            }

            g2d.setColor( Color.black );
            g2d.setStroke( majorStroke );

            g2d.drawRect( textLeft, 0, plotWidth, plotHeight );

            drawDomainLabel( trans, g2d, textLeft, plotHeight + 2, th );

            for( int i = 1; i < chart.domainAxis.majorSectionCount; i++ )
            {
                int tickx = textLeft +
                    ( int )( plotWidth * ( double )i / chart.domainAxis.majorSectionCount );

                g2d.drawLine( tickx, 0, tickx, 12 );
                g2d.drawLine( tickx, plotHeight - 12, tickx, plotHeight );

                drawDomainLabel( trans, g2d, tickx, plotHeight + 2, th );
            }

            drawDomainLabel( trans, g2d, plotWidth + textRight, plotHeight + 2,
                th );

            for( int i = 1; i < chart.domainAxis.majorSectionCount; i++ )
            {
                int ticky = ( int )( plotHeight * ( double )i / chart.rangeAxis.majorSectionCount );

                g2d.drawLine( textLeft, ticky, textLeft + 12, ticky );
                g2d.drawLine( textLeft + plotWidth, ticky, textLeft +
                    plotWidth - 12, ticky );
            }

            axesLayer.repaint = false;
        }

        axesLayer.paint( graphics, x, y );
    }

    /***************************************************************************
     * @param trans
     * @param g2d
     * @param x
     * @param y
     * @param h
     **************************************************************************/
    private void drawDomainLabel( ScreenPlotTransformer trans, Graphics2D g2d,
        int x, int y, int h )
    {
        double d = trans.fromScreenDomain( x );
        domainLabel.text = String.format( "%.3f", d );
        int tw = domainText.calculateSize().width;
        domainText.repaint();
        domainText.draw( g2d, x - tw / 2, y, tw, h );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension calculateSize()
    {
        return null;
    }
}
