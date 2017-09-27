package org.mc.ui.net;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.*;

import org.jutils.SwingUtils;
import org.jutils.concurrent.Stoppable;
import org.jutils.io.LogUtils;
import org.jutils.net.*;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.net.MulticastInputsView;
import org.jutils.ui.net.NetMessagesView;
import org.mc.McTxThread;
import org.mc.ui.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticastView implements IConnectionView
{
    /**  */
    private final JPanel view;
    /**  */
    private final MulticastInputsView inputsView;
    /**  */
    private final McConfigurationPanel configPanel;
    /**  */
    private final NetMessagesView messagesPanel;
    /**  */
    private final McInputPanel inputPanel;

    /**  */
    private Multicaster commModel;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticastView()
    {
        this.view = new JPanel();
        this.inputsView = new MulticastInputsView();
        this.configPanel = new McConfigurationPanel( inputsView );
        this.messagesPanel = new NetMessagesView();
        this.inputPanel = new McInputPanel();

        this.commModel = null;

        inputsView.setEnabled( true );

        configPanel.addBindActionListener( ( e ) -> bindUnbind() );

        inputPanel.addSendActionListener( ( e ) -> sendMessage() );

        // ---------------------------------------------------------------------
        // Setup main panel
        // ---------------------------------------------------------------------
        view.setLayout( new GridBagLayout() );

        view.add( configPanel.getView(),
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 6, 6, 6, 6 ), 0, 0 ) );
        view.add( messagesPanel.getView(),
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 6, 6, 6 ), 0, 0 ) );
        view.add( inputPanel.getView(),
            new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 6, 6, 6 ), 0, 0 ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
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
                unbindSocket();
            }
            catch( IOException ex )
            {
                SwingUtils.showErrorMessage( getView(),
                    "Unable to properly close multicast connection: " +
                        ex.getMessage(),
                    "Unbind Error" );
            }
        }
    }

    /***************************************************************************
     * @param msg
     **************************************************************************/
    public void addMessage( NetMessage msg )
    {
        // LogUtils.printDebug(
        // "rx'd msg: " + HexUtils.toHexString( msg.contents ) );
        messagesPanel.addMessage( msg );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void sendMessage()
    {
        String msgStr = inputPanel.getMessageText();

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

            if( inputPanel.isScheduling() )
            {
                int msgCount = inputPanel.getMessageCount();
                int msgDelay = inputPanel.getSendDelay();
                Window win = SwingUtils.getComponentsWindow( getView() );

                McTxThread txThread = new McTxThread( msgCount, msgDelay,
                    msgBytes, commModel.connection, win );
                Stoppable stoppable = new Stoppable( txThread );
                Thread thread = new Thread( stoppable );
                thread.start();
            }
            else
            {
                commModel.connection.txMessage( msgBytes );
            }
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( getView(),
                "ERROR: " + ex.getMessage() );
        }
        inputPanel.selectAll();
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
                unbindSocket();
                bound = false;
            }
            else
            {
                MulticastInputs socket = inputsView.getData();
                @SuppressWarnings( "resource")
                IConnection connection = new MulticastConnection( socket );

                ItemActionListener<NetMessage> rxListener;
                ItemActionListener<String> errListener;

                rxListener = ( e ) -> SwingUtilities.invokeLater(
                    () -> addMessage( e.getItem() ) );
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
        inputPanel.setBound( bound );
        configPanel.setBindEnabled( true );
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

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    private void unbindSocket() throws IOException
    {
        messagesPanel.clearMessages();

        commModel.close();
        commModel = null;
    }

    @Override
    public String getTitle()
    {
        return "Multicast";
    }
}
