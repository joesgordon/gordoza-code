package org.mc;

import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.jutils.concurrent.IStoppableTask;
import org.jutils.concurrent.ITaskStopManager;
import org.mc.io.MulticastConnection;
import org.mc.ui.McFrame;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McRxThread implements IStoppableTask
{
    /**  */
    private final McFrame frame;
    /**  */
    private final MulticastConnection commModel;

    /***************************************************************************
     * @param frame
     * @param commModel
     **************************************************************************/
    public McRxThread( McFrame frame, MulticastConnection commModel )
    {
        this.frame = frame;
        this.commModel = commModel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run( ITaskStopManager stopper )
    {
        while( stopper.continueProcessing() )
        {
            try
            {
                final McMessage msg = commModel.rxMessage();
                SwingUtilities.invokeLater( () -> frame.addMessage( msg ) );
            }
            catch( SocketTimeoutException ex )
            {
                ;
            }
            catch( IOException ex )
            {
                JOptionPane.showMessageDialog( frame.getView(),
                    "Error receiving packet: " + ex.getMessage() );
            }
            catch( Exception ex )
            {
                ex.printStackTrace();
                break;
            }
        }
    }
}
