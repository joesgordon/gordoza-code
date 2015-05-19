package org.jutils.io;

import org.jutils.utils.BitArray;

/*******************************************************************************
 * Wraps a byte array and provides bit by bit access to the data.
 ******************************************************************************/
public class BitBuffer
{
    /** Masks needed to set a particular bit. */
    public static final byte [] SET_MASKS;
    /** Masks needed to clear a particular bit. */
    public static final byte [] CLEAR_MASKS;

    static
    {
        SET_MASKS = new byte[8];
        CLEAR_MASKS = new byte[SET_MASKS.length];

        for( int i = 0; i < SET_MASKS.length; i++ )
        {
            SET_MASKS[i] = ( byte )( 1 << i );
            CLEAR_MASKS[i] = ( byte )~SET_MASKS[i];
        }
    }

    /** The byte array to be used. */
    public final byte [] buffer;
    /** The current position into the byte array. */
    private final BitPosition position;
    /** The number of bits in this buffer. */
    private final int bits;

    /***************************************************************************
     * Creates a new buffer with the provided number of bits.
     * @param bitCount the length of the buffer to be created.
     **************************************************************************/
    public BitBuffer( int bitCount )
    {
        this( new byte[( bitCount + 7 ) / 8] );
    }

    /***************************************************************************
     * Creates a new buffer using the provided byte array as the internal
     * buffer.
     * @param buffer the byte array to read to/write from.
     **************************************************************************/
    public BitBuffer( byte [] buffer )
    {
        this.buffer = buffer;
        this.position = new BitPosition();
        this.bits = buffer.length * 8;
    }

    /***************************************************************************
     * Reads bits from this buffer and places them in the provided buffer.
     * @param buf the buffer to which bits will be written.
     * @param bitCount the number of bits to read/write.
     **************************************************************************/
    public void writeTo( BitBuffer buf, int bitCount )
    {
        // int remaining = remainingBits();
        //
        // if( bitCount > remaining )
        // {
        // throw new IllegalArgumentException(
        // "Too few bits remaining in buffer; expected >= " + bitCount +
        // ", found " + remaining );
        // }

        for( int i = 0; i < bitCount; i++ )
        {
            buf.writeBit( this.readBit() );
        }
    }

    /***************************************************************************
     * Sets the position of the next read/write operation.
     * @param byteIndex the index of the byte.
     * @param bitIndex the index of the bit (0 - 7).
     * @throws IllegalArgumentException if either the byte or bit index is
     * invalid.
     **************************************************************************/
    public void setPosition( int byteIndex, int bitIndex )
        throws IllegalArgumentException
    {
        if( byteIndex >= buffer.length )
        {
            throw new IllegalArgumentException( "The byte index (" + byteIndex +
                ") must be < the buffer length (" + buffer.length + ")" );
        }

        position.set( byteIndex, bitIndex );
    }

    /***************************************************************************
     * Sets the absolute bit index position.
     * @param bitIndex the index of the bit.
     * @throws IllegalArgumentException if the bit index is invalid (< 0 or >=
     * bit count).
     **************************************************************************/
    public void setPosition( int bitIndex ) throws IllegalArgumentException
    {
        if( bitIndex >= bitCount() )
        {
            throw new IllegalArgumentException( "The bit index (" + bitIndex +
                ") must be < the bit count (" + bitCount() + ")" );
        }

        int bytePos = bitIndex / 8;
        int bitPos = bitIndex % 8;

        setPosition( bytePos, bitPos );
    }

    /***************************************************************************
     * Increments the current position by the provided number of bits.
     * @param bitCount the number of bits to seek from the current position.
     * @throws IllegalArgumentException if the resultant index is invalid.
     **************************************************************************/
    public void increment( int bitCount )
    {
        BitPosition pos = new BitPosition( position );

        pos.increment( bitCount );

        if( pos.getByte() >= buffer.length )
        {
            throw new IllegalArgumentException( "The byte index (" +
                position.getByte() + ") must be < the buffer length (" +
                buffer.length + ")" );
        }

        position.set( pos );
    }

