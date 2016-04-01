package org.jutils.io;

/*******************************************************************************
 * Creates a reader to return a bit flag from byte, short, int, or long values.
 ******************************************************************************/
public class BitReader
{
    /** Masks needed to clear a particular bit. */
    public static final int [] MASKS;

    static
    {
        MASKS = new int[64];

        for( int m = 0; m < MASKS.length; m++ )
        {
            MASKS[m] = 1;
            for( int i = 1; i < m; i++ )
            {
                MASKS[m] <<= 1;
            }
        }
    }

    /** The mask for the desired bit. */
    private final int mask;

    /***************************************************************************
     * Creates a new reader with the provided bit.
     * @param bit the bit to be read.
     **************************************************************************/
    public BitReader( int bit )
    {
        if( bit >= MASKS.length )
        {
            throw new IllegalArgumentException(
                "BitReader can read up to only " + MASKS.length + "-bits" );
        }

        this.mask = MASKS[bit];
    }

    /***************************************************************************
     * Reads the bit from the provided value.
     * @param value the field containing the bit flag.
     * @return the boolean representation of the bit.
     **************************************************************************/
    public boolean read( byte value )
    {
        return ( value & mask ) == mask;
    }

    /***************************************************************************
     * Reads the bit from the provided value.
     * @param value the field containing the bit flag.
     * @return the boolean representation of the bit.
     **************************************************************************/
    public boolean read( short value )
    {
        return ( value & mask ) == mask;
    }

    /***************************************************************************
     * Reads the bit from the provided value.
     * @param value the field containing the bit flag.
     * @return the boolean representation of the bit.
     **************************************************************************/
    public boolean read( int value )
    {
        return ( value & mask ) == mask;
    }

    /***************************************************************************
     * Reads the bit from the provided value.
     * @param value the field containing the bit flag.
     * @return the boolean representation of the bit.
     **************************************************************************/
    public boolean read( long value )
    {
        return ( value & mask ) == mask;
    }
}
