package org.jutils.chart.ui;

import java.awt.*;

import javax.swing.*;

import org.jutils.ui.*;
import org.jutils.ui.model.IView;

public class JChartFrameView implements IView<JFrame>
{
    private final JFrame frame;
    private final ChartView chartView;

    public JChartFrameView( String title )
    {
        this.frame = new JFrame();
        this.chartView = new ChartView();

        frame.setTitle( title );

        frame.setJMenuBar( createMenubar( frame ) );
        frame.setContentPane( createContentPane() );
    }

    private JMenuBar createMenubar( JFrame frame )
    {
        JMenuBar menubar = new JGoodiesMenuBar();

        menubar.add( createFileMenu() );

        return menubar;
    }

    private JMenu createFileMenu()
    {
        JMenu menu = new JMenu( "File" );
        JMenuItem item;

        item = new JMenuItem( "Exit" );
        item.addActionListener( new ExitListener( frame ) );
        menu.add( item );

        return menu;
    }

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

    @Override
    public JFrame getView()
    {
        return frame;
    }
}
