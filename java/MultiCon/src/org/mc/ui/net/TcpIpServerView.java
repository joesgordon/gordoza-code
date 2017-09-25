package org.mc.ui.net;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.swing.*;

import org.jutils.Utils;
import org.jutils.concurrent.*;
import org.jutils.io.LogUtils;
import org.jutils.net.*;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.net.TcpInputsView;
import org.mc.ui.*;

/***************************************************************************
 * 
 **************************************************************************/
public class TcpIpServerView implements IConnectionView
{
    private final JPanel view;
    private final TcpInputsView inputsView;
    private final McConfigurationPanel configPanel;
    private final NetMessagesView messagesView;

    private Multicaster commModel;
    private TcpConnection connection;
    private Thread acceptThread;
    private Stoppable acceptTask;

    public TcpIpServerView()
    {
        this.inputsView = new TcpInputsView( true, true );
        this.configPanel = new McConfigurationPanel( inputsView );
        this.messagesView = new NetMessagesView();
        this.view = createView();

        this.commModel = null;
    }

    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        inputsView.setEnabled( true );

        configPanel.addBindActionListener( ( e ) -> bindUnbind() );

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

    private void bindUnbind()
    {
        boolean bound = ( commModel != null || acceptTask != null );

        configPanel.setBindEnabled( false );

        try
        {
            if( bound )
            {
                close();
                bound = false;
            }
            else
            {
                TcpInputs inputs = inputsView.getData();
                Runnable dc = () -> displayErrorMessage( "Disconnected" );
                this.connection = new TcpConnection( inputs, dc );

                AcceptTask task = new AcceptTask( connection, this );
                this.acceptTask = new Stoppable( task );
                this.acceptThread = new Thread( acceptTask );

                acceptThread.start();
                bound = true;
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
                acceptTask.stopAndWaitFor();
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
                messagesView.clearMessages();

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

    /***************************************************************************
     * 
     **************************************************************************/
    private static class AcceptTask implements IStoppableTask
    {
        private final TcpConnection connection;
        private final TcpIpServerView view;

        public AcceptTask( TcpConnection connection, TcpIpServerView view )
        {
            this.connection = connection;
            this.view = view;
        }

        @Override
        public void run( ITaskStopManager stopManager )
        {
            try
            {
                boolean accepted = false;

                while( !accepted && stopManager.continueProcessing() )
                {
                    accepted = connection.accept();
                }

                view.setAcceptedConnection( connection );
            }
            catch( IOException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
