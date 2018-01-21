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
import org.mc.ui.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpClientView implements IConnectionView
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
    private final MessageTextView textView;

    /**  */
    private ConnectionListener commModel;

    /***************************************************************************
     * 
     **************************************************************************/
    public UdpClientView()
    {
        this.inputsView = new UdpInputsView();
        this.configPanel = new BindView( inputsView );
        this.messagesView = new NetMessagesView();
        this.textView = new MessageTextView();
        this.view = createView();

        this.commModel = null;

        OptionsSerializer<McOptions> userio = McMain.getUserData();

        inputsView.setData( userio.getOptions().udpClientInputs );
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
                UdpInputs inputs = inputsView.getData();

                OptionsSerializer<McOptions> userio = McMain.getUserData();
                McOptions options = userio.getOptions();
                options.udpClientInputs = inputs;
                userio.write( options );

                @SuppressWarnings( "resource")
                IConnection connection = new UdpConnection( inputs );

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
            ex.printStackTrace();
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

        String msgStr = textView.getData();

        // msgStr = msgStr.trim();

        if( msgStr.length() < 1 )
        {
            JOptionPane.showMessageDialog( getView(), "Nothing to send",
                "ERROR", JOptionPane.ERROR_MESSAGE );
            return;
        }

        try
        {
            byte[] msgBytes = msgStr.getBytes( Charset.forName( "UTF-8" ) );

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
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * 
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
    }

    @Override
    public String getTitle()
    {
        return "UDP Client";
    }
}
