package org.jutils.chart.app;

import java.awt.Color;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import org.jutils.chart.ChartIcons;
import org.jutils.chart.ChartUtils;
import org.jutils.chart.model.ISeriesData;
import org.jutils.chart.model.Series;
import org.jutils.chart.ui.ChartView;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.RecentFilesMenuView;
import org.jutils.ui.RecentFilesMenuView.IRecentSelected;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JChartFrameView implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final ChartView chartView;
    /**  */
    private final RecentFilesMenuView recentFiles;

    /**  */
    private final OptionsSerializer<UserData> options;

    /***************************************************************************
     * @param title
     * @param options
     **************************************************************************/
    public JChartFrameView( String title )
    {
        this.options = JChartAppConstants.getOptions();

        this.frameView = new StandardFrameView();
        this.chartView = new ChartView();
        this.recentFiles = new RecentFilesMenuView();

        createMenubar( frameView.getMenuBar(), frameView.getFileMenu() );
        frameView.setContent( chartView.getView() );

        JFrame frame = frameView.getView();

        frame.setTitle( title );
        frame.setIconImages( ChartIcons.getChartImages() );
        frame.addWindowListener( new ChartWindowListener( this ) );

        chartView.addFileLoadedListener( new FileLoadedListener( this ) );

        recentFiles.setData( options.getOptions().recentFiles.toList() );
        recentFiles.addSelectedListener( new FileSelected( this ) );

        chartView.chart.domainAxis.title.visible = true;
        chartView.chart.domainAxis.title.text = "X Values";

        chartView.chart.rangeAxis.title.visible = true;
        chartView.chart.rangeAxis.title.text = "Y Values";

        chartView.chart.secDomainAxis.title.visible = true;
        chartView.chart.secDomainAxis.title.text = "Sec X Values";

        chartView.chart.secRangeAxis.title.visible = true;
        chartView.chart.secRangeAxis.title.text = "Sec Y Values";

        chartView.chart.legend.visible = true;

        Series s;
        ISeriesData<?> data;

        // data = ChartUtils.createTestSeries();
        // s = new Series( data );
        // s.line.weight = 4;
        // s.isPrimaryDomain = true;
        // s.isPrimaryRange = true;
        // chartView.addSeries( s );

        chartView.chart.title.text = "Example Data Sets";

        int pointCount = 10000;

        data = ChartUtils.createLineSeries( pointCount, -1.0, 0.0, -5.0, 5.0 );
        s = new Series( data );
        s.name = "y = -x";
        s.marker.color = new Color( 0xFF9933 );
        s.highlight.color = new Color( 0xFF9933 );
        s.line.color = new Color( 0xCC6622 );
        chartView.addSeries( s, true );

        data = ChartUtils.createSinSeries( pointCount, 1.0, 4.0, 0.0, -5.0,
            5.0 );
        s = new Series( data );
        s.name = "y = sin(x)";
        s.marker.color = new Color( 0x339933 );
        s.highlight.color = new Color( 0x339933 );
        s.line.color = new Color( 0x227722 );
        s.line.weight = 4;
        chartView.addSeries( s, true );

        data = ChartUtils.createLineSeries( pointCount, 1.0, 0.0, 5.1, 10.9 );
        s = new Series( data );
        s.name = "y = x";
        s.line.weight = 4;
        s.isPrimaryDomain = false;
        s.isPrimaryRange = false;
        chartView.addSeries( s, true );

        // data = ChartUtils.createSinSeries( pointCount, 4.0, 40.0, 0.0, -5.0,
        // 5.0
        // );
        // s = new Series( data );
        // s.name = "y = sin(x)";
        // s.marker.color = new Color( 0x393933 );
        // s.highlight.color = new Color( 0x339933 );
        // s.line.color = new Color( 0x227722 );
        // s.line.weight = 4;
        // chartView.addSeries( s, true );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public ChartView getChart()
    {
        return chartView;
    }

    /***************************************************************************
     * @param menuBar
     * @return
     **************************************************************************/
    private JMenuBar createMenubar( JMenuBar menubar, JMenu fileMenu )
    {
        menubar.add( createFileMenu( fileMenu ) );

        menubar.add( createViewMenu() );

        return menubar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createFileMenu( JMenu menu )
    {
        JMenuItem item;
        int i = 0;

        item = new JMenuItem( chartView.openAction );
        menu.add( item, i++ );

        item = new JMenuItem( chartView.saveAction );
        menu.add( item, i++ );

        menu.add( recentFiles.getView(), i++ );

        item = new JMenuItem( "Clear" );
        item.addActionListener( new ClearListener( this ) );
        menu.add( item, i++ );

        menu.add( new JSeparator(), i++ );

        return menu;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createViewMenu()
    {
        JMenu menu = new JMenu( "View" );
        JMenuItem item;

        item = new JMenuItem( chartView.propertiesAction );
        menu.add( item );

        return menu;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ClearListener implements ActionListener
    {
        private final JChartFrameView view;

        public ClearListener( JChartFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            view.chartView.clear();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileLoadedListener implements ItemActionListener<File>
    {
        private final JChartFrameView view;

        public FileLoadedListener( JChartFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<File> event )
        {
            view.options.getOptions().recentFiles.push( event.getItem() );
            view.options.write();
            view.recentFiles.setData(
                view.options.getOptions().recentFiles.toList() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileSelected implements IRecentSelected
    {
        private final JChartFrameView view;

        public FileSelected( JChartFrameView view )
        {
            this.view = view;
        }

        @Override
        public void selected( File file, boolean ctrlPressed )
        {
            view.chartView.importData( file, ctrlPressed );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ChartWindowListener extends WindowAdapter
    {
        private final JChartFrameView view;

        public ChartWindowListener( JChartFrameView view )
        {
            this.view = view;
        }

        @Override
        public void windowClosing( WindowEvent e )
        {
            view.chartView.closeOptions();
        }
    }
}
