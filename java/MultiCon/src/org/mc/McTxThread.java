package org.mc;

import java.awt.Window;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import org.jutils.concurrent.IStoppableTask;
import org.jutils.concurrent.ITaskStopManager;
import org.jutils.net.IConnection;
import org.mc.ui.TxDialog;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McTxThread implements IStoppableTask
{
    /**  */
    private int sendCount;
    /**  */
    private TxDialog dialog;
    /**  */
    private final int sendCountMax;
    /**  */
    private final int sendDelay;
    /**  */
    private final byte[] msgContents;
    /**  */
    private final IConnection commModel;
    /**  */
    private final Window frame;

    /***************************************************************************
     * @param count
     * @param delay
     * @param contents
     * @param commModel
     * @param parent
     **************************************************************************/
    public McTxThread( int count, int delay, byte[] contents,
        IConnection commModel, Window parent )
    {
        sendCount = 0;
        sendCountMax = count;
        sendDelay = delay;
        msgContents = contents;
        this.commModel = commModel;
        frame = parent;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run( final ITaskStopManager stopper )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                dialog = new TxDialog( frame, stopper );
                dialog.setMax( sendCountMax );
                dialog.setMessage( "Sending message" );

                JDialog view = dialog.getView();
                view.setSize( 400, 150 );
                view.validate();
                view.setLocationRelativeTo( frame );
                view.setVisible( true );
            }
        } );

        while( stopper.continueProcessing() && sendCount < sendCountMax )
        {
            try
            {
                commModel.txMessage( msgContents );
            }
            catch( IOException ex )
            {
                ex.printStackTrace();
            }

            sendCount++;
            SwingUtilities.invokeLater( new Runnable()
            {
                @Override
                public void run()
                {
                    dialog.setValue( sendCount );
                }
            } );

            if( sendDelay > 50 )
            {
                try
                {
                    Thread.sleep( sendDelay );
                }
                catch( InterruptedException ex )
                {
                    ex.printStackTrace();
                    break;
                }
            }
        }

        SwingUtilities.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                dialog.getView().setVisible( false );
            }
        } );
    }
}
