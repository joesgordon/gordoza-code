package org.jutils.io;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.jutils.Utils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ip4Address
{
    /**  */
    public final byte [] address;

    /***************************************************************************
     * 
     **************************************************************************/
    public Ip4Address()
    {
        this.address = new byte[4];
    }

    /***************************************************************************
     * @param f1
     * @param f2
     * @param f3
     * @param f4
     **************************************************************************/
    public Ip4Address( int f1, int f2, int f3, int f4 )
    {
        this();

        set( f1, f2, f3, f4 );
    }

    /***************************************************************************
     * @param address
     **************************************************************************/
    public Ip4Address( Ip4Address address )
    {
        this();

        if( address != null && address.address != null )
        {
            set( address );
        }
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

    /***************************************************************************
     * @param f1
     * @param f2
     * @param f3
     * @param f4
     **************************************************************************/
    public void set( int f1, int f2, int f3, int f4 )
    {
        address[0] = ( byte )f1;
        address[1] = ( byte )f2;
        address[2] = ( byte )f3;
        address[3] = ( byte )f4;
    }

    /***************************************************************************
     * @param address
     **************************************************************************/
    public void set( Ip4Address address )
    {
        Utils.byteArrayCopy( address.address, 0, this.address, 0, 4 );
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }
        else if( obj instanceof Ip4Address )
        {
            Ip4Address address = ( Ip4Address )obj;

            return Arrays.equals( this.address, address.address );
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode( address );
    }
}
