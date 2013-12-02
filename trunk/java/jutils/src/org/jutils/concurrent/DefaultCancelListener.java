package org.jutils.concurrent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DefaultCancelListener implements ActionListener
{
    /**  */
    private final ITaskStopManager stopper;

    /***************************************************************************
     * @param stopper
     **************************************************************************/
    public DefaultCancelListener( ITaskStopManager stopper )
    {
        this.stopper = stopper;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void actionPerformed( ActionEvent e )
    {
        stopper.stop();
    }
}
