package org.jutils.task;

import java.awt.Component;
import java.awt.event.ActionListener;

import org.jutils.ui.model.IView;

public interface ITaskView extends IView<Component>
{
    public void addCancelListener( ActionListener listener );

    public void signalMessage( String message );

    public void signalPercent( int percent );

    public void signalError( TaskError error );
}
