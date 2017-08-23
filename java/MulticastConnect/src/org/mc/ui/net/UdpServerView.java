package org.mc.ui.net;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.mc.ui.IConnectionView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpServerView implements IConnectionView
{
    /**  */
    private final JPanel view;

    /***************************************************************************
     * 
     **************************************************************************/
    public UdpServerView()
    {
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel();
        // TODO Auto-generated method stub
        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void close()
    {
        // TODO Auto-generated method stub
    }
}
