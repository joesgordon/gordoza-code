package org.jutils.net;

import java.io.Closeable;
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IConnection extends Closeable
{
    /***************************************************************************
     * @param buf
     * @return
     * @throws IOException
     **************************************************************************/
    public NetMessage txMessage( byte [] buf ) throws IOException;

    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    public NetMessage rxMessage() throws IOException;

    /***************************************************************************
     * Returns the first {@link InetAddress} of the network interface name
     * provided or dies if the JRE is strange.
     * @param nicName the name of the network interface to be found.
     * @return The first address of the network interface of the provided name
     * or {@code null} if none found.
     * @throws RuntimeException if the implementing JRE is so strange as to not
     * recognize 0.0.0.0 as a valid address or when a {@link SocketException} is
     * thrown on {@link NetworkInterface#getByName(String)}. The former should
     * never happen and it is unclear from the documentation when/why the latter
     * would happen.
     **************************************************************************/
    public static InetAddress getNicAddress( String nicName )
        throws RuntimeException
    {
        InetAddress nicAddr = null;

        if( nicName != null )
        {
            try
            {
                NetworkInterface nic = NetworkInterface.getByName( nicName );

                nicAddr = getNicAddress( nic );
            }
            catch( SocketException ex )
            {
                throw new RuntimeException(
                    "0.0.0.0 not recognized as a valid address", ex );
            }
        }

        if( nicAddr == null )
        {
            nicAddr = getAnyLocalAddress();
        }

        return nicAddr;
    }

    /***************************************************************************
     * Gets the first address of the provided network interface.
     * @param nic the network interface to get the address of.
     * @return The first address of the network interface of the provided name
     * or {@code null} if none found or privoded interface is {@code null}.
     **************************************************************************/
    public static InetAddress getNicAddress( NetworkInterface nic )
    {
        if( nic != null )
        {
            Enumeration<InetAddress> addresses = nic.getInetAddresses();

            if( addresses.hasMoreElements() )
            {
                return addresses.nextElement();
            }
        }

        return null;
    }

    /***************************************************************************
     * @throws RuntimeException if the implementing JRE is so strange as to not
     * recognize 0.0.0.0 as a valid address.
     **************************************************************************/
    public static InetAddress getAnyLocalAddress()
    {
        byte [] inAddrAnyBytes = new byte[] { 0, 0, 0, 0 };

        try
        {
            return InetAddress.getByAddress( inAddrAnyBytes );
        }
        catch( UnknownHostException ex )
        {
            throw new RuntimeException(
                "0.0.0.0 not recognized as a valid address", ex );
        }

    }
}
