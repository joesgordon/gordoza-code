package org.jutils.ui.progress;

import org.jutils.concurrent.ITaskStopManager;

/***************************************************************************
 * 
 **************************************************************************/
public interface IProgressTask
{
    public void run( ITaskStopManager stopManager, IProgressView progress );
}
