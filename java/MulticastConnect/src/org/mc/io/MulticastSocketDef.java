package org.mc.io;

import java.net.NetworkInterface;
import java.net.SocketException;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticastSocketDef
{
    /**  */
    public final Ip4Address address;
    /**  */
    public int port;
    /**  */
    public String nic;
    /**  */
    public int ttl;

    public MulticastSocketDef()
    {
        this.address = new Ip4Address();
        this.port = 2048;
        this.nic = null;
        this.ttl = 10;

        address.address[0] = ( byte )238;
        address.address[1] = 0;
        address.address[2] = 0;
        address.address[3] = 1;
    }

    public NetworkInterface getSystemNic() throws SocketException
    {
        return nic == null ? null : NetworkInterface.getByName( nic );
    }
}
