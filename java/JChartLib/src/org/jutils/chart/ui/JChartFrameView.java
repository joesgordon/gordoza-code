package org.jutils.chart.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.chart.ChartIcons;
import org.jutils.chart.ChartUtils;
import org.jutils.chart.ui.objects.Series;
import org.jutils.ui.StandardFrameView;
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

    /***************************************************************************
     * @param title
     **************************************************************************/
    public JChartFrameView( String title )
    {
        this.frameView = new StandardFrameView();
        this.chartView = new ChartView();

        createMenubar( frameView.getMenuBar(), frameView.getFileMenu() );
        frameView.setContent( chartView.getView() );

        JFrame frame = frameView.getView();

        frame.setTitle( title );
        frame.setIconImages( ChartIcons.getChartImages() );

        Series s;

        s = new Series( ChartUtils.createLineSeries( 1000000, 1.0, 0.0, -5.0,
            5.0 ) );
        s.line.setSize( 4 );
        // s.line = null;
        chartView.addSeries( s );

        s = new Series( ChartUtils.createLineSeries( 1000000, -1.0, 0.0, -5.0,
            5.0 ) );
        s.marker.setColor( new Color( 0xFF9933 ) );
        s.highlightMarker.setColor( new Color( 0xFF9933 ) );
        s.line.setColor( new Color( 0xCC6622 ) );
        // s.line = null;
        chartView.addSeries( s, true );

        s = new Series( ChartUtils.createSinSeries( 1000000, 1.0, 4.0, 0.0,
            -5.0, 5.0 ) );
        s.marker.setColor( new Color( 0x339933 ) );
        s.highlightMarker.setColor( new Color( 0x339933 ) );
        s.line.setColor( new Color( 0x227722 ) );
        s.line.setSize( 4 );
        // s.line = null;
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

        item = new JMenuItem( "Clear" );
        item.addActionListener( new ClearListener( this ) );
        menu.add( item, 0 );

        menu.add( new JSeparator(), 1 );

        return menu;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createViewMenu()
    {
        JMenu menu = new JMenu( "View" );
        JMenuItem item;

        item = new JMenuItem( "Data" );
        item.addActionListener( new DataDialogListener( this ) );
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
    private static class DataDialogListener implements ActionListener
    {
        private final OkDialog dialog;
        private final DataView dataView;

        public DataDialogListener( JChartFrameView view )
        {
            this.dataView = new DataView();
            this.dialog = new OkDialog( dataView, view.getView() );

            JDialog d = dialog.getView();

            d.setSize( 300, 300 );
            d.validate();
            d.setLocationRelativeTo( view.getView() );
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            JDialog d = dialog.getView();

            if( !d.isVisible() )
            {
                d.setVisible( true );
            }
        }
    }

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
}
