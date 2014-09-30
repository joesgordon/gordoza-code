package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.chart.model.Chart;
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
    public Chart chart;
    /**  */
    private final BasicStroke majorStroke;
    /**  */
    private final BasicStroke gridStroke;
    /**  */
    private int weight;

    /***************************************************************************
     * 
     **************************************************************************/
    public AxesWidget()
    {
        this.weight = 2;
        this.axesLayer = new Layer2d();
        this.majorStroke = new BasicStroke( weight, BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND );
        final float dash1 [] = { 8.0f };
        this.gridStroke = new BasicStroke( 1.0f, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f );
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

            int w = width - weight / 2;
            int h = height - weight / 2;

            axesLayer.clear();

            // LogUtils.printDebug( "axes: w: " + w + ", h: " + h );

            if( chart.gridlinesVisible )
            {
                g2d.setColor( Color.lightGray );
                g2d.setStroke( gridStroke );

                for( int i = 1; i < chart.domainAxis.majorSectionCount; i++ )
                {
                    int tickx = ( int )( w * ( double )i / chart.domainAxis.majorSectionCount );

                    g2d.drawLine( tickx, 12, tickx, h - 12 );
                }

                for( int i = 1; i < chart.domainAxis.majorSectionCount; i++ )
                {
                    int ticky = ( int )( h * ( double )i / chart.rangeAxis.majorSectionCount );

                    g2d.drawLine( 12, ticky, w - 12, ticky );
                }
            }

            g2d.setColor( Color.black );
            g2d.setStroke( majorStroke );

            g2d.drawRect( 0, 0, w, h );

            for( int i = 1; i < chart.domainAxis.majorSectionCount; i++ )
            {
                int tickx = ( int )( w * ( double )i / chart.domainAxis.majorSectionCount );

                g2d.drawLine( tickx, 0, tickx, 12 );
                g2d.drawLine( tickx, h - 12, tickx, h );
            }

            for( int i = 1; i < chart.domainAxis.majorSectionCount; i++ )
            {
                int ticky = ( int )( h * ( double )i / chart.rangeAxis.majorSectionCount );

                g2d.drawLine( 0, ticky, 12, ticky );
                g2d.drawLine( w, ticky, w - 12, ticky );
            }

            axesLayer.repaint = false;
        }

        axesLayer.paint( graphics, x, y );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension calculateSize()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
