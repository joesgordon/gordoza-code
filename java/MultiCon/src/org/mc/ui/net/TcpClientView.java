package org.mc.ui.net;

import java.io.IOException;
import java.net.SocketException;

import javax.swing.JComponent;

import org.jutils.io.options.OptionsSerializer;
import org.jutils.net.*;
import org.jutils.ui.net.TcpInputsView;
import org.mc.MulticonMain;
import org.mc.MulticonOptions;
import org.mc.ui.IConnectionView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpClientView implements IConnectionView
{
    /**  */
    private final TcpInputsView inputsView;

    /***************************************************************************
     * 
     **************************************************************************/
    public TcpClientView()
    {
        this.inputsView = new TcpInputsView( false );

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();

        inputsView.setData( userio.getOptions().tcpClientInputs );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return inputsView.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getTitle()
    {
        return "TCP Client";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IConnection createConnection() throws SocketException, IOException
    {
        TcpInputs inputs = inputsView.getData();

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();
        MulticonOptions options = userio.getOptions();
        options.tcpClientInputs = inputs;
        userio.write( options );

        return new TcpConnection( inputs );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        inputsView.setEnabled( editable );
    }
}
