package org.jutils.chart.app;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import org.jutils.chart.ChartIcons;
import org.jutils.chart.ChartUtils;
import org.jutils.chart.model.ISeriesData;
import org.jutils.chart.model.Series;
import org.jutils.chart.ui.ChartView;
import org.jutils.io.OptionsSerializer;
import org.jutils.ui.RecentFilesMenuView;
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
    private final OptionsSerializer<UserData> userio;

    /***************************************************************************
     * @param title
     * @param userio
     **************************************************************************/
    public JChartFrameView( String title )
    {
        this.userio = JChartAppConstants.getUserIO();

        this.frameView = new StandardFrameView();
        this.chartView = new ChartView();
        this.recentFiles = new RecentFilesMenuView();

        createMenubar( frameView.getMenuBar(), frameView.getFileMenu() );
        frameView.setContent( chartView.getView() );

        JFrame frame = frameView.getView();

        frame.setTitle( title );
        frame.setIconImages( ChartIcons.getChartImages() );

        chartView.addFileLoadedListener( new FileLoadedListener( this ) );

        recentFiles.setData( userio.getOptions().recentFiles.toList() );
        recentFiles.addSelectedListener( new FileSelected( this ) );

        Series s;
        ISeriesData<?> data;

        // data = ChartUtils.createTestSeries();
        // s = new Series( data );
        // s.line.weight = 4;
        // s.isPrimaryDomain = true;
        // s.isPrimaryRange = true;
        // chartView.addSeries( s );

        chartView.chart.title.text = "Example Data Sets";

        data = ChartUtils.createLineSeries( 1000000, -1.0, 0.0, -5.0, 5.0 );
        s = new Series( data );
        s.name = "y = -x";
        s.marker.color = new Color( 0xFF9933 );
        s.highlight.color = new Color( 0xFF9933 );
        s.line.color = new Color( 0xCC6622 );
        chartView.addSeries( s, true );

        data = ChartUtils.createSinSeries( 1000000, 1.0, 4.0, 0.0, -5.0, 5.0 );
        s = new Series( data );
        s.name = "y = sin(x)";
        s.marker.color = new Color( 0x339933 );
        s.highlight.color = new Color( 0x339933 );
        s.line.color = new Color( 0x227722 );
        s.line.weight = 4;
        chartView.addSeries( s, true );

        data = ChartUtils.createLineSeries( 1000000, 1.0, 0.0, 5.0, 10.0 );
        s = new Series( data );
        s.name = "y = x";
        s.line.weight = 4;
        s.isPrimaryDomain = false;
        s.isPrimaryRange = false;
        chartView.addSeries( s, true );
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

        item = new JMenuItem( chartView.dataAction );
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
            view.userio.getOptions().recentFiles.push( event.getItem() );
            view.userio.write();
            view.recentFiles.setData( view.userio.getOptions().recentFiles.toList() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileSelected implements ItemActionListener<File>
    {
        private final JChartFrameView view;

        public FileSelected( JChartFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<File> event )
        {
            view.chartView.importData( event.getItem(), false );
        }
    }
}
