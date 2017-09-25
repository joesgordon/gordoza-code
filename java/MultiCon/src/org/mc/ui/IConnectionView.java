package org.mc.ui;

import javax.swing.JComponent;

import org.jutils.ui.model.IView;

public interface IConnectionView extends IView<JComponent>
{
    public void close();
}
