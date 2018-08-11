package org.mc.ui.net;

import java.io.IOException;
import java.net.SocketException;

import javax.swing.JComponent;

import org.jutils.io.options.OptionsSerializer;
import org.jutils.net.*;
import org.jutils.ui.net.MulticastInputsView;
import org.mc.MulticonMain;
import org.mc.MulticonOptions;
import org.mc.ui.IConnectionView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticastView implements IConnectionView
{
    /**  */
    private final MulticastInputsView inputsView;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticastView()
    {
        this.inputsView = new MulticastInputsView();

        inputsView.setEnabled( true );

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();

        inputsView.setData( userio.getOptions().multicastInputs );
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
    public IConnection createConnection() throws SocketException, IOException
    {
        MulticastInputs inputs = inputsView.getData();

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();
        MulticonOptions options = userio.getOptions();
        options.multicastInputs = inputs;
        userio.write( options );

        return new MulticastConnection( inputs );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        inputsView.setEnabled( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getTitle()
    {
        return "Multicast";
    }
}
