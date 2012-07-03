package org.jutils;

public class NumberParsingUtils
{
    public static int parseHexInteger( String s )
    {
        return ( int )parseHex( s, 8 );
    }

    public static long parseHexLong( String s )
    {
        return parseHex( s, 16 );
    }

    public static long parseHex( String s, int maxLen )
    {
        int len;

        s.trim();
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

    public static int digitFromHex( char c )
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

        throw new NumberFormatException( "'" + c +
            "' is not a hexadecimal digit" );
    }
}
