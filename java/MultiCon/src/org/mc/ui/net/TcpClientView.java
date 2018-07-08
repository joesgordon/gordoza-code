package org.mc.ui.net;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import org.jutils.io.LogUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.net.*;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.net.NetMessagesView;
import org.jutils.ui.net.TcpInputsView;
import org.mc.McMain;
import org.mc.McOptions;
import org.mc.ui.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpClientView implements IConnectionView
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
    private final MessageTextView textView;

    /**  */
    private ConnectionListener commModel;

    /***************************************************************************
     * 
     **************************************************************************/
    public TcpClientView()
    {
        this.inputsView = new TcpInputsView( false );
        this.configPanel = new BindView( inputsView );
        this.messagesView = new NetMessagesView();
        this.textView = new MessageTextView();
        this.view = createView();

        this.commModel = null;

        OptionsSerializer<McOptions> userio = McMain.getUserData();

        inputsView.setData( userio.getOptions().tcpClientInputs );
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

        textView.addEnterListener( ( e ) -> sendMessage() );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( configPanel.getView(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( messagesView.getView(), constraints );

        textView.getView().setPreferredSize(
            messagesView.getView().getPreferredSize() );
        textView.getView().setMinimumSize(
            messagesView.getView().getMinimumSize() );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( textView.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * @param bind
     **************************************************************************/
    private void bindUnbind( boolean bind )
    {
        boolean bound = bind;

        configPanel.setBindEnabled( false );

        try
        {
            if( bind )
            {
                TcpInputs inputs = inputsView.getData();

                OptionsSerializer<McOptions> userio = McMain.getUserData();
                McOptions options = userio.getOptions();
                options.tcpClientInputs = inputs;
                userio.write( options );

                Runnable dc = () -> displayErrorMessage( "Disconnected" );
                @SuppressWarnings( "resource")
                IConnection connection = new TcpConnection( inputs, dc );

                ItemActionListener<NetMessage> rxListener;
                ItemActionListener<String> errListener;

                rxListener = ( e ) -> SwingUtilities.invokeLater(
                    () -> messagesView.addMessage( e.getItem() ) );
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
            // ex.printStackTrace();
            commModel = null;
        }

        configPanel.setBound( bound );
        configPanel.setBindEnabled( true );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void sendMessage()
    {
        if( commModel == null )
        {
            return;
        }

        byte[] msgBytes = textView.getData();

        if( msgBytes.length < 1 )
        {
            JOptionPane.showMessageDialog( getView(), "Nothing to send",
                "ERROR", JOptionPane.ERROR_MESSAGE );
            return;
        }

        try
        {
            NetMessage msg = commModel.connection.txMessage( msgBytes );

            messagesView.addMessage( msg );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( getView(),
                "ERROR: " + ex.getMessage() );
        }
        textView.selectAll();
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
        configPanel.setBindEnabled( true );
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
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getTitle()
    {
        return "TCP Client";
    }
}
