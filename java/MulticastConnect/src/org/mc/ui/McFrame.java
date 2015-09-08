package org.mc.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Arrays;

import javax.swing.*;

import org.jutils.concurrent.Stoppable;
import org.mc.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McFrame extends JFrame
{
    /**  */
    private McConfigurationPanel confPanel;
    /**  */
    private McMessagesPanel messagesPanel;
    /**  */
    private McInputPanel inputPanel;
    /**  */
    private McComm commModel;
    /**  */
    private McRxThread receiver;
    private Stoppable rxThread;

    /***************************************************************************
     * 
     **************************************************************************/
    public McFrame()
    {
        commModel = null;

        // ---------------------------------------------------------------------
        // Setup configuration panel.
        // ---------------------------------------------------------------------
        confPanel = new McConfigurationPanel();

        confPanel.addBindActionListener( new BindListener() );

        // ---------------------------------------------------------------------
        // Setup display panel
        // ---------------------------------------------------------------------
        messagesPanel = new McMessagesPanel();

        // ---------------------------------------------------------------------
        // Setup send panel
        // ---------------------------------------------------------------------
        inputPanel = new McInputPanel();

        inputPanel.addSendActionListener( new SendListener() );

        // ---------------------------------------------------------------------
        // Setup main panel
        // ---------------------------------------------------------------------
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout( new GridBagLayout() );

        mainPanel.add( confPanel,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 6, 6, 6, 6 ), 0, 0 ) );
        mainPanel.add( messagesPanel,
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 6, 6, 6 ), 0, 0 ) );
        mainPanel.add( inputPanel,
            new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 6, 6, 6 ), 0, 0 ) );

        setTitle( "MulticastConnect" );
        setContentPane( mainPanel );
        addWindowListener( new ClosingListener() );
    }

    /***************************************************************************
     * @param msg
     **************************************************************************/
    public void addMessage( McMessage msg )
    {
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
            JOptionPane.showMessageDialog( this, "Nothing to send", "ERROR",
                JOptionPane.ERROR_MESSAGE );
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

                McTxThread txThread = new McTxThread( msgCount, msgDelay,
                    msgBytes, commModel, this );
                Stoppable stoppable = new Stoppable( txThread );
                Thread thread = new Thread( stoppable );
                thread.start();
            }
            else
            {
                commModel.txMessage( msgBytes );
            }
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( this, "ERROR: " + ex.getMessage() );
        }
        inputPanel.setMessageText( "" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void connect()
    {
        boolean bound = confPanel.isBound();

        confPanel.setBindEnabled( false );

        try
        {
            if( bound )
            {
                String addressString = confPanel.getAddress();
                int port = confPanel.getPort();
                int ttl = confPanel.getTTL();
                int msgSize = confPanel.getMessageSize();
                NetworkInterface nic = confPanel.getNic();

                commModel = new McComm( addressString, port, ttl, msgSize,
                    nic );
                receiver = new McRxThread( this, commModel );
                rxThread = new Stoppable( receiver );
                Thread thread = new Thread( rxThread );
                thread.start();
            }
            else
            {
                unbindSocket();
            }
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( this, "ERROR: " + ex.getMessage() );
            bound = false;
        }

        confPanel.setBound( bound );
        inputPanel.setBound( bound );

        confPanel.setBindEnabled( true );
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    private void unbindSocket() throws IOException
    {
        try
        {
            rxThread.stopAndWaitFor();
        }
        catch( InterruptedException e )
        {
        }

        messagesPanel.clearMessages();
        commModel.close();
        commModel = null;
    }

    private class SendListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            sendMessage();
        }
    }

    private class BindListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            connect();
        }
    }

    private class ClosingListener extends WindowAdapter
    {
        public void windowClosing( WindowEvent e )
        {
            if( commModel != null )
            {
                try
                {
                    unbindSocket();
                }
                catch( IOException ex )
                {
                    JOptionPane.showMessageDialog( McFrame.this,
                        "ERROR: " + ex.getMessage() );
                }
            }
        }
    }
}
