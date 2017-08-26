package org.jutils.ui.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import org.jutils.ui.event.updater.IUpdater;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RightClickListener extends MouseAdapter
{
    /**  */
    private final IUpdater<MouseEvent> callback;

    /***************************************************************************
     * @param callback
     **************************************************************************/
    public RightClickListener( IUpdater<MouseEvent> callback )
    {
        this.callback = callback;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void mouseClicked( MouseEvent e )
    {
        // LogUtils.printDebug( "Clicked: " + e.toString() );
        if( SwingUtilities.isRightMouseButton( e ) && !e.isConsumed() )
        {
            callback.update( e );
        }
    }
}
