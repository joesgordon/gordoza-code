package org.mc.ui.net;

import java.io.IOException;
import java.net.SocketException;

import javax.swing.JComponent;

import org.jutils.io.options.OptionsSerializer;
import org.jutils.net.*;
import org.jutils.ui.net.UdpInputsView;
import org.mc.MulticonMain;
import org.mc.MulticonOptions;
import org.mc.ui.IConnectionView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpView implements IConnectionView
{
    /**  */
    private final UdpInputsView inputsView;

    /***************************************************************************
     * 
     **************************************************************************/
    public UdpView()
    {
        this.inputsView = new UdpInputsView();

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();

        inputsView.setData( userio.getOptions().udpClientInputs );
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
        return "UDP";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IConnection createConnection() throws SocketException, IOException
    {
        UdpInputs inputs = inputsView.getData();

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();
        MulticonOptions options = userio.getOptions();
        options.udpClientInputs = inputs;
        userio.write( options );

        return new UdpConnection( inputs );
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
