package org.jutils.ui.hex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.jutils.NumberParsingUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class HexUtils
{
    /**  */
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

    /***************************************************************************
     * @param b
     * @return
     **************************************************************************/
    public static int toUnsigned( byte b )
    {
        return b & 0x0FF;
    }

    /***************************************************************************
     * @param i
     * @return
     **************************************************************************/
    public static byte toSigned( int i )
    {
        return ( byte )i;
    }

    /***************************************************************************
     * @param i
     * @return
     **************************************************************************/
    public static String toHexString( int i )
    {
        String s = Integer.toHexString( i ).toUpperCase();

        if( i < 0x10 )
        {
            s = "0" + s;
        }

        return s;
    }

    public static char toHex( int nibble )
    {
        switch( nibble )
        {
            case 0:
                return '0';
            case 1:
                return '1';
            case 2:
                return '2';
            case 3:
                return '3';
            case 4:
                return '4';
            case 5:
                return '5';
            case 6:
                return '6';
            case 7:
                return '7';
            case 8:
                return '8';
            case 9:
                return '9';
            case 10:
                return 'A';
            case 11:
                return 'B';
            case 12:
                return 'C';
            case 13:
                return 'D';
            case 14:
                return 'E';
            case 15:
                return 'F';
        }

        throw new IllegalArgumentException( "Outside the range of a nibble: " +
            nibble );
    }

    /***************************************************************************
     * @param bytes
     * @return
     **************************************************************************/
    public static String toHexString( byte[] bytes )
    {
        List<Byte> byteList = new ArrayList<Byte>( bytes.length );

        for( int i = 0; i < bytes.length; i++ )
        {
            byteList.add( bytes[i] );
        }

        return toHexString( byteList );
    }

    /***************************************************************************
     * @param sync
     * @return
     **************************************************************************/
    public static String toHexString( List<Byte> bytes )
    {
        return toHexString( bytes, "" );
    }

    /***************************************************************************
     * @param bytes
     * @param delim
     * @return
     **************************************************************************/
    public static String toHexString( List<Byte> bytes, String delim )
    {
        StringBuilder str = new StringBuilder();

        for( int i = 0; i < bytes.size(); i++ )
        {
            if( i > 0 )
            {
                str.append( delim );
            }

            str.append( toHexString( bytes.get( i ) & 0xFF ) );
        }

        return str.toString();
    }

    /***************************************************************************
     * @param messyString
     * @return
     * @throws PatternSyntaxException
     **************************************************************************/
    public static String trimHexString( String messyString )
        throws PatternSyntaxException
    {
        return messyString.replaceAll( "[^0-9a-fA-F]", "" );
    }

    public static byte[] fromHexStringToArray( String text )
    {
        List<Byte> byteList = fromHexString( text );
        byte[] bytes = new byte[byteList.size()];

        for( int i = 0; i < byteList.size(); i++ )
        {
            bytes[i] = byteList.get( i );
        }

        return bytes;
    }

    /***************************************************************************
     * @param text
     * @return
     * @throws NumberFormatException
     **************************************************************************/
    public static List<Byte> fromHexString( String text )
        throws NumberFormatException
    {
        if( text.length() == 0 )
        {
            throw new NumberFormatException( "The string is empty" );
        }

        List<Byte> bytes = new ArrayList<Byte>( text.length() / 2 );
        int b = 0;
        char c = '-';

        for( int i = 0; i < text.length(); i++ )
        {
            c = text.charAt( i );

            if( i % 2 == 0 )
            {
                b = NumberParsingUtils.digitFromHex( c );
            }
            else
            {
                b = b << 4;
                b |= NumberParsingUtils.digitFromHex( c );
                bytes.add( ( byte )b );
            }
        }

        if( text.length() % 2 != 0 )
        {
            throw new NumberFormatException(
                "The string must be an even number of hexadecimal digits" );
        }

        return bytes;
    }

    public static List<Byte> asList( byte[] array )
    {
        List<Byte> bytes = new ArrayList<Byte>( array.length );

        for( int i = 0; i < array.length; i++ )
        {
            bytes.add( array[i] );
        }

        return bytes;
    }

    public static byte[] asArray( List<Byte> bytes )
    {
        byte[] array = new byte[bytes.size()];

        for( int i = 0; i < bytes.size(); i++ )
        {
            array[i] = bytes.get( i );
        }

        return array;
    }

    public static byte fromBcd( byte b )
    {
        return ( byte )( ( ( ( b >>> 4 ) & 0x0F ) * 10 ) + ( b & 0x0F ) );
    }
}
