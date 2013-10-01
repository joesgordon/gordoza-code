package org.jutils.chart.ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;

import javax.swing.JComponent;

import org.jutils.chart.ChartUtils;
import org.jutils.chart.ISeries;
import org.jutils.chart.ui.objects.Chart;
import org.jutils.chart.ui.objects.Series;
import org.jutils.ui.model.IView;

public class ChartView implements IView<JComponent>
{
    private final Java2dPanel mainPanel;
    private final Chart chart;

    public ChartView()
    {
        this.mainPanel = new Java2dPanel();
        this.chart = new Chart();

        Series s;

        s = new Series( ChartUtils.createLineSeries( 1000000, 1.0, 0.0, -5.0,
            5.0 ) );
        chart.serieses.add( s );

        s = new Series(
            ChartUtils.createSinSeries( 100000, 1.0, 0.0, -5.0, 5.0 ) );
        s.marker.setColor( new Color( 0x339933 ) );
        s.highlightMarker.setColor( new Color( 0x339933 ) );
        s.highlightMarker.setBorderColor( new Color( 0x339933 ^ -1 ) );
        chart.serieses.add( s );

        s = new Series( ChartUtils.createLineSeries( 100000, -0.750, 0.0, -5.0,
            5.0 ) );
        s.marker.setColor( new Color( 0xFF9933 ) );
        s.highlightMarker.setColor( new Color( 0xFF9933 ) );
        s.highlightMarker.setBorderColor( new Color( 0xFF9933 ^ -1 ) );
        chart.serieses.add( s );

        chart.context.xMin = -5.0;
        chart.context.xRange = 10.0;
        chart.context.yMin = -5.0;
        chart.context.yRange = 10.0;

        mainPanel.setObject( chart );

        mainPanel.addComponentListener( new ChartComponentListener( this ) );
        mainPanel.addMouseMotionListener( new ChartMouseListenter( this ) );
    }

    @Override
    public JComponent getView()
    {
        return mainPanel;
    }

    private static int findX( ISeries series, double x )
    {
        int lo = 0;
        int hi = series.getCount() - 1;
        int value = -1;

        if( series.getX( hi ) < x )
        {
            value = hi;
        }
        else if( x < series.getX( 0 ) )
        {
            value = 0;
        }
        else
        {
            while( value < 0 && lo <= hi )
            {
                // Key is in a[lo..hi] or not present.
                int mid = lo + ( hi - lo ) / 2;
                double x1 = series.getX( mid );
                double x2 = series.getX( mid + 1 );

                if( x < x1 )
                {
                    hi = mid - 1;
                }
                else if( x2 < x )
                {
                    lo = mid + 1;
                }
                else
                {
                    value = mid;
                }
            }
        }

        if( value < ( series.getCount() - 1 ) &&
            ( x - series.getX( value ) ) > ( series.getX( value + 1 ) - x ) )
        {
            value++;
        }

        return value;
    }

    private static class ChartMouseListenter extends MouseMotionAdapter
    {
        private final ChartView view;

        public ChartMouseListenter( ChartView view )
        {
            this.view = view;
        }

        @Override
        public void mouseMoved( MouseEvent e )
        {
            Point p;
            int mx = e.getPoint().x;
            double x;
            int idx;
            int w = view.mainPanel.getWidth();
            int h = view.mainPanel.getHeight();

            // System.out.println( "here: " + mx );

            for( Series s : view.chart.serieses )
            {
                x = ChartUtils.coordsToValueX( mx, w, view.chart.context );
                idx = ChartView.findX( s.data, x );
                p = ChartUtils.valueToChartCoords( s.data.getX( idx ),
                    s.data.getY( idx ), w, h, view.chart.context );

                // p.x = mx;

                s.highlightMarker.setLocation( p );
            }

            view.chart.highlightLayer.repaint = true;

            view.mainPanel.repaint();
        }
    }

    private static class ChartComponentListener implements ComponentListener
    {
        private final ChartView view;

        public ChartComponentListener( ChartView view )
        {
            this.view = view;
        }

        @Override
        public void componentResized( ComponentEvent e )
        {
            view.chart.axesLayer.repaint = true;
            view.chart.seriesLayer.repaint = true;
            view.chart.highlightLayer.clear();
            view.chart.highlightLayer.repaint = false;
        }

        @Override
        public void componentMoved( ComponentEvent e )
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void componentShown( ComponentEvent e )
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void componentHidden( ComponentEvent e )
        {
            // TODO Auto-generated method stub

        }
    }
}
