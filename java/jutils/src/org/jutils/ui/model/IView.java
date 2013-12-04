package org.jutils.ui.model;

import java.awt.Component;

/*******************************************************************************
 * Represents a generic method of storing a view.
 ******************************************************************************/
public interface IView<T extends Component>
{
    /***************************************************************************
     * Returns the pre-built view.
     **************************************************************************/
    public T getView();
}
