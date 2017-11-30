package org.jutils.ui.net;

import java.awt.BorderLayout;
import java.awt.Component;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.net.NetMessage;
import org.jutils.ui.hex.ByteBuffer;
import org.jutils.ui.hex.HexPanel;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * Displays a net message by fields and bytes.
 ******************************************************************************/
public class NetMessageView implements IDataView<NetMessage>
{
    /** Callback that builds a string representation of the message content. */
    private final IDataView<NetMessage> msgView;

    /** The main panel for this view. */
    private final JPanel view;
    /** The field for message meta-data (e.g. Rx/Tx, time, remote IP/Port). */
    private final JTextField infoField;
    /** The bytes of the message. */
    private final HexPanel bytesField;

    /** The message being displayed. */
    private NetMessage msg;

    /***************************************************************************
     * 
     **************************************************************************/
    public NetMessageView()
    {
        this( null, false );
    }

    /***************************************************************************
     * @param msgWriter
     * @param addScrollPane
     **************************************************************************/
    public NetMessageView( IDataView<NetMessage> msgWriter,
        boolean addScrollPane )
    {
        this.msgView = msgWriter;

        this.infoField = new JTextField( 5 );
        this.bytesField = new HexPanel();
        this.view = createView( addScrollPane );

        try
        {
            InetAddress addr = InetAddress.getLocalHost();
            setData(
                new NetMessage( true, addr.getHostName(), 0, new byte[0] ) );
        }
        catch( UnknownHostException ex )
        {
            throw new RuntimeException(
                "This system doesn't know what 127.0.0.1 is. Poor system. It's homeless. :_(",
                ex );
        }

    }

    /***************************************************************************
     * @param addScrollPane
     * @return
     **************************************************************************/
    private JPanel createView( boolean addScrollPane )
    {
        JPanel panel = new JPanel( new BorderLayout() );
        JTabbedPane tabs = new JTabbedPane();

        if( msgView != null )
        {
            tabs.add( "Fields", createFieldsPanel( addScrollPane ) );
        }
        tabs.add( "Bytes", createBytesPanel() );

        panel.add( createInfoPanel(), BorderLayout.NORTH );
        panel.add( tabs, BorderLayout.CENTER );

        tabs.requestFocusInWindow();

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createInfoPanel()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        infoField.setEditable( false );
        infoField.setBorder( new EmptyBorder( 0, 0, 4, 0 ) );

        panel.add( infoField, BorderLayout.NORTH );

        return panel;
    }

    /***************************************************************************
     * @param addScrollPane
     * @return
     **************************************************************************/
    private Component createFieldsPanel( boolean addScrollPane )
    {
        JPanel panel = new JPanel( new BorderLayout() );

        Component comp = msgView.getView();

        if( addScrollPane )
        {
            JScrollPane pane = new JScrollPane( msgView.getView() );
            comp = pane;
        }

        panel.add( comp, BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createBytesPanel()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( bytesField.getView(), BorderLayout.CENTER );

        return panel;
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
    public NetMessage getData()
    {
        return msg;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( NetMessage msg )
    {
        this.msg = msg;

        infoField.setText( buildInfoText( msg ) );

        if( msgView != null )
        {
            msgView.setData( msg );
        }

        bytesField.setBuffer( new ByteBuffer( msg.contents ) );
    }

    /***************************************************************************
     * Builds the string that describes the message reception/transmission time
     * and remote address/port.
     * @param msg the message to be described.
     * @return the built string.
     **************************************************************************/
    private static String buildInfoText( NetMessage msg )
    {
        StringBuilder str = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss.SSS" );

        str.append( "Message with " );
        str.append( msg.contents.length );
        str.append( " bytes " );
        str.append( msg.received ? "received " : " transmitted " );
        str.append( "at " );
        str.append( msg.time.format( dtf ) );
        str.append( " from " );
        str.append( msg.address );
        str.append( ":" );
        str.append( msg.port );

        return str.toString();
    }
}
