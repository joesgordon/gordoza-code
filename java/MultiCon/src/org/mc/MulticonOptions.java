package org.mc;

import org.jutils.net.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticonOptions
{
    /**  */
    public TcpInputs tcpServerInputs;
    /**  */
    public TcpInputs tcpClientInputs;
    /**  */
    public UdpInputs udpServerInputs;
    /**  */
    public UdpInputs udpClientInputs;
    /**  */
    public MulticastInputs multicastInputs;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticonOptions()
    {
        this.tcpServerInputs = new TcpInputs();
        this.tcpClientInputs = new TcpInputs();
        this.udpServerInputs = new UdpInputs();
        this.udpClientInputs = new UdpInputs();
        this.multicastInputs = new MulticastInputs();
    }

    /***************************************************************************
     * @param options the options to be copied.
     **************************************************************************/
    public MulticonOptions( MulticonOptions options )
    {
        this.tcpServerInputs = options.tcpServerInputs == null ? new TcpInputs()
            : new TcpInputs( options.tcpServerInputs );
        this.tcpClientInputs = options.tcpClientInputs == null ? new TcpInputs()
            : new TcpInputs( options.tcpClientInputs );
        this.udpServerInputs = options.udpServerInputs == null ? new UdpInputs()
            : new UdpInputs( options.udpServerInputs );
        this.udpClientInputs = options.udpClientInputs == null ? new UdpInputs()
            : new UdpInputs( options.udpClientInputs );
        this.multicastInputs = options.multicastInputs == null
            ? new MulticastInputs()
            : new MulticastInputs( options.multicastInputs );
    }
}
