package org.jutils.ui.hex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.jutils.NumberParsingUtils;

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

    /***************************************************************************
     * 
     **************************************************************************/
    private HexUtils()
    {
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

    public static String toHexString( List<Byte> sync )
    {
        return toHexString( sync, "" );
    }

    public static String toHexString( List<Byte> sync, String delim )
    {
        StringBuilder str = new StringBuilder();

        for( int i = 0; i < sync.size(); i++ )
        {
            if( i > 0 )
            {
                str.append( delim );
            }

            str.append( toHexString( sync.get( i ) & 0xFF ) );
        }

        return str.toString();
    }

    public static String trimHexString( String messyString )
        throws PatternSyntaxException
    {
        return messyString.replaceAll( "[^0-9a-fA-F]", "" );
    }

    public static List<Byte> fromHexString( String text )
        throws NumberFormatException
    {
        if( text.length() % 2 != 0 )
        {
            throw new NumberFormatException(
                "The string must be an even number of hexadecimal digits." );
        }

        int len = text.length() / 2;
        List<Byte> bytes = new ArrayList<Byte>( len );
        String byteStr;

        for( int i = 0; i < len; i++ )
        {
            byteStr = text.substring( i * 2, i * 2 + 2 );
            bytes.add( ( byte )NumberParsingUtils.parseHexByte( byteStr ) );
        }

        return bytes;
    }
}
