package org.mc.ui;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import org.jutils.SwingUtils;
import org.jutils.io.LogUtils;
import org.jutils.net.*;
import org.jutils.ui.TitleView;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.net.NetMessagesView;
import org.mc.ui.BindingFrameView.IBindableView;

/*******************************************************************************
 *
 ******************************************************************************/
public class ConnectionBindableView implements IBindableView
{
    /**  */
    private final IConnectionView connectionView;

    /**  */
    private final JPanel view;
    /**  */
    private final NetMessagesView messagesPanel;
    /**  */
    private final MessageInputPanel inputPanel;

    /**  */
    private ConnectionListener commModel;

    /***************************************************************************
     * @param connectionView
     **************************************************************************/
    public ConnectionBindableView( IConnectionView connectionView )
    {
        this.connectionView = connectionView;

        this.messagesPanel = new NetMessagesView();
        this.inputPanel = new MessageInputPanel();
        this.view = createView();

        this.commModel = null;

        inputPanel.setEditable( false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        TitleView msgsTitlePanel = new TitleView( "Messages",
            messagesPanel.getView() );

        TitleView cfgTitlePanel = new TitleView( "Configuration",
            connectionView.getView() );

        panel.add( cfgTitlePanel.getView(),
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 6, 6, 4, 6 ), 0, 0 ) );

        panel.add( inputPanel.getView(),
            new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 6, 6, 6 ), 0, 0 ) );

        panel.add( msgsTitlePanel.getView(),
            new GridBagConstraints( 1, 0, 1, 2, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 6, 6, 6 ), 0, 0 ) );

        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * @param bind
     **************************************************************************/
    @Override
    public void bind() throws IOException
    {
        @SuppressWarnings( "resource")
        IConnection connection = connectionView.createConnection();

        setConnection( connection );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void unbind()
    {
        inputPanel.setEditable( false );
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
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return connectionView.getTitle();
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
        byte[] msgBytes = inputPanel.getMessageText();

        if( msgBytes.length < 1 )
        {
            JOptionPane.showMessageDialog( getView(), "Nothing to send",
                "ERROR", JOptionPane.ERROR_MESSAGE );
            return;
        }

        try
        {
            // if( inputPanel.isScheduling() )
            // {
            // int msgCount = inputPanel.getMessageCount();
            // int msgDelay = inputPanel.getSendDelay();
            // Window win = SwingUtils.getComponentsWindow( getView() );
            //
            // McTxThread txThread = new McTxThread( msgCount, msgDelay,
            // msgBytes, commModel.connection, win );
            // TaskThread thread = new TaskThread( txThread,
            // "Multicast Tx Thread" );
            // thread.start();
            // }

            commModel.connection.sendMessage( msgBytes );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( getView(),
                "ERROR: " + ex.getMessage() );
        }
        inputPanel.selectAll();
    }

    /***************************************************************************
     * @param connection
     * @throws IOException
     **************************************************************************/
    private void setConnection( IConnection connection ) throws IOException
    {
        ItemActionListener<NetMessage> rxListener;
        ItemActionListener<String> errListener;

        rxListener = ( e ) -> SwingUtilities.invokeLater(
            () -> addMessage( e.getItem() ) );
        errListener = ( e ) -> SwingUtilities.invokeLater(
            () -> displayErrorMessage( e.getItem() ) );

        commModel = new ConnectionListener( connection, rxListener,
            errListener );

        inputPanel.setConnection( connection );

        connectionView.setEditable( false );
        inputPanel.setEditable( true );
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
        inputPanel.closeConnection();
        messagesPanel.clearMessages();

        try
        {
            commModel.close();
            commModel = null;
        }
        finally
        {
            connectionView.setEditable( true );
        }
    }
}
