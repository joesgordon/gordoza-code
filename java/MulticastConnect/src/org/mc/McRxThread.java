package org.mc;

import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.jutils.concurrent.IStoppableTask;
import org.jutils.concurrent.ITaskStopManager;
import org.mc.ui.McFrame;

public class McRxThread implements IStoppableTask
{
    private McFrame frame;

    private McComm commModel;

    public McRxThread( McFrame frame, McComm commModel )
    {
        this.frame = frame;
        this.commModel = commModel;
    }

    @Override
    public void run( ITaskStopManager stopper )
    {
        while( stopper.continueProcessing() )
        {
            try
            {
                final McMessage msg = commModel.rxMessage();
                SwingUtilities.invokeLater( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        frame.addMessage( msg );
                    }
                } );
            }
            catch( SocketTimeoutException ex )
            {
                ;
            }
            catch( IOException ex )
            {
                JOptionPane.showMessageDialog( frame,
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
