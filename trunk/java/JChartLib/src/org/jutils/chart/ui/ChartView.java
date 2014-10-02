package org.jutils.chart.ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import java.io.*;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.jutils.chart.*;
import org.jutils.chart.data.*;
import org.jutils.chart.data.ChartContext.IDimensionCoords;
import org.jutils.chart.io.DataFileReader;
import org.jutils.chart.model.*;
import org.jutils.chart.ui.objects.ChartWidget;
import org.jutils.chart.ui.objects.SeriesWidget;
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
    private final ChartWidgetPanel mainPanel;
    /**  */
    private final ChartWidget chartWidget;
    /**  */
    private final IPalette palette;

    /**  */
    private final ItemActionList<File> fileLoadedListeners;

    /**  */
    private Chart chart;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartView()
    {
        this.chart = new Chart();
        this.mainPanel = new ChartWidgetPanel();
        this.chartWidget = new ChartWidget( chart );
        this.palette = new PresetPalette();

        this.fileLoadedListeners = new ItemActionList<>();

        mainPanel.setObject( chartWidget );

        mainPanel.addComponentListener( new ChartComponentListener( this ) );
        mainPanel.addMouseMotionListener( new ChartMouseListenter( this ) );
        mainPanel.setDropTarget( new FileDropTarget( new ChartDropTarget( this ) ) );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addFileLoadedListener( ItemActionListener<File> l )
    {
        fileLoadedListeners.addListener( l );
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

        chart.series.add( s );
        chartWidget.plot.serieses.add( new SeriesWidget( chart, s ) );

        chartWidget.calculateBounds();
        chartWidget.plot.seriesLayer.repaint = true;
        chartWidget.axes.axesLayer.repaint = true;
        mainPanel.repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clear()
    {
        chart.series.clear();
        chartWidget.plot.serieses.clear();
        chartWidget.plot.highlightLayer.repaint = true;
        chartWidget.plot.seriesLayer.repaint = true;
        setTitle( "Title" );
        mainPanel.repaint();
    }

    /***************************************************************************
     * @param files
     **************************************************************************/
    public void importData( List<File> files )
    {
        boolean addData = false;

        for( File file : files )
        {
            importData( file, addData );
            addData = true;
        }
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

            Color c = palette.next();

            s.name = file.getName();

            s.marker.color = c;
            s.highlight.color = c;
            s.line.color = c;

            addSeries( s, addData );

            // LogUtils.printDebug( String.format( "x => (%f,%f)",
            // chart.plot.context.xMin, chart.plot.context.xMax ) );
            //
            // LogUtils.printDebug( String.format( "y => (%f,%f)",
            // chart.plot.context.yMin, chart.plot.context.yMax ) );
            // LogUtils.printDebug( "" );

            fileLoadedListeners.fireListeners( this, file );
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

        if( chartWidget.plot.serieses.size() < 2 )
        {
            setTitle( file.getName() );
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
     * @param title
     **************************************************************************/
    public void setTitle( String title )
    {
        chart.title.text = title;
        chartWidget.title.repaint();
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
            Point p = new Point( e.getX() - view.chartWidget.context.x,
                e.getY() - 20 );
            XYPoint xy = new XYPoint();
            int idx;

            ChartContext context = view.chartWidget.context;

            context.latchCoords();

            // LogUtils.printDebug( "here: " + mx );

            for( SeriesWidget s : view.chartWidget.plot.serieses )
            {
                IDimensionCoords domainCoords;
                IDimensionCoords rangeCoords;

                if( s.series.isPrimaryDomain )
                {
                    domainCoords = context.domain.primary;
                }
                else
                {
                    domainCoords = context.domain.secondary;
                }

                if( s.series.isPrimaryRange )
                {
                    rangeCoords = context.range.primary;
                }
                else
                {
                    rangeCoords = context.range.secondary;
                }

                xy.x = domainCoords.fromScreen( p.x );
                idx = ChartUtils.findNearest( s.series.data, xy.x );

                xy = new XYPoint( s.series.data.get( idx ) );
                p.x = domainCoords.fromCoord( xy.x );
                p.y = rangeCoords.fromCoord( xy.y );

                // LogUtils.printDebug( "here: " + xy.x );

                s.highlight.setLocation( new Point( p ) );
            }

            view.chartWidget.plot.highlightLayer.repaint = true;

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
            view.chartWidget.axes.axesLayer.repaint = true;
            view.chartWidget.plot.seriesLayer.repaint = true;
            view.chartWidget.plot.highlightLayer.clear();
            view.chartWidget.plot.highlightLayer.repaint = false;
            view.mainPanel.repaint();
        }
    }
}
