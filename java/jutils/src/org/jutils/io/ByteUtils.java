package org.jutils.io;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class ByteUtils
{
    /***************************************************************************
     * 
     **************************************************************************/
    private ByteUtils()
    {
    }

    /***************************************************************************
     * @param data
     * @return
     **************************************************************************/
    public static int getInteger( byte [] data )
    {
        return getInteger( data, 0 );
    }

    /***************************************************************************
     * @param data
     * @param index
     * @return
     **************************************************************************/
    public static int getInteger( byte [] data, int index )
    {
        int i = 0;

        i |= Byte.toUnsignedInt( data[index + 0] );

        i <<= 8;
        i |= Byte.toUnsignedInt( data[index + 1] );

        i <<= 8;
        i |= Byte.toUnsignedInt( data[index + 2] );

        i <<= 8;
        i |= Byte.toUnsignedInt( data[index + 3] );

        return i;
    }

    /***************************************************************************
     * @param data
     * @return
     **************************************************************************/
    public static short getShort( byte [] data )
    {
        return getShort( data, 0 );
    }

    /***************************************************************************
     * @param data
     * @param index
     * @return
     **************************************************************************/
    public static short getShort( byte [] data, int index )
    {
        short s = 0;

        s |= Byte.toUnsignedInt( data[index + 0] );

        s <<= 8;
        s |= Byte.toUnsignedInt( data[index + 1] );

        return s;
    }
}
