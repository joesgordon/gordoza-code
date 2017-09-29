package org.mc.ui.net;

import java.awt.*;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;

import javax.swing.*;

import org.jutils.Utils;
import org.jutils.concurrent.*;
import org.jutils.io.LogUtils;
import org.jutils.net.*;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.net.NetMessagesView;
import org.jutils.ui.net.TcpInputsView;
import org.mc.ui.IConnectionView;
import org.mc.ui.McConfigurationPanel;

/***************************************************************************
 * 
 **************************************************************************/
public class TcpServerView implements IConnectionView
{
    private final JPanel view;
    private final TcpInputsView inputsView;
    private final McConfigurationPanel configPanel;
    private final NetMessagesView messagesView;

    private Multicaster commModel;
    private TcpConnection connection;
    private Thread acceptThread;
    private Stoppable acceptTask;

    public TcpServerView()
    {
        this.inputsView = new TcpInputsView( true, true );
        this.configPanel = new McConfigurationPanel( inputsView );
        this.messagesView = new NetMessagesView();
        this.view = createView();

        this.commModel = null;

        TcpInputs inputs = inputsView.getData();

        inputs.localPort = 5000;

        inputsView.setData( inputs );
    }

    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        inputsView.setEnabled( true );

        configPanel.addBindActionListener(
            ( e ) -> bindUnbind( !isBinding() ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( configPanel.getView(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( messagesView.getView(), constraints );

        return panel;
    }

    private boolean isBinding()
    {
        return commModel != null || acceptTask != null;
    }

    private void bindUnbind( boolean bind )
    {
        configPanel.setBindEnabled( false );

        LogUtils.printDebug( "model %s, task %s", commModel, acceptTask );

        boolean bound = !bind;

        try
        {
            if( bind )
            {
                bind();
                bound = true;
            }
            else
            {
                close();
                bound = false;
            }

            inputsView.setEnabled( !bound );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( getView(),
                "ERROR: " + ex.getMessage() );
            bound = false;
            ex.printStackTrace();
        }

        configPanel.setBound( bound );

        configPanel.setBindEnabled( true );
    }

    private void bind() throws IOException
    {
        TcpInputs inputs = inputsView.getData();

        AcceptTask task = new AcceptTask( inputs, this );
        this.acceptTask = new Stoppable( task );
        this.acceptThread = new Thread( acceptTask );

        messagesView.clearMessages();

        acceptThread.start();
    }

    /**  */
    private static final byte[] PREFIX = "Received: ".getBytes(
        Charset.forName( "UTF-8" ) );

    /**
     * @param msg
     */
    private void rxMessage( NetMessage msg )
    {
        messagesView.addMessage( msg );

        byte[] response = new byte[PREFIX.length + msg.contents.length];
        Utils.byteArrayCopy( PREFIX, 0, response, 0, PREFIX.length );
        Utils.byteArrayCopy( msg.contents, 0, response, PREFIX.length,
            msg.contents.length );

        try
        {
            msg = connection.txMessage( response );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( getView(),
                "ERROR: " + ex.getMessage() );
        }
        messagesView.addMessage( msg );
    }

    @Override
    public JComponent getView()
    {
        return view;
    }

    @Override
    public void close()
    {
        if( acceptThread != null )
        {
            acceptTask.stop();
            acceptThread.interrupt();
            try
            {
                LogUtils.printDebug( "Waiting for" );
                acceptTask.stopAndWaitFor();
                LogUtils.printDebug( ">Waited for" );
            }
            catch( InterruptedException ex )
            {
            }

            this.acceptTask = null;
            this.acceptThread = null;
        }

        if( commModel != null )
        {
            try
            {
                commModel.close();
                commModel = null;
            }
            catch( IOException ex )
            {
                displayErrorMessage(
                    "Unable to close connection: " + ex.getMessage() );
            }
        }
    }

    /***************************************************************************
     * @param errorMsg
     **************************************************************************/
    private void displayErrorMessage( String errorMsg )
    {
        LogUtils.printError( errorMsg );

        // SwingUtils.showErrorMessage( getView(), errorMsg,
        // "Communication Error" );

        close();
    }

    private void setAcceptedConnection( TcpConnection connection )
    {
        this.connection = connection;

        ItemActionListener<NetMessage> rxListener;
        ItemActionListener<String> errListener;

        rxListener = ( e ) -> SwingUtilities.invokeLater(
            () -> rxMessage( e.getItem() ) );
        errListener = ( e ) -> SwingUtilities.invokeLater(
            () -> displayErrorMessage( e.getItem() ) );

        try
        {
            commModel = new Multicaster( connection, rxListener, errListener );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
        }

        acceptThread = null;
    }

    public void handleDisconnected()
    {
        LogUtils.printDebug( "Disconnected" );
        bindUnbind( false );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class AcceptTask implements IStoppableTask
    {
        private final TcpInputs inputs;
        private final TcpServerView view;

        public AcceptTask( TcpInputs inputs, TcpServerView view )
        {
            this.inputs = inputs;
            this.view = view;
        }

        @Override
        public void run( ITaskStopManager stopManager )
        {
            Runnable dc = () -> view.handleDisconnected();

            try( TcpServer server = new TcpServer( inputs ) )
            {
                boolean accepted = false;

                while( !accepted && stopManager.continueProcessing() )
                {
                    try
                    {
                        @SuppressWarnings( "resource")
                        TcpConnection connection = server.accept( dc );
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
                view.acceptTask = null;
                view.acceptThread = null;
            }
        }
    }

    @Override
    public String getTitle()
    {
        return "TCP Server";
    }
}
