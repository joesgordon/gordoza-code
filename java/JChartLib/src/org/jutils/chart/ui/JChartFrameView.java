package org.jutils.chart.ui;

import java.awt.*;

import javax.swing.*;

import org.jutils.chart.ChartUtils;
import org.jutils.chart.ui.objects.Series;
import org.jutils.ui.*;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JChartFrameView implements IView<JFrame>
{
    /**  */
    private final JFrame frame;
    /**  */
    private final ChartView chartView;

    /***************************************************************************
     * @param title
     **************************************************************************/
    public JChartFrameView( String title )
    {
        this.frame = new JFrame();
        this.chartView = new ChartView();

        frame.setTitle( title );

        frame.setJMenuBar( createMenubar( frame ) );
        frame.setContentPane( createContentPane() );

        Series s;

        s = new Series( ChartUtils.createLineSeries( 1000000, 1.0, 0.0, -5.0,
            5.0 ) );
        // s.marker = null;
        s.line.setSize( 4 );
        s.line = null;
        chartView.addSeries( s );

        s = new Series( ChartUtils.createSinSeries( 1000000, 1.0, 4.0, 0.0,
            -5.0, 5.0 ) );
        s.marker.setColor( new Color( 0x339933 ) );
        s.highlightMarker.setColor( new Color( 0x339933 ) );
        s.line.setColor( new Color( 0x227722 ) );
        s.line.setSize( 4 );
        s.line = null;
        chartView.addSeries( s );

        s = new Series( ChartUtils.createLineSeries( 1000000, -0.750, 0.0,
            -5.0, 5.0 ) );
        s.marker.setColor( new Color( 0xFF9933 ) );
        s.highlightMarker.setColor( new Color( 0xFF9933 ) );
        s.line.setColor( new Color( 0xCC6622 ) );
        s.line = null;
        chartView.addSeries( s );
    }

    /***************************************************************************
     * @param frame
     * @return
     **************************************************************************/
    private JMenuBar createMenubar( JFrame frame )
    {
        JMenuBar menubar = new JGoodiesMenuBar();

        menubar.add( createFileMenu() );

        return menubar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createFileMenu()
    {
        JMenu menu = new JMenu( "File" );
        JMenuItem item;

        item = new JMenuItem( "Exit" );
        item.addActionListener( new ExitListener( frame ) );
        menu.add( item );

        return menu;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Container createContentPane()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        StatusBarPanel statusView = new StatusBarPanel();

        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 );
        panel.add( chartView.getView(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 );
        panel.add( new JSeparator(), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 );
        panel.add( statusView.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frame;
    }
}
