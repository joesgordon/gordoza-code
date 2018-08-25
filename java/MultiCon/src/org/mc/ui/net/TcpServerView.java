package org.mc.ui.net;

import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.swing.JComponent;

import org.jutils.concurrent.*;
import org.jutils.io.LogUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.net.*;
import org.jutils.ui.net.TcpInputsView;
import org.mc.MulticonMain;
import org.mc.MulticonOptions;
import org.mc.ui.BindingFrameView.IBindableView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpServerView implements IBindableView
{
    /**  */
    public static final String NAME = "TCP Server";

    /**  */
    private final TcpInputsView inputsView;

    /**  */
    private TaskThread acceptThread;

    /***************************************************************************
     * 
     **************************************************************************/
    public TcpServerView()
    {
        this.inputsView = new TcpInputsView( true, true );

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();

        inputsView.setData(
            new TcpInputs( userio.getOptions().tcpServerInputs ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void bind() throws IOException
    {
        TcpInputs inputs = inputsView.getData();

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();
        MulticonOptions options = userio.getDefault();
        options.tcpServerInputs = new TcpInputs( inputs );
        userio.write( options );

        AcceptTask task = new AcceptTask( inputs, this );
        this.acceptThread = new TaskThread( task, "TCP Server Accept" );

        acceptThread.start();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void unbind() throws IOException
    {
        if( acceptThread != null )
        {
            acceptThread.stop();
            acceptThread.interrupt();
            LogUtils.printDebug( "Waiting for" );
            acceptThread.stopAndWait();
            LogUtils.printDebug( ">Waited for" );

            this.acceptThread = null;
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return inputsView.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return NAME;
    }

    /***************************************************************************
     * @param connection
     **************************************************************************/
    private void setAcceptedConnection( TcpConnection connection )
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class AcceptTask implements ITask
    {
        /**  */
        private final TcpInputs inputs;
        /**  */
        private final TcpServerView view;

        /**
         * @param inputs
         * @param view
         */
        public AcceptTask( TcpInputs inputs, TcpServerView view )
        {
            this.inputs = inputs;
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run( ITaskHandler stopManager )
        {
            try( TcpServer server = new TcpServer( inputs ) )
            {
                boolean accepted = false;

                while( !accepted && stopManager.canContinue() )
                {
                    try
                    {
                        @SuppressWarnings( "resource")
                        TcpConnection connection = server.accept();

                        view.setAcceptedConnection( connection );
                        accepted = true;
                    }
                    catch( SocketTimeoutException ex )
                    {
                    }
                }
            }
            catch( IOException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            finally
            {
                view.acceptThread = null;
            }
        }
    }
}
