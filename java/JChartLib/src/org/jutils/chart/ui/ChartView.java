package org.jutils.chart.ui;

import java.awt.Point;
import java.awt.event.*;

import javax.swing.JComponent;

import org.jutils.chart.ChartUtils;
import org.jutils.chart.data.ISeries;
import org.jutils.chart.ui.objects.Chart;
import org.jutils.chart.ui.objects.Series;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartView implements IView<JComponent>
{
    private final ChadgetPanel mainPanel;
    private final Chart chart;

    public ChartView()
    {
        this.mainPanel = new ChadgetPanel();
        this.chart = new Chart();

        mainPanel.setObject( chart );

        mainPanel.addComponentListener( new ChartComponentListener( this ) );
        mainPanel.addMouseMotionListener( new ChartMouseListenter( this ) );
    }

    public void addSeries( Series s )
    {
        chart.plot.serieses.add( s );
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
            int w = view.chart.context.width;
            int h = view.chart.context.height;

            // System.out.println( "here: " + mx );

            for( Series s : view.chart.plot.serieses )
            {
                x = ChartUtils.coordsToValueX( mx, w, view.chart.context );
                idx = ChartView.findX( s.data, x );
                p = ChartUtils.valueToChartCoords( s.data.getX( idx ),
                    s.data.getY( idx ), w, h, view.chart.context );

                // p.x = mx;

                s.highlightMarker.setLocation( p );
            }

            view.chart.plot.highlightLayer.repaint = true;

            view.mainPanel.repaint();
        }
    }

    private static class ChartComponentListener extends ComponentAdapter
    {
        private final ChartView view;

        public ChartComponentListener( ChartView view )
        {
            this.view = view;
        }

        @Override
        public void componentResized( ComponentEvent e )
        {
            view.chart.elements.axesLayer.repaint = true;
            view.chart.plot.seriesLayer.repaint = true;
            view.chart.plot.highlightLayer.clear();
            view.chart.plot.highlightLayer.repaint = false;
        }
    }
}
