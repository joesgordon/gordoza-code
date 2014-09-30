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
import org.jutils.chart.model.Series;
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
    private final ChartWidget chart;
    /**  */
    private final IPalette palette;

    /**  */
    private final ItemActionList<File> fileLoadedListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartView()
    {
        this.mainPanel = new ChartWidgetPanel();
        this.chart = new ChartWidget();
        this.palette = new PresetPalette();

        this.fileLoadedListeners = new ItemActionList<>();

        mainPanel.setObject( chart );

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

        chart.plot.serieses.add( new SeriesWidget( s ) );

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
            Series s = new Series();

            Color c;

            s.data = reader.read( file );
            s.name = file.getName();

            c = palette.next();

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

            chart.plot.seriesLayer.repaint = true;
            mainPanel.repaint();

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

            for( SeriesWidget s : view.chart.plot.serieses )
            {
                trans.fromScreen( p, xy );
                idx = ChartUtils.findNearest( s.series.data, xy.x );

                xy = new XYPoint( s.series.data.get( idx ) );
                trans.fromChart( xy, p );

                // LogUtils.printDebug( "here: " + xy.x );

                s.highlight.setLocation( new Point( p ) );
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
            view.chart.axes.axesLayer.repaint = true;
            view.chart.plot.seriesLayer.repaint = true;
            view.chart.plot.highlightLayer.clear();
            view.chart.plot.highlightLayer.repaint = false;
            view.mainPanel.repaint();
        }
    }
}
