package org.mc.ui.net;

import java.awt.*;
import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.swing.JComponent;
import javax.swing.JPanel;

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
    private final JPanel view;
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
        this.view = createView();

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();

        inputsView.setData( userio.getOptions().tcpServerInputs );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        inputsView.setEnabled( true );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( inputsView.getView(), constraints );

        return panel;
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
        options.tcpServerInputs = inputs;
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
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "TCP Server";
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

    public void setAcceptedConnection( TcpConnection connection )
    {
        // TODO Auto-generated method stub
    }
}
