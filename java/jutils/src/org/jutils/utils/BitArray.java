package org.jutils.utils;

import java.util.*;

import org.jutils.io.BitBuffer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BitArray implements Iterable<Boolean>
{
    /**  */
    private final List<Boolean> bits;

    /***************************************************************************
     * 
     **************************************************************************/
    public BitArray()
    {
        bits = new ArrayList<>( 64 );
    }

    /***************************************************************************
     * @param bytes
     **************************************************************************/
    public void set( byte [] bytes )
    {
        bits.clear();

        BitBuffer buf = new BitBuffer( bytes );

        for( int i = 0; i < buf.bitCount(); i++ )
        {
            bits.add( buf.readBit() );
        }
    }

    /***************************************************************************
     * @param str
     * @throws NumberFormatException
     **************************************************************************/
    public void set( String str ) throws NumberFormatException
    {
        bits.clear();

        for( int i = 0; i < str.length(); i++ )
        {
            char c = str.charAt( i );

            if( c == '0' )
            {
                bits.add( false );
            }
            else if( c == '1' )
            {
                bits.add( true );
            }
            else
            {
                throw new NumberFormatException( "Invalid binary character '" +
                    c + "' found at index " + i );
            }
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<Boolean> toList()
    {
        return new ArrayList<>( bits );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public byte [] getLeftAligned()
    {
        int byteCount = ( bits.size() + 7 ) / 8;
        byte [] bytes = new byte[byteCount];
        if( byteCount > 0 )
        {
            BitBuffer buf = new BitBuffer( bytes );

            for( Boolean b : bits )
            {
                buf.writeBit( b );
            }
        }

        return bytes;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public byte [] getRightAligned()
    {
        int byteCount = ( bits.size() + 7 ) / 8;
        byte [] bytes = new byte[byteCount];

        if( byteCount > 0 )
        {
            BitBuffer buf = new BitBuffer( bytes );
            int bit = ( 8 - bits.size() % 8 ) % 8;

            buf.setPosition( 0, bit );

            for( Boolean b : bits )
            {
                buf.writeBit( b );
            }
        }

        return bytes;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int size()
    {
        return bits.size();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Iterator<Boolean> iterator()
    {
        return bits.iterator();
    }

}
