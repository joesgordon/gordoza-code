package org.mc.ui.net;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.*;

import org.jutils.SwingUtils;
import org.jutils.io.LogUtils;
import org.jutils.net.*;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.net.NetMessagesView;
import org.jutils.ui.net.UdpInputsView;
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
    private final McConfigurationPanel configPanel;
    /**  */
    private final NetMessagesView messagesView;
    /**  */
    private final MessageTextView textView;

    /**  */
    private Multicaster commModel;

    /***************************************************************************
     * 
     **************************************************************************/
    public UdpClientView()
    {
        this.inputsView = new UdpInputsView();
        this.configPanel = new McConfigurationPanel( inputsView );
        this.messagesView = new NetMessagesView();
        this.textView = new MessageTextView();
        this.view = createView();

        this.commModel = null;
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
                @SuppressWarnings( "resource")
                IConnection connection = new UdpConnection( inputs );

                ItemActionListener<NetMessage> rxListener;
                ItemActionListener<String> errListener;

                rxListener = ( e ) -> SwingUtilities.invokeLater(
                    () -> messagesView.addMessage( e.getItem() ) );
                errListener = ( e ) -> SwingUtilities.invokeLater(
                    () -> displayErrorMessage( e.getItem() ) );

                commModel = new Multicaster( connection, rxListener,
                    errListener );
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

    /***************************************************************************
     * 
     **************************************************************************/
    private void sendMessage()
    {
        String msgStr = textView.getData();

        msgStr = msgStr.trim();

        if( msgStr.length() < 1 )
        {
            JOptionPane.showMessageDialog( getView(), "Nothing to send",
                "ERROR", JOptionPane.ERROR_MESSAGE );
            return;
        }

        try
        {
            byte[] strBytes = msgStr.getBytes();
            byte[] msgBytes = Arrays.copyOf( strBytes, strBytes.length + 1 );

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

        SwingUtils.showErrorMessage( getView(), errorMsg,
            "Communication Error" );
    }

    @Override
    public String getTitle()
    {
        return "UDP Client";
    }
}