    /***************************************************************************
     * Sets the current position to the provided position.
     * @param pos the bit position at which to set the current I/O pointer.
     **************************************************************************/
    public void setPosition( BitPosition pos )
    {
        position.set( pos );
    }

    /***************************************************************************
     * Sets the read/write position to the end of the buffer.
     **************************************************************************/
    public void seekToEnd()
    {
        position.set( buffer.length, 0 );
    }

    /***************************************************************************
     * Returns the position of the next read/write operation.
     **************************************************************************/
    public BitPosition getPosition()
    {
        return new BitPosition( position );
    }

    /***************************************************************************
     * Returns the byte index of the current position.
     **************************************************************************/
    public int getByte()
    {
        return position.getByte();
    }

    /***************************************************************************
     * Returns the bit index of the current position.
     **************************************************************************/
    public int getBit()
    {
        return position.getBit();
    }

    /***************************************************************************
     * Reads a single bit from the current position and increments the position
     * by 1 bit.
     * @return the bit read.
     **************************************************************************/
    public boolean readBit()
    {
        byte mask = SET_MASKS[BitPosition.BIT_INDEXES[position.getBit()]];
        boolean bit = ( buffer[position.getByte()] & mask ) == mask;

        position.increment();

        return bit;
    }

    /***************************************************************************
     * Writes the provided bit to the current position and increments the
     * position by 1 bit.
     * @param bit the bit to be written.
     **************************************************************************/
    public void writeBit( boolean bit )
    {
        byte mask;

        if( bit )
        {
            mask = SET_MASKS[BitPosition.BIT_INDEXES[position.getBit()]];
            buffer[position.getByte()] |= mask;
        }
        else
        {
            mask = CLEAR_MASKS[BitPosition.BIT_INDEXES[position.getBit()]];
            buffer[position.getByte()] &= mask;
        }

        position.increment();
    }

    /***************************************************************************
     * Returns the number of bytes remaining.
     * @return
     **************************************************************************/
    public int remainingBits()
    {
        return remainingBytes() * 8 - position.getBit();
    }

    /***************************************************************************
     * Returns the number of bits in this buffer
     **************************************************************************/
    public int bitCount()
    {
        return bits;
    }

    /***************************************************************************
     * returns the number of bytes remaining in this buffer.
     **************************************************************************/
    public int remainingBytes()
    {
        return buffer.length - position.getByte();
    }

    /***************************************************************************
     * Finds the specified bits starting at the provided byte index.
     * @param bits the bits to be found.
     * @param start the beginning byte index to start looking.
     * @return the bit position where the provided bits were found or null if
     * not found.
     **************************************************************************/
    public BitPosition find( BitArray bits, int start )
    {
        BitPosition startPos = new BitPosition( start, 0 );

        return find( bits, startPos );
    }

    /**************************************************************************
     * Finds the specified bits starting at the provided position.
     * @param bits the bits to be found.
     * @param start the beginning position to start looking.
     * @return the bit position where the provided bits were found or null if
     * not found.
     *************************************************************************/
    public BitPosition find( BitArray bits, BitPosition start )
    {
        BitPosition curPos = new BitPosition( position );
        int idx = 0;
        boolean bit;
        BitPosition pos = null;

        position.set( start );

        while( remainingBits() > 0 )
        {
            bit = readBit();

            if( bit == bits.get( idx ) )
            {
                idx++;

                if( idx >= bits.size() )
                {
                    pos = new BitPosition( position );
                    pos.increment( -bits.size() );
                    break;
                }
            }
            else
            {
                position.increment( -idx );
                idx = 0;
            }
        }

        position.set( curPos.getByte(), curPos.getBit() );

        return pos;
    }

    /***************************************************************************
     * Resets the current position to the beginning of the bit buffer.
     **************************************************************************/
    public void reset()
    {
        position.reset();
    }
}
