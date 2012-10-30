package org.jutils.ui.model;

import java.awt.Component;

public interface IComponentView extends IView<Component>
{
    @Override
    public Component getView();
}
