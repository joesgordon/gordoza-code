package org.jutils.ui.model;

import java.awt.Component;

/*******************************************************************************
 * Represents a generic method of storing a view.
 * @param <T> the type of {@link Component} stored by this view.
 ******************************************************************************/
public interface IView<T extends Component>
{
    /***************************************************************************
     * Returns the previously built view.
     **************************************************************************/
    public T getView();
}
