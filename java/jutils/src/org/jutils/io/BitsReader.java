package org.jutils.io;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BitsReader
{
    /** Masks needed to clear a particular bit. */
    public static final int [] MASKS;

    static
    {
        MASKS = new int[33];

        for( int m = 0; m < MASKS.length; m++ )
        {
            for( int i = 0; i < m; i++ )
            {
                MASKS[m] <<= 1;
                MASKS[m] |= 1;
            }
        }
    }

    /**  */
    private final int mask;
    /**  */
    private final int shift;

    /***************************************************************************
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

        if( end >= Integer.SIZE )
        {
            throw new IllegalArgumentException(
                "End index cannot be greater than " + ( Integer.SIZE - 1 ) );
        }

        int len = end - start + 1;

        int mask = MASKS[len];
        int shift = start;

        this.mask = mask << shift;
        this.shift = shift;

    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public int read( int value )
    {
        return ( value & mask ) >> shift;
    }
}
