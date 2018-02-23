package org.mc.ui.net;

import java.awt.*;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;

import javax.swing.*;

import org.jutils.Utils;
import org.jutils.concurrent.*;
import org.jutils.io.LogUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.net.*;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.net.NetMessagesView;
import org.jutils.ui.net.TcpInputsView;
import org.mc.McMain;
import org.mc.McOptions;
import org.mc.ui.BindView;
import org.mc.ui.IConnectionView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpServerView implements IConnectionView
{
    /**  */
    private final JPanel view;
    /**  */
    private final TcpInputsView inputsView;
    /**  */
    private final BindView configPanel;
    /**  */
    private final NetMessagesView messagesView;

    /**  */
    private ConnectionListener commModel;
    /**  */
    private TcpConnection connection;
    /**  */
    private TaskThread acceptThread;

    /***************************************************************************
     * 
     **************************************************************************/
    public TcpServerView()
    {
        this.inputsView = new TcpInputsView( true, true );
        this.configPanel = new BindView( inputsView );
        this.messagesView = new NetMessagesView();
        this.view = createView();

        this.commModel = null;

        OptionsSerializer<McOptions> userio = McMain.getUserData();

        inputsView.setData( userio.getOptions().tcpServerInputs );
    }

    /**
     * @return
     */
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        inputsView.setEnabled( true );

        configPanel.setCallback( ( b ) -> bindUnbind( b ) );

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

    /**
     * @param bind
     */
    private void bindUnbind( boolean bind )
    {
        configPanel.setBindEnabled( false );

        // LogUtils.printDebug( "model %s, task %s", commModel, acceptThread );

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

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    private void bind() throws IOException
    {
        TcpInputs inputs = inputsView.getData();

        OptionsSerializer<McOptions> userio = McMain.getUserData();
        McOptions options = userio.getDefault();
        options.tcpServerInputs = inputs;
        userio.write( options );

        AcceptTask task = new AcceptTask( inputs, this );
        this.acceptThread = new TaskThread( task, "TCP Server Accept" );

        messagesView.clearMessages();

        acceptThread.start();
    }

    /**  */
    private static final byte[] PREFIX = "Received: ".getBytes(
        Charset.forName( "UTF-8" ) );

    /***************************************************************************
     * @param msg
     **************************************************************************/
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
    public void close()
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
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getTitle()
    {
        return "TCP Server";
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

    /***************************************************************************
     * @param connection
     **************************************************************************/
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
            commModel = new ConnectionListener( connection, rxListener,
                errListener );
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
    private void handleDisconnected()
    {
        LogUtils.printDebug( "Disconnected" );
        bindUnbind( false );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class AcceptTask implements ITask
    {
        private final TcpInputs inputs;
        private final TcpServerView view;

        public AcceptTask( TcpInputs inputs, TcpServerView view )
        {
            this.inputs = inputs;
            this.view = view;
        }

        @Override
        public void run( ITaskHandler stopManager )
        {
            Runnable dc = () -> view.handleDisconnected();

            try( TcpServer server = new TcpServer( inputs ) )
            {
                boolean accepted = false;

                while( !accepted && stopManager.canContinue() )
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
                view.acceptThread = null;
            }
        }
    }
}
