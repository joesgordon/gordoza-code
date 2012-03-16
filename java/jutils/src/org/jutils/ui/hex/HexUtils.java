package org.jutils.ui.hex;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexUtils
{
    public static final String[] BYTE_STRINGS;

    static
    {
        BYTE_STRINGS = new String[256];

        for( int i = 0; i < BYTE_STRINGS.length; i++ )
        {
            BYTE_STRINGS[i] = toHexString( i );
        }
    }

    public static int toUnsigned( byte b )
    {
        return b & 0x0FF;
    }

    public static byte toSigned( int i )
    {
        return ( byte )i;
    }

    public static String toHexString( int i )
    {
        String s = Integer.toHexString( i ).toUpperCase();

        if( i < 0x10 )
        {
            s = "0" + s;
        }

        return s;
    }
}
