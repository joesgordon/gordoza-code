package org.mc.ui;

import java.awt.*;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jutils.SwingUtils;
import org.jutils.io.LogUtils;
import org.jutils.net.*;
import org.jutils.ui.TitleView;
import org.jutils.ui.event.updater.IUpdater;
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

        IUpdater<NetMessage> msgNotifier = ( m ) -> SwingUtilities.invokeLater(
            () -> addMessage( m ) );

        this.messagesPanel = new NetMessagesView();
        this.inputPanel = new MessageInputPanel( msgNotifier );
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
    public void unbind() throws IOException
    {
        inputPanel.setEditable( false );
        inputPanel.close();

        messagesPanel.clearMessages();

        if( commModel != null )
        {
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
     * @param connection
     * @throws IOException
     **************************************************************************/
    private void setConnection( IConnection connection ) throws IOException
    {
        IUpdater<NetMessage> rxListener;
        IUpdater<String> errListener;

        rxListener = ( m ) -> SwingUtilities.invokeLater(
            () -> addMessage( m ) );
        errListener = ( m ) -> SwingUtilities.invokeLater(
            () -> displayErrorMessage( m ) );

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
}
