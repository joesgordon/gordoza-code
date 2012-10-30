package org.jutils.ui.model;

import java.awt.Component;

public interface IView<T extends Component>
{
    T getView();
}
