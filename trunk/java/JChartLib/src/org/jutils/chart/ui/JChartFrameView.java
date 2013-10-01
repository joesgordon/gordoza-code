package org.jutils.chart.ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;

import org.jutils.chart.series.LineSeries;
import org.jutils.chart.ui.objects.*;
import org.jutils.ui.model.IView;

public class JChartFrameView implements IView<JFrame>
{
    private final JFrame frame;

    public JChartFrameView()
    {
        this.frame = new JFrame();

        Java2dPanel mainPanel = new Java2dPanel();

        Chart chart = new Chart();

        chart.serieses.add( new Series( new LineSeries( 3000000, 1.0, 0.0,
            -5.0, 5.0 ) ) );

        chart.context.xMin = -5.0;
        chart.context.xRange = 10.0;
        chart.context.yMin = -5.0;
        chart.context.yRange = 10.0;

        mainPanel.setObject( chart );

        // mainPanel.addMouseMotionListener( new ChartMouseListenter( obj,
        // mainPanel ) );

        frame.setContentPane( mainPanel );
    }

    @Override
    public JFrame getView()
    {
        return frame;
    }

    private class ChartMouseListenter extends MouseMotionAdapter
    {
        private CircleMarker marker;
        private Java2dPanel panel;

        public ChartMouseListenter( CircleMarker obj, Java2dPanel panel )
        {
            this.marker = obj;
            this.panel = panel;
        }

        @Override
        public void mouseMoved( MouseEvent e )
        {
            Point p = e.getPoint();

            marker.x = p.x;
            marker.y = ( int )( panel.getHeight() - p.x * panel.getHeight() *
                1.0 / panel.getWidth() );

            if( p.x > ( panel.getWidth() / 2 ) )
            {
                marker.hasBorder = false;
            }
            else
            {
                marker.hasBorder = true;
            }

            panel.repaint();
        }
    }
}
