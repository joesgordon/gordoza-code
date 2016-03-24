package org.jutils.io;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BitReader
{
    /** Masks needed to clear a particular bit. */
    public static final int [] MASKS;

    static
    {
        MASKS = new int[32];

        for( int m = 0; m < MASKS.length; m++ )
        {
            MASKS[m] = 1;
            for( int i = 1; i < m; i++ )
            {
                MASKS[m] <<= 1;
            }
        }
    }

    /**  */
    private final int mask;

    /***************************************************************************
     * @param bit
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
     * @param value
     * @return
     **************************************************************************/
    public boolean read( int value )
    {
        return ( value & mask ) == mask;
    }
}
