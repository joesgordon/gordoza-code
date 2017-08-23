package org.mc.ui.net;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.mc.ui.IConnectionView;

public class UdpClientView implements IConnectionView
{
    private final JPanel view;

    public UdpClientView()
    {
        this.view = createView();
    }

    private JPanel createView()
    {
        JPanel panel = new JPanel();
        // TODO Auto-generated method stub
        return panel;
    }

    @Override
    public JComponent getView()
    {
        return view;
    }

    @Override
    public void close()
    {
        // TODO Auto-generated method stub
    }
}
