package org.jutils.io;

/*******************************************************************************
 * Defines a reader to read a series of bits comprising a sub-field from a byte,
 * short, int, or long.
 ******************************************************************************/
public class BitsReader
{
    /** Masks needed to clear a particular bit. */
    public static final long [] MASKS;

    static
    {
        MASKS = new long[65];

        for( int m = 0; m < MASKS.length; m++ )
        {
            for( int i = 0; i < m; i++ )
            {
                MASKS[m] <<= 1;
                MASKS[m] |= 1;
            }
        }
    }

    /** The mask for the bits of the sub-field. */
    private final long mask;
    /** The number of bytes to shift the sub-field down to bit zero. */
    private final long shift;

    /***************************************************************************
     * Creates a reader that reads the bits out of an integer from the start to
     * the end bits.
     * @param start inclusive start bit.
     * @param end inclusive end bit.
     **************************************************************************/
    public BitsReader( int start, int end )
    {
        if( end <= start )
        {
            throw new IllegalArgumentException(
                "End index must be greater than start index" );
        }

        if( end >= Long.SIZE )
        {
            throw new IllegalArgumentException(
                "End index cannot be greater than " + ( Long.SIZE - 1 ) );
        }

        int len = end - start + 1;

        long mask = MASKS[len];
        int shift = start;

        this.mask = mask << shift;
        this.shift = shift;

    }

    /***************************************************************************
     * Reads the sub-field from the provided value.
     * @param value the field containing the sub-field.
     * @return the sub-field read.
     **************************************************************************/
    public byte read( byte value )
    {
        return ( byte )( ( value & mask ) >>> shift );
    }

    /***************************************************************************
     * Reads the sub-field from the provided value.
     * @param value the field containing the sub-field.
     * @return the sub-field read.
     **************************************************************************/
    public short read( short value )
    {
        return ( short )( ( value & mask ) >>> shift );
    }

    /***************************************************************************
     * Reads the sub-field from the provided value.
     * @param value the field containing the sub-field.
     * @return the sub-field read.
     **************************************************************************/
    public int read( int value )
    {
        return ( int )( ( value & mask ) >>> shift );
    }

    /***************************************************************************
     * Reads the sub-field from the provided value.
     * @param value the field containing the sub-field.
     * @return the sub-field read.
     **************************************************************************/
    public long read( long value )
    {
        return ( value & mask ) >>> shift;
    }
}
