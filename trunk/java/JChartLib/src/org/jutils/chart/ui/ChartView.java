package org.jutils.chart.ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import java.io.*;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.jutils.chart.*;
import org.jutils.chart.data.ScreenPlotTransformer;
import org.jutils.chart.data.XYPoint;
import org.jutils.chart.io.DataFileReader;
import org.jutils.chart.model.ISeriesData;
import org.jutils.chart.ui.objects.Chart;
import org.jutils.chart.ui.objects.Series;
import org.jutils.ui.event.*;
import org.jutils.ui.event.FileDropTarget.DropActionType;
import org.jutils.ui.event.FileDropTarget.IFileDropEvent;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartView implements IView<JComponent>
{
    /**  */
    private final ChadgetPanel mainPanel;
    /**  */
    private final Chart chart;
    /**  */
    private final IPalette palette;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartView()
    {
        this.mainPanel = new ChadgetPanel();
        this.chart = new Chart();
        this.palette = new PresetPalette();

        mainPanel.setObject( chart );

        mainPanel.addComponentListener( new ChartComponentListener( this ) );
        mainPanel.addMouseMotionListener( new ChartMouseListenter( this ) );
        mainPanel.setDropTarget( new FileDropTarget( new ChartDropTarget( this ) ) );
    }

    /***************************************************************************
     * @param s
     **************************************************************************/
    public void addSeries( Series s )
    {
        addSeries( s, false );
    }

    /***************************************************************************
     * @param s
     * @param addData
     **************************************************************************/
    public void addSeries( Series s, boolean addData )
    {
        if( !addData )
        {
            clear();
        }

        Color c = palette.next();
        chart.plot.serieses.add( s );
        s.marker.setColor( c );
        s.highlightMarker.setColor( c );
        s.line.setColor( c );
        // s.line = null;

        chart.plot.calculateRanges();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clear()
    {
        chart.plot.serieses.clear();
        chart.plot.highlightLayer.repaint = true;
        chart.plot.seriesLayer.repaint = true;
        mainPanel.repaint();
    }

    /***************************************************************************
     * @param file
     * @param addData
     **************************************************************************/
    public void importData( File file, boolean addData )
    {
        try
        {
            DataFileReader reader = new DataFileReader();
            ISeriesData data = reader.read( file );
            Series s = new Series( data );

            s.name = file.getName();

            addSeries( s, addData );

            // LogUtils.printDebug( "x => (%f,%f)", chart.plot.context.xMin,
            // chart.plot.context.xMax );
            // LogUtils.printDebug("");

            chart.plot.seriesLayer.repaint = true;
            mainPanel.repaint();
        }
        catch( FileNotFoundException ex )
        {
            JOptionPane.showMessageDialog( mainPanel,
                "The file was not found: " + file.getAbsolutePath(),
                "File Not Found", JOptionPane.ERROR_MESSAGE );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( mainPanel,
                "I/O Exception: " + ex.getMessage(), "I/O Exception",
                JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return mainPanel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ChartDropTarget implements
        ItemActionListener<IFileDropEvent>
    {
        private final ChartView view;

        public ChartDropTarget( ChartView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<IFileDropEvent> event )
        {
            IFileDropEvent fde = event.getItem();
            List<File> files = fde.getFiles();

            boolean addData = fde.getActionType() == DropActionType.COPY;

            for( int i = 0; i < files.size(); i++ )
            {
                view.importData( files.get( i ), addData );
                addData = true;
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
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
            Point p = new Point( e.getX() - 20, e.getY() - 20 );
            XYPoint xy = new XYPoint();
            int idx;

            ScreenPlotTransformer trans = new ScreenPlotTransformer(
                view.chart.plot.context );

            // LogUtils.printDebug( "here: " + mx );

            for( Series s : view.chart.plot.serieses )
            {
                trans.fromScreen( p, xy );
                idx = ChartUtils.findNearest( s.data, xy.x );

                xy = new XYPoint( s.data.get( idx ) );
                trans.fromChart( xy, p );

                // LogUtils.printDebug( "here: " + xy.x );

                s.highlightMarker.setLocation( new Point( p ) );
            }

            view.chart.plot.highlightLayer.repaint = true;

            view.mainPanel.repaint();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
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
            view.mainPanel.repaint();
        }
    }
}
