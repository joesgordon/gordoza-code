package org.mc.ui;

import javax.swing.JComponent;

import org.jutils.ui.model.IView;

/*******************************************************************************
 * Defines a view that can create connections.
 ******************************************************************************/
public interface IConnectionView extends IView<JComponent>
{
    /***************************************************************************
     * Closes the connection if it has been established.
     **************************************************************************/
    public void close();

    /***************************************************************************
     * @return the string describing the connection this view represents.
     **************************************************************************/
    public String getTitle();
}
