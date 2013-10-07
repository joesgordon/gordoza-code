package org.jutils.chart.ui;

import javax.swing.JPanel;

import org.jutils.ui.model.IView;

public class DataView implements IView<JPanel>
{
    private final JPanel view;

    public DataView()
    {
        this.view = createView();
    }

    private JPanel createView()
    {
        JPanel panel = new JPanel();

        return panel;
    }

    @Override
    public JPanel getView()
    {
        return view;
    }
}
