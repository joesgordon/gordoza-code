package org.mc.io;

import java.net.InetAddress;
import java.net.UnknownHostException;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ip4Address
{
    /**  */
    public final byte[] address;

    /***************************************************************************
     * 
     **************************************************************************/
    public Ip4Address()
    {
        this.address = new byte[4];
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        String str = "";

        for( byte b : address )
        {
            if( !str.isEmpty() )
            {
                str += ".";
            }

            str += Integer.toString( Byte.toUnsignedInt( b ) );
        }

        return str;
    }

    /***************************************************************************
     * @return
     * @throws UnknownHostException
     **************************************************************************/
    public InetAddress getInetAddress() throws UnknownHostException
    {
        return InetAddress.getByAddress( address );
    }
}
