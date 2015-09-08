package org.jutils;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * Static helper class for parsing numbers. This class exists because the java
 * standard library is, IMHO, broken. If you have a long (int, short, and byte
 * also exhibit the same behavior) called {@code foo}, you should be able to
 * execute the following:<code> foo = Long.parseString( Long.toHexString( foo ),
 * 16 ) </code> And you can. As long as {@code foo} is not negative. If
 * {@code foo} is negative, the code will throw a NumberFormatException. You
 * don't believe me, do you? Go ahead. Try it. Oh, no. I'll
 * wait...<br>...<br>...<br>...<br>...<br>...<br>...<br>...<br>...<br>...<br>...
 * <br>...<br>...<br>...<br>...<br>...<br><br> See. Broke, didn't it? Well I
 * tested the code below and it appears to be faster than all the
 * INTEGRAL_CLASS.parse anyway.
 ******************************************************************************/
public final class NumberParsingUtils
{
    /***************************************************************************
     * Parses the provided string as a hexadecimal byte of size
     * {@link Integer#SIZE}. If the string contains &quot;0x&quot; at the
     * beginning it will be ignored.
     * @param s the string to be parsed.
     * @return the byte parsed.
     * @throws NumberFormatException any error in the format of the number.
     **************************************************************************/
    public static int parseHexByte( String s ) throws NumberFormatException
    {
        return ( int )parseHex( s, 2 );
    }

    /***************************************************************************
     * Parses the provided string as a hexadecimal short of size
     * {@link Integer#SIZE}. If the string contains &quot;0x&quot; at the
     * beginning it will be ignored.
     * @param s the string to be parsed.
     * @return the short parsed.
     * @throws NumberFormatException any error in the format of the number.
     **************************************************************************/
    public static int parseHexShort( String s ) throws NumberFormatException
    {
        return ( int )parseHex( s, 4 );
    }

    /***************************************************************************
     * Parses the provided string as a hexadecimal integer of size
     * {@link Integer#SIZE}. If the string contains &quot;0x&quot; at the
     * beginning it will be ignored.
     * @param s the string to be parsed.
     * @return the integer parsed.
     * @throws NumberFormatException any error in the format of the number.
     **************************************************************************/
    public static int parseHexInteger( String s ) throws NumberFormatException
    {
        return ( int )parseHex( s, 8 );
    }

    /***************************************************************************
     * Parses the provided string as a hexadecimal integer of size
     * {@link Long#SIZE}. If the string contains &quot;0x&quot; at the beginning
     * it will be ignored.
     * @param s the string to be parsed.
     * @return the long parsed.
     * @throws NumberFormatException any error in the format of the number.
     **************************************************************************/
    public static long parseHexLong( String s ) throws NumberFormatException
    {
        return parseHex( s, 16 );
    }

    /***************************************************************************
     * @param s the string to be parsed. If the string contains &quot;0x&quot;
     * at the beginning it will be ignored.
     * @param maxLen
     * @return the long parsed.
     * @throws NumberFormatException any error in the format of the number.
     **************************************************************************/
    public static long parseHex( String s, int maxLen )
        throws NumberFormatException
    {
        int len;

        if( s.startsWith( "0x" ) )
        {
            s = s.substring( 2 );
        }

        len = s.length();

        if( len > maxLen )
        {
            throw new NumberFormatException(
                "The number may be no longer than " + maxLen +
                    " hexadecimal characters: [" + len + "] '" + s + "'" );
        }
        else if( len == 0 )
        {
            throw new NumberFormatException( "Empty string" );
        }

        long result = 0;
        long digit = 0;
        char c;

        for( int i = 0; i < len; i++ )
        {
            c = s.charAt( i );

            digit = digitFromHex( c );

            result |= ( digit << ( ( len - i - 1 ) * 4 ) );
        }

        return result;
    }

    /***************************************************************************
     * Returns the integer representation of the hexadecimal character.
     * @param c the character to be parsed.
     * @return the integer parsed.
     * @throws NumberFormatException if the character is not [0-9a-fA-F].
     **************************************************************************/
    public static int digitFromHex( char c ) throws NumberFormatException
    {
        switch( c )
        {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'a':
                return 0xA;
            case 'b':
                return 0xB;
            case 'c':
                return 0xC;
            case 'd':
                return 0xD;
            case 'e':
                return 0xE;
            case 'f':
                return 0xF;
            case 'A':
                return 0xA;
            case 'B':
                return 0xB;
            case 'C':
                return 0xC;
            case 'D':
                return 0xD;
            case 'E':
                return 0xE;
            case 'F':
                return 0xF;
        }

        throw new NumberFormatException(
            "'" + c + "' is not a hexadecimal digit" );
    }

    /***************************************************************************
     * @param str
     * @return
     * @throws NumberFormatException
     **************************************************************************/
    public static List<Boolean> fromBinaryString( String str )
        throws NumberFormatException
    {
        List<Boolean> bits = new ArrayList<>( str.length() );

        if( str.isEmpty() )
        {
            throw new NumberFormatException( "The string is empty" );
        }

        for( int i = 0; i < str.length(); i++ )
        {
            switch( str.charAt( i ) )
            {
                case '0':
                    bits.add( false );
                    break;

                case '1':
                    bits.add( true );
                    break;

                default:
                    throw new NumberFormatException(
                        "Non-binary character '" + str.charAt( i ) +
                            "' found at index " + i + " in string " + str );
            }
            ;
        }

        return bits;
    }

    /***************************************************************************
     * @param bits
     * @return
     **************************************************************************/
    public static String toBinaryString( Iterable<Boolean> bits )
    {
        StringBuilder builder = new StringBuilder();

        for( Boolean bit : bits )
        {
            builder.append( bit ? "1" : 0 );
        }

        return builder.toString();
    }
}
