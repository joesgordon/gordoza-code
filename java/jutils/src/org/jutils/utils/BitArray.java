package org.jutils.utils;

import java.util.*;

import org.jutils.io.BitBuffer;
import org.jutils.ui.hex.HexUtils;

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
     * @param bits
     **************************************************************************/
    public BitArray( BitArray bits )
    {
        this.bits = new ArrayList<>( bits.bits );
    }

    /***************************************************************************
     * @param hexString
     **************************************************************************/
    public BitArray( String hexString ) throws NumberFormatException
    {
        this();

        set( HexUtils.fromHexStringToArray( hexString ) );
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
     * @param binaryString
     * @throws NumberFormatException
     **************************************************************************/
    public void set( String binaryString ) throws NumberFormatException
    {
        bits.clear();

        for( int i = 0; i < binaryString.length(); i++ )
        {
            char c = binaryString.charAt( i );

            if( c == '0' )
            {
                bits.add( false );
            }
            else if( c == '1' )
            {
                bits.add( true );
            }
            else if( c == ' ' )
            {
                continue;
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

    /***************************************************************************
     * Returns the bit at the provided index.
     * @param idx
     * @return
     **************************************************************************/
    public boolean get( int idx )
    {
        return bits.get( idx );
    }

    /***************************************************************************
     * @param bits2
     **************************************************************************/
    public void set( BitArray bits )
    {
        this.bits.clear();
        this.bits.addAll( bits.bits );
    }

    /***************************************************************************
     * Returns a binary representation of this collection of bits.
     **************************************************************************/
    @Override
    public String toString()
    {
        char [] str = new char[bits.size()];

        for( int i = 0; i < str.length; i++ )
        {
            Boolean b = bits.get( i );
            str[i] = b ? '1' : '0';
        }

        return new String( str );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String toHexLeft()
    {
        return HexUtils.toHexString( getLeftAligned() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String toHexRight()
    {
        return HexUtils.toHexString( getRightAligned() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getDescription()
    {
        return String.format( "%s (<- = 0x%s) (-> = 0x%s)", toString(),
            toHexLeft(), toHexRight() );
    }
}
