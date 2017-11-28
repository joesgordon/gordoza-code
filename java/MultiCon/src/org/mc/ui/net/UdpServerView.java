package org.mc.ui.net;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.Charset;

import javax.swing.*;

import org.jutils.SwingUtils;
import org.jutils.Utils;
import org.jutils.io.LogUtils;
import org.jutils.net.*;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.net.NetMessagesView;
import org.jutils.ui.net.UdpInputsView;
import org.mc.ui.IConnectionView;
import org.mc.ui.McConfigurationPanel;

/***************************************************************************
 * 
 **************************************************************************/
public class UdpServerView implements IConnectionView
{
    /**  */
    private final JPanel view;
    /**  */
    private final UdpInputsView inputsView;
    /**  */
    private final McConfigurationPanel configPanel;
    /**  */
    private final NetMessagesView messagesView;

    /**  */
    private Multicaster commModel;
    /**  */
    private UdpConnection connection;

    /***************************************************************************
     * 
     **************************************************************************/
    public UdpServerView()
    {
        this.inputsView = new UdpInputsView( true, false );
        this.configPanel = new McConfigurationPanel( inputsView );
        this.messagesView = new NetMessagesView();
        this.view = createView();

        this.commModel = null;

        UdpInputs inputs = inputsView.getData();

        inputs.localPort = 5000;

        inputsView.setData( inputs );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
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

    /***************************************************************************
     * 
     **************************************************************************/
    private void bindUnbind()
    {
        boolean bound = ( commModel != null );

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
                UdpInputs inputs = inputsView.getData();
                this.connection = new UdpConnection( inputs );

                ItemActionListener<NetMessage> rxListener;
                ItemActionListener<String> errListener;

                rxListener = ( e ) -> SwingUtilities.invokeLater(
                    () -> rxMessage( e.getItem() ) );
                errListener = ( e ) -> SwingUtilities.invokeLater(
                    () -> displayErrorMessage( e.getItem() ) );

                commModel = new Multicaster( connection, rxListener,
                    errListener );
                messagesView.clearMessages();
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

    /***************************************************************************
     * @param msg
     **************************************************************************/
    private void rxMessage( NetMessage msg )
    {
        addMessage( msg );

        byte[] response = new byte[PREFIX.length + msg.contents.length];
        Utils.byteArrayCopy( PREFIX, 0, response, 0, PREFIX.length );
        Utils.byteArrayCopy( msg.contents, 0, response, PREFIX.length,
            msg.contents.length );

        try
        {
            InetAddress addr = InetAddress.getByName( msg.address );
            msg = connection.txMessage( response, addr, msg.port );

            addMessage( msg );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( getView(),
                "ERROR: " + ex.getMessage() );
        }
    }

    /***************************************************************************
     * @param msg
     **************************************************************************/
    private void addMessage( NetMessage msg )
    {
        SwingUtilities.invokeLater( () -> messagesView.addMessage( msg ) );
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

        SwingUtils.showErrorMessage( getView(), errorMsg,
            "Communication Error" );

        bindUnbind();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getTitle()
    {
        return "UDP Server";
    }
}
