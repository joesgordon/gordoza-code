package org.jutils.net;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Objects;

import org.jutils.io.Ip4Address;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticastInputs
{
    /**  */
    public final Ip4Address address;
    /**  */
    public int port;
    /**  */
    public String nic;
    /**  */
    public int ttl;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticastInputs()
    {
        this.address = new Ip4Address( 238, 0, 0, 1 );
        this.port = 2048;
        this.nic = null;
        this.ttl = 10;
    }

    /***************************************************************************
     * @param inputs
     **************************************************************************/
    public MulticastInputs( MulticastInputs inputs )
    {
        this.address = new Ip4Address( inputs.address );
        this.port = inputs.port;
        this.nic = inputs.nic;
        this.ttl = inputs.ttl;
    }

    /***************************************************************************
     * @return
     * @throws SocketException
     **************************************************************************/
    public NetworkInterface getSystemNic() throws SocketException
    {
        return nic == null ? null : NetworkInterface.getByName( nic );
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }
        else if( obj instanceof MulticastInputs )
        {
            MulticastInputs inputs = ( MulticastInputs )obj;
            return address.equals( inputs.address ) && port == inputs.port &&
                nic == inputs.nic && ttl == inputs.ttl;
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( address, port, nic, ttl );
    }
}
