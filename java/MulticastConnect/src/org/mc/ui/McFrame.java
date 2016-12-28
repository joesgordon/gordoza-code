package org.mc.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.*;

import org.jutils.concurrent.Stoppable;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.model.IView;
import org.mc.*;
import org.mc.io.MulticastConnection;
import org.mc.io.MulticastSocketDef;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final McConfigurationPanel confPanel;
    /**  */
    private final McMessagesPanel messagesPanel;
    /**  */
    private final McInputPanel inputPanel;
    /**  */
    private MulticastConnection commModel;
    /**  */
    private McRxThread receiver;
    /**  */
    private Stoppable rxThread;

    /***************************************************************************
     * 
     **************************************************************************/
    public McFrame()
    {
        this.frameView = new StandardFrameView();

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

        mainPanel.add( confPanel.getView(),
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 6, 6, 6, 6 ), 0, 0 ) );
        mainPanel.add( messagesPanel.getView(),
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 6, 6, 6 ), 0, 0 ) );
        mainPanel.add( inputPanel.getView(),
            new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 6, 6, 6 ), 0, 0 ) );

        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 500, 700 );
        frameView.setTitle( "MulticastConnect" );
        frameView.setContent( mainPanel );

        frameView.getView().addWindowListener(
            new ClosingListener( getView() ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
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

                McTxThread txThread = new McTxThread( msgCount, msgDelay,
                    msgBytes, commModel, getView() );
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
            JOptionPane.showMessageDialog( getView(),
                "ERROR: " + ex.getMessage() );
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
                MulticastSocketDef socket = confPanel.getSocket();

                commModel = new MulticastConnection( socket );
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
            JOptionPane.showMessageDialog( getView(),
                "ERROR: " + ex.getMessage() );
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
        @Override
        public void actionPerformed( ActionEvent e )
        {
            connect();
        }
    }

    private final class ClosingListener extends WindowAdapter
    {
        private final Component parent;

        public ClosingListener( Component parent )
        {
            this.parent = parent;
        }

        @Override
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
                    JOptionPane.showMessageDialog( parent,
                        "ERROR: " + ex.getMessage() );
                }
            }
        }
    }
}
