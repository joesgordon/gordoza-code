package org.mc.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.concurrent.Stoppable;
import org.jutils.io.LogUtils;
import org.jutils.net.*;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.IView;
import org.mc.McTxThread;

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
    private Multicaster commModel;

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

        frameView.getView().setIconImages( IconConstants.getImages(
            IconConstants.CHAT_16, IconConstants.CHAT_32, IconConstants.CHAT_48,
            IconConstants.CHAT_64 ) );

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

                McTxThread txThread = new McTxThread( msgCount, msgDelay,
                    msgBytes, commModel.getConnection(), getView() );
                Stoppable stoppable = new Stoppable( txThread );
                Thread thread = new Thread( stoppable );
                thread.start();
            }
            else
            {
                commModel.getConnection().txMessage( msgBytes );
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
    private void bindUnbind()
    {
        boolean bound = ( commModel != null );

        confPanel.setBindEnabled( false );

        try
        {
            if( bound )
            {
                unbindSocket();
                bound = false;
            }
            else
            {
                MulticastInputs socket = confPanel.getSocket();

                ItemActionListener<NetMessage> rxListener;
                ItemActionListener<String> errListener;

                rxListener = ( e ) -> SwingUtilities.invokeLater(
                    () -> addMessage( e.getItem() ) );
                errListener = ( e ) -> SwingUtilities.invokeLater(
                    () -> displayErrorMessage( e.getItem() ) );

                commModel = new Multicaster( socket, rxListener, errListener );
                bound = true;
            }
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( getView(),
                "ERROR: " + ex.getMessage() );
            bound = false;
            ex.printStackTrace();
        }

        confPanel.setBound( bound );
        inputPanel.setBound( bound );
        confPanel.setBindEnabled( true );
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

    /***************************************************************************
     * 
     **************************************************************************/
    private class SendListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            sendMessage();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class BindListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            bindUnbind();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
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
