package org.jutils.io;

/*******************************************************************************
 * Defines the position of a bit in a byte array.
 ******************************************************************************/
public class BitPosition
{
    /** The zero-relative index of the byte. Must be a positive value. */
    private int byteIndex;
    /** The zero-relative index of the bit. Must be between 0-7 inclusive. */
    private int bitIndex;

    /***************************************************************************
     * Creates a new position that points to the first bit of a byte array.
     **************************************************************************/
    public BitPosition()
    {
        reset();
    }

    /***************************************************************************
     * Creates a new position that points to the same bit as the provided
     * position.
     * @param position the position to be copied.
     **************************************************************************/
    public BitPosition( BitPosition position )
    {
        this.byteIndex = position.byteIndex;
        this.bitIndex = position.bitIndex;
    }

    /***************************************************************************
     * Increments the position to the next bit.
     **************************************************************************/
    public void increment()
    {
        bitIndex += 1;

        if( bitIndex > 7 )
        {
            byteIndex += 1;
            bitIndex = 0;
        }
    }

    /***************************************************************************
     * Increments the position by the specified number of bits.
     * @param bitCount the distance (in bits) to seek.
     **************************************************************************/
    public void increment( int bitCount )
    {
        long bits = byteIndex * 8 + bitIndex + bitCount;

        byteIndex = ( int )( bits / 8 );
        bitIndex = ( int )( bits % 8 );
    }

    /***************************************************************************
     * Returns the byte index of this position.
     **************************************************************************/
    public int getByte()
    {
        return byteIndex;
    }

    /***************************************************************************
     * Returns the bit index of this position.
     **************************************************************************/
    public int getBit()
    {
        return bitIndex;
    }

    /***************************************************************************
     * Sets the position to the provided byte and bit indexes.
     * @param byteIndex the byte index (must be > 0).
     * @param bitIndex the bit index (must be 0 - 7 inclusive).
     * @throws IllegalArgumentException if either index is invalid.
     **************************************************************************/
    public void set( int byteIndex, int bitIndex )
        throws IllegalArgumentException
    {
        if( byteIndex < 0 )
        {
            throw new IllegalArgumentException(
                "The byte index must be > -1: " + byteIndex );
        }

        if( bitIndex < 0 || bitIndex > 7 )
        {
            throw new IllegalArgumentException(
                "The byte index must be > -1 and < 8: " + bitIndex );
        }

        this.byteIndex = byteIndex;
        this.bitIndex = bitIndex;
    }

    /***************************************************************************
     * Sets the position to point to the first bit of a byte array.
     **************************************************************************/
    public void reset()
    {
        set( 0, 0 );
    }
}
