package org.mc.ui.net;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import org.jutils.SwingUtils;
import org.jutils.io.LogUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.net.*;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.net.MulticastInputsView;
import org.jutils.ui.net.NetMessagesView;
import org.mc.McMain;
import org.mc.McOptions;
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
    private final BindView configPanel;
    /**  */
    private final NetMessagesView messagesPanel;
    /**  */
    private final MsgInputPanel inputPanel;

    /**  */
    private ConnectionListener commModel;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticastView()
    {
        this.view = new JPanel();
        this.inputsView = new MulticastInputsView();
        this.configPanel = new BindView( inputsView );
        this.messagesPanel = new NetMessagesView();
        this.inputPanel = new MsgInputPanel();

        this.commModel = null;

        inputsView.setEnabled( true );

        configPanel.setCallback( ( b ) -> bindUnbind( b ) );

        inputPanel.addSendListener( ( e ) -> sendMessage() );

        // ---------------------------------------------------------------------
        // Setup main panel
        // ---------------------------------------------------------------------
        view.setLayout( new GridBagLayout() );

        view.add( configPanel.getView(),
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 6, 6, 4, 6 ), 0, 0 ) );

        view.add( inputPanel.getView(),
            new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 6, 6, 6 ), 0, 0 ) );

        view.add( messagesPanel.getView(),
            new GridBagConstraints( 1, 0, 1, 2, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 6, 6, 6 ), 0, 0 ) );

        OptionsSerializer<McOptions> userio = McMain.getUserData();

        inputsView.setData( userio.getOptions().multicastInputs );
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
     * {@inheritDoc}
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
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getTitle()
    {
        return "Multicast";
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

            commModel.connection.txMessage( msgBytes );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( getView(),
                "ERROR: " + ex.getMessage() );
        }
        inputPanel.selectAll();
    }

    /***************************************************************************
     * @param bind
     **************************************************************************/
    private void bindUnbind( boolean bind )
    {
        configPanel.setBindEnabled( false );

        boolean bound = bind;

        try
        {
            if( bind )
            {
                MulticastInputs inputs = inputsView.getData();

                OptionsSerializer<McOptions> userio = McMain.getUserData();
                McOptions options = userio.getOptions();
                options.multicastInputs = inputs;
                userio.write( options );

                @SuppressWarnings( "resource")
                IConnection connection = new MulticastConnection( inputs );

                ItemActionListener<NetMessage> rxListener;
                ItemActionListener<String> errListener;

                rxListener = ( e ) -> SwingUtilities.invokeLater(
                    () -> addMessage( e.getItem() ) );
                errListener = ( e ) -> SwingUtilities.invokeLater(
                    () -> displayErrorMessage( e.getItem() ) );

                commModel = new ConnectionListener( connection, rxListener,
                    errListener );
                bound = true;
            }
            else
            {
                unbindSocket();
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
        inputPanel.setEditable( bound );
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
}
