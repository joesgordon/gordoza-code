package org.jutils.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ComponentView implements IView<JPanel>
{
    /**  */
    private final JPanel view;

    /***************************************************************************
     * 
     **************************************************************************/
    public ComponentView()
    {
        this.view = new JPanel( new BorderLayout() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * @param comp
     **************************************************************************/
    public void setComponent( Component comp )
    {
        view.removeAll();
        if( comp != null )
        {
            view.add( comp, BorderLayout.CENTER );
        }
        view.invalidate();
        view.validate();
        view.repaint();
    }
}
