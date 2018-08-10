package org.mc.ui.net;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.swing.*;

import org.jutils.SwingUtils;
import org.jutils.io.LogUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.net.*;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.net.NetMessagesView;
import org.jutils.ui.net.UdpInputsView;
import org.mc.McMain;
import org.mc.McOptions;
import org.mc.ui.BindView;
import org.mc.ui.IConnectionView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpServerView implements IConnectionView
{
    /**  */
    private final JPanel view;
    /**  */
    private final UdpInputsView inputsView;
    /**  */
    private final BindView configPanel;
    /**  */
    private final NetMessagesView messagesView;

    /**  */
    private ConnectionListener commModel;
    /**  */
    private UdpConnection connection;

    /***************************************************************************
     * 
     **************************************************************************/
    public UdpServerView()
    {
        this.inputsView = new UdpInputsView( true, false );
        this.configPanel = new BindView( inputsView );
        this.messagesView = new NetMessagesView();
        this.view = createView();

        this.commModel = null;

        OptionsSerializer<McOptions> userio = McMain.getUserData();

        inputsView.setData( userio.getOptions().udpServerInputs );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
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

    /***************************************************************************
     * 
     **************************************************************************/
    private void bindUnbind( boolean bind )
    {
        boolean bound = bind;

        LogUtils.printDebug( "UDP Server %s", bind ? "binding" : "unbinding" );

        configPanel.setBindEnabled( false );

        try
        {
            if( bind )
            {
                UdpInputs inputs = inputsView.getData();

                OptionsSerializer<McOptions> userio = McMain.getUserData();
                McOptions options = userio.getOptions();
                options.udpServerInputs = inputs;
                userio.write( options );

                this.connection = new UdpConnection( inputs );

                ItemActionListener<NetMessage> rxListener;
                ItemActionListener<String> errListener;

                rxListener = ( e ) -> SwingUtilities.invokeLater(
                    () -> rxMessage( e.getItem() ) );
                errListener = ( e ) -> SwingUtilities.invokeLater(
                    () -> displayErrorMessage( e.getItem() ) );

                commModel = new ConnectionListener( connection, rxListener,
                    errListener );
                messagesView.clearMessages();
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

    /**  */
    private static final byte[] PREFIX = "Received: ".getBytes(
        Charset.forName( "UTF-8" ) );

    /***************************************************************************
     * @param msg
     **************************************************************************/
    private void rxMessage( NetMessage msg )
    {
        addMessage( msg );
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

        bindUnbind( false );
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
