package testbed;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class LongParse
{

    public static void main( String[] args )
    {
        List<IHexLongParser> parsers = new ArrayList<LongParse.IHexLongParser>();

        parsers.add( new BigIntegerConstructor() );
        parsers.add( new LongDotParseLong() );
        parsers.add( new SplitMethod() );
        parsers.add( new StraightforwardParse() );

        for( IHexLongParser parser : parsers )
        {
            testParse( parser );
        }
    }

    private static void testParse( IHexLongParser parser )
    {
        int max = 10000000;
        long start = System.nanoTime();
        String str;
        long num;

        for( int i = -max; i < max; i++ )
        {
            str = Long.toHexString( i );
            // num = Long.parseLong( str, 16 );
            // num = parseHexLong( str );
            // num = new BigInteger( str, 16 ).longValue();
            // num = parseHexLong2( str );
            try
            {
                num = parser.parse( str );

                if( num != i )
                {
                    System.err.println( num + " != " + i );
                }
            }
            catch( NumberFormatException ex )
            {
                System.out.println( parser.getName() + " : BROKEN because \"" +
                    ex.getMessage() + "\"" );
                return;
            }
        }
        long estTime = System.nanoTime() - start;
        double sec = estTime / 1000000000.0;

        System.out.println( parser.getName() +
            String.format( " took: %3f", sec ) );
    }

    /**
     * From SO http://stackoverflow.com/questions/1410168
     * @param s
     * @return
     */
    public static long parseHexLong2( String s )
    {
        int len = s.length();

        if( len > 16 )
        {
            throw new NumberFormatException(
                "A long may be no longer than 16 hexadecimal characters: [" +
                    len + "] '" + s + "'" );
        }
        else if( len == 0 )
        {
            throw new NumberFormatException( "Empty string" );
        }

        long msb = 0;
        long lsb = 0;

        if( len > 8 )
        {
            msb = Long.parseLong( s.substring( 0, len - 8 ), 16 );
            lsb = Long.parseLong( s.substring( len - 8 ), 16 );
        }
        else
        {
            lsb = Long.parseLong( s, 16 );
        }

        return msb << 32 | lsb;
    }

    public static long parseHexLong( String s )
    {
        int len = s.length();

        if( len > 16 )
        {
            throw new NumberFormatException(
                "A long may be no longer than 16 hexadecimal characters: [" +
                    len + "] '" + s + "'" );
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

    private static interface IHexLongParser
    {
        public long parse( String s );

        public String getName();
    }

    private static class LongDotParseLong implements IHexLongParser
    {
        @Override
        public long parse( String s )
        {
            return Long.parseLong( s, 16 );
        }

        @Override
        public String getName()
        {
            return "Long.parseLong(String, 16)";
        }
    }

    private static class BigIntegerConstructor implements IHexLongParser
    {
        @Override
        public long parse( String s )
        {
            return new BigInteger( s, 16 ).longValue();
        }

        @Override
        public String getName()
        {
            return "new BigInteger(String).longValue()";
        }
    }

    private static class SplitMethod implements IHexLongParser
    {
        @Override
        public long parse( String s )
        {
            return parseHexLong2( s );
        }

        @Override
        public String getName()
        {
            return "Split Method";
        }
    }

    private static class StraightforwardParse implements IHexLongParser
    {
        @Override
        public long parse( String s )
        {
            return parseHexLong( s );
        }

        @Override
        public String getName()
        {
            return "Straightforward Method";
        }
    }
}
