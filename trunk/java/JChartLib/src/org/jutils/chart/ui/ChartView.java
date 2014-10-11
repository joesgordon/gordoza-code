package org.jutils.chart.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.jutils.Utils;
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
    private final WidgetPanel mainPanel;
    /**  */
    private final ChartWidget chartWidget;
    /**  */
    private final IPalette palette;
    /**  */
    public final DataView dataView;

    /**  */
    private final ItemActionList<File> fileLoadedListeners;

    /**  */
    public final Chart chart;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartView()
    {
        this.chart = new Chart();
        this.mainPanel = new WidgetPanel();
        this.chartWidget = new ChartWidget( chart );
        this.palette = new PresetPalette();
        this.dataView = new DataView();

        this.fileLoadedListeners = new ItemActionList<>();

        mainPanel.setObject( chartWidget );

        ChartMouseListenter ml = new ChartMouseListenter( this );

        mainPanel.addComponentListener( new ChartComponentListener( this ) );
        mainPanel.addMouseListener( ml );
        mainPanel.addMouseMotionListener( ml );
        mainPanel.setDropTarget( new FileDropTarget( new ChartDropTarget( this ) ) );

        mainPanel.setFocusable( true );

        String actionMapKey = "delete_point";
        KeyStroke deleteKey = KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0 );
        ActionMap amap = mainPanel.getActionMap();
        InputMap imap = mainPanel.getInputMap( JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );

        imap.put( deleteKey, actionMapKey );
        amap.put( actionMapKey, new ActionAdapter( new DeletePointListener(
            this ), actionMapKey, null ) );

        mainPanel.setMinimumSize( new Dimension( 150, 150 ) );
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
            dataView.removeAllSeries();
        }

        chart.series.add( s );
        chartWidget.plot.serieses.add( new SeriesWidget( chart, s,
            chartWidget.context ) );
        repaintChart();

        dataView.addSeries( s, chart.series.size() );
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

        repaintChart();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void repaintChart()
    {
        chartWidget.calculateBounds();
        chartWidget.plot.seriesLayer.repaint = true;
        chartWidget.plot.highlightLayer.repaint = true;
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
            ISeriesData<?> data = reader.read( file );
            Series s = new Series( data );

            Color c = palette.next();

            s.name = file.getName();
            s.resource = file.getAbsolutePath();
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

        BufferedImage image = Utils.createTransparentImage( w, h );
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint( RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC );
        g2d.setRenderingHint( RenderingHints.KEY_FRACTIONALMETRICS,
            RenderingHints.VALUE_FRACTIONALMETRICS_ON );
        g2d.setRenderingHint( RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY );
        g2d.setRenderingHint( RenderingHints.KEY_STROKE_CONTROL,
            RenderingHints.VALUE_STROKE_PURE );

        mainPanel.setObject( new CircleMarker() );
        chartWidget.setVolatileVisible( false );
        repaintChart();

        chartWidget.draw( g2d, 0, 0, w, h );
        chartWidget.setVolatileVisible( true );

        mainPanel.setObject( chartWidget );
        repaintChart();

        try
        {
            if( ImageIO.write( image, "png", file ) )
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
                view.chartWidget.context.restoreAutoBounds();
                view.chartWidget.plot.seriesLayer.repaint = true;
                view.chartWidget.axes.axesLayer.repaint = true;
                view.mainPanel.repaint();
            }
            else if( SwingUtilities.isRightMouseButton( e ) &&
                e.getClickCount() == 2 )
            {
                for( SeriesWidget sw : view.chartWidget.plot.serieses )
                {
                    sw.clearSelected();
                    view.chartWidget.plot.seriesLayer.repaint = true;
                    view.mainPanel.repaint();
                }
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

            s.x = Math.max( s.x, context.x );
            s.x = Math.min( s.x, context.x + context.width );
            s.y = Math.max( s.y, context.y );
            s.y = Math.min( s.y, context.y + context.height );

            e.x = Math.max( e.x, context.x );
            e.x = Math.min( e.x, context.x + context.width );
            e.y = Math.max( e.y, context.y );
            e.y = Math.min( e.y, context.y + context.height );

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

            Span pds;
            Span prs;
            Span sds = null;
            Span srs = null;

            dmin = context.domain.primary.fromScreen( xmin );
            dmax = context.domain.primary.fromScreen( xmax );
            pds = new Span( dmin, dmax );

            dmin = context.range.primary.fromScreen( ymax );
            dmax = context.range.primary.fromScreen( ymin );
            prs = new Span( dmin, dmax );
            // LogUtils.printDebug( "primary domain from " + ymin + " to " +
            // ymax );
            // LogUtils.printDebug( "primary domain from " + dmin + " to " +
            // dmax );

            if( context.domain.secondary != null )
            {
                dmin = context.domain.secondary.fromScreen( xmin );
                dmax = context.domain.secondary.fromScreen( xmax );
                sds = new Span( dmin, dmax );
            }

            if( context.range.secondary != null )
            {
                dmin = context.range.secondary.fromScreen( ymax );
                dmax = context.range.secondary.fromScreen( ymin );
                srs = new Span( dmin, dmax );
            }

            if( pds.range == 0.0 || prs.range == 0.0 ||
                ( sds != null && sds.range == 0.0 ) ||
                ( srs != null && srs.range == 0.0 ) )
            {
                view.chartWidget.plot.highlightLayer.repaint = true;
                view.mainPanel.repaint();
                return;
            }

            if( SwingUtilities.isLeftMouseButton( evt ) )
            {
                Bounds b = context.getBounds();

                b.primaryDomainSpan = pds;
                b.primaryRangeSpan = prs;
                b.secondaryDomainSpan = sds;
                b.secondaryRangeSpan = srs;

                view.chartWidget.setBounds( b );
            }
            else
            {
                Span ds;
                Span rs;

                for( SeriesWidget sw : view.chartWidget.plot.serieses )
                {
                    if( sw.series.isPrimaryDomain )
                    {
                        ds = pds;
                    }
                    else
                    {
                        ds = sds;
                    }

                    if( sw.series.isPrimaryRange )
                    {
                        rs = prs;
                    }
                    else
                    {
                        rs = srs;
                    }

                    sw.setSelected( ds, rs );
                }
            }

            view.chartWidget.plot.seriesLayer.repaint = true;
            view.chartWidget.plot.highlightLayer.repaint = true;
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

            // if( p.x < 0 || p.y < 0 || p.x > context.width ||
            // p.y > context.height )
            // {
            // for( SeriesWidget s : view.chartWidget.plot.serieses )
            // {
            // s.highlight.setLocation( new Point( -5, -5 ) );
            // }
            // }

            // LogUtils.printDebug( "hover: " + mx );

            int seriesIdx = 0;
            for( SeriesWidget s : view.chartWidget.plot.serieses )
            {
                Point sp = new Point( p );
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

                if( domainCoords != null )
                {
                    xy.x = domainCoords.fromScreen( sp.x );

                    idx = ChartUtils.findNearest( s.series.data, xy.x );

                    if( idx > -1 )
                    {
                        if( s.series.isPrimaryRange )
                        {
                            rangeCoords = context.range.primary;
                        }
                        else
                        {
                            rangeCoords = context.range.secondary;
                        }

                        xy = new XYPoint( s.series.data.get( idx ) );
                        sp.x = domainCoords.fromCoord( xy.x );
                        sp.y = rangeCoords.fromCoord( xy.y );

                        // LogUtils.printDebug( "hover [" + s.series.name +
                        // "]: " +
                        // p.x + xy.x );

                        s.highlight.setLocation( new Point( sp ) );

                        view.dataView.setSelected( seriesIdx, idx );
                    }
                }
                seriesIdx++;
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

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DeletePointListener implements ActionListener
    {
        private final ChartView view;

        public DeletePointListener( ChartView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            // System.out.println( "Deleting points..." );

            for( SeriesWidget series : view.chartWidget.plot.serieses )
            {
                for( IDataPoint xy : series.series.data )
                {
                    if( xy.isSelected() )
                    {
                        xy.setHidden( true );
                        xy.setSelected( false );
                    }
                }
            }

            ChartContext context = view.chartWidget.context;

            context.calculateAutoBounds( view.chart );

            view.chartWidget.plot.seriesLayer.repaint = true;
            view.mainPanel.repaint();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class SeriesChangedEvent
    {
        public final Series s;
        public final int index;
        public final boolean added;

        public SeriesChangedEvent( Series s, int index, boolean added )
        {
            this.s = s;
            this.index = index;
            this.added = added;
        }
    }
}
