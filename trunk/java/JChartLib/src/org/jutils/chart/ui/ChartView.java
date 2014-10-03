package org.jutils.chart.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.jutils.chart.*;
import org.jutils.chart.data.*;
import org.jutils.chart.data.ChartContext.IDimensionCoords;
import org.jutils.chart.io.DataFileReader;
import org.jutils.chart.model.*;
import org.jutils.chart.ui.objects.*;
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

        ChartMouseListenter ml = new ChartMouseListenter( this );

        mainPanel.addComponentListener( new ChartComponentListener( this ) );
        mainPanel.addMouseListener( ml );
        mainPanel.addMouseMotionListener( ml );
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
        repaintChart();
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

        repaintChart();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void repaintChart()
    {
        chartWidget.calculateBounds();
        chartWidget.plot.seriesLayer.repaint = true;
        chartWidget.axes.axesLayer.repaint = true;
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
     * @param file
     **************************************************************************/
    public void saveAsImage( File file )
    {
        int w = 640;
        int h = 480;

        BufferedImage bImg = new BufferedImage( w, h,
            BufferedImage.TYPE_INT_RGB );
        Graphics2D cg = bImg.createGraphics();

        mainPanel.setObject( new CircleMarker() );
        chartWidget.setVolatileVisible( false );
        repaintChart();

        chartWidget.draw( cg, 0, 0, w, h );
        chartWidget.setVolatileVisible( true );

        mainPanel.setObject( chartWidget );
        repaintChart();

        try
        {
            if( ImageIO.write( bImg, "png", file ) )
            {
                System.out.println( "-- saved" );
            }
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
    private static class ChartMouseListenter extends MouseAdapter
    {
        private final ChartView view;

        public ChartMouseListenter( ChartView view )
        {
            this.view = view;
        }

        @Override
        public void mouseDragged( MouseEvent e )
        {
            view.chartWidget.plot.selection.visible = true;
            view.chartWidget.plot.selection.end = e.getPoint();

            view.chartWidget.plot.highlightLayer.repaint = true;
            view.mainPanel.repaint();
        }

        public void mouseClicked( MouseEvent e )
        {
            if( SwingUtilities.isLeftMouseButton( e ) && e.getClickCount() == 2 )
            {
                view.chartWidget.context.restoreRanges();
                view.chartWidget.plot.seriesLayer.repaint = true;
                view.chartWidget.axes.axesLayer.repaint = true;
                view.mainPanel.repaint();
            }
        }

        @Override
        public void mousePressed( MouseEvent e )
        {
            view.chartWidget.plot.selection.start = e.getPoint();
        }

        @Override
        public void mouseReleased( MouseEvent evt )
        {
            if( !view.chartWidget.plot.selection.visible )
            {
                return;
            }

            view.chartWidget.plot.selection.visible = false;

            ChartContext context = view.chartWidget.context;

            Point s = view.chartWidget.plot.selection.start;
            Point e = evt.getPoint();

            s.x -= context.x;
            e.x -= context.x;

            s.y -= context.y;
            e.y -= context.y;

            int xmin = Math.min( s.x, e.x );
            int xmax = Math.max( s.x, e.x );

            int ymin = Math.min( s.y, e.y );
            int ymax = Math.max( s.y, e.y );

            double dmin;
            double dmax;

            dmin = context.domain.primary.fromScreen( xmin );
            dmax = context.domain.primary.fromScreen( xmax );
            context.primaryDomainSpan = new Span( dmin, dmax );

            dmin = context.range.primary.fromScreen( ymin );
            dmax = context.range.primary.fromScreen( ymax );
            context.primaryRangeSpan = new Span( dmin, dmax );
            // LogUtils.printDebug( "primary domain from " + ymin + " to " +
            // ymax );
            // LogUtils.printDebug( "primary domain from " + dmin + " to " +
            // dmax );

            dmin = context.domain.secondary.fromScreen( xmin );
            dmax = context.domain.secondary.fromScreen( xmax );
            context.secondaryDomainSpan = new Span( dmin, dmax );

            dmin = context.range.secondary.fromScreen( ymin );
            dmax = context.range.secondary.fromScreen( ymax );
            context.secondaryRangeSpan = new Span( dmin, dmax );

            context.latchCoords();

            view.chartWidget.plot.seriesLayer.repaint = true;
            view.chartWidget.axes.axesLayer.repaint = true;
            view.mainPanel.repaint();
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
