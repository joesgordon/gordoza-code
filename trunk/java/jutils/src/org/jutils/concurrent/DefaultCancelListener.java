package org.jutils.concurrent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DefaultCancelListener implements ActionListener
{
    /**  */
    private final IStopper stopper;

    /***************************************************************************
     * @param stopper
     **************************************************************************/
    public DefaultCancelListener( IStopper stopper )
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
