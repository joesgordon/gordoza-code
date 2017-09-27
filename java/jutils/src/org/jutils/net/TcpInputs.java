package org.jutils.net;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpInputs
{
    /**  */
    public String nic;
    /**  */
    public int localPort;
    /**  */
    public Ip4Address remoteAddress;
    /**  */
    public int remotePort;
    /**
     * The number of milliseconds to block for communications. Must be > -1. 0
     * is interpreted as an infinite timeout.
     */
    public int timeout;

    /***************************************************************************
     * 
     **************************************************************************/
    public TcpInputs()
    {
        this.nic = null;
        this.localPort = 0;
        this.remoteAddress = new Ip4Address( 127, 0, 0, 1 );
        this.remotePort = 80;
        this.timeout = 500;
    }
}
