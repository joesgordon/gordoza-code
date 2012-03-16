package org.jutils;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import org.jutils.ui.hex.HexUtils;

/*******************************************************************************
 *
 ******************************************************************************/
public final class Utils
{
    /**  */
    public static final char[] REGEX_METAC = { '\\', '^', '|', '[', ']', '(',
        ')', '$', '.', '+', '*', '?', '{', '}' };

    /**  */
    public static final String NEW_LINE = System.getProperty( PropConstants.SYS_LINE_SEP );

    /**  */
    public static final String USER_HOME = System.getProperty( PropConstants.SYS_USER_DIR );

    private static final SimpleDateFormat debugDateFormatter;

    static
    {
        debugDateFormatter = new SimpleDateFormat( "HH:mm:ss:SSSS" );
    }

    /***************************************************************************
     *
     **************************************************************************/
    private Utils()
    {
    }

    /***************************************************************************
     * Returns the elapsed time in the default formatted string.
     * @return the elapsed time in the default formatted string.
     **************************************************************************/
    public static String getElapsedString( Date d )
    {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat( "HH:mm:ss:SSS" );
        DATE_FORMAT.setTimeZone( TimeZone.getTimeZone( "GMT" ) );
        return DATE_FORMAT.format( d );
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

    /***************************************************************************
     * @param message
     **************************************************************************/
    public static void printDebug( String message )
    {
        System.out.print( "DEBUG[" );
        System.out.print( debugDateFormatter.format( new Date() ) );
        System.out.print( "]: " );
        System.out.println( message );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static void printStackTrace()
    {
        Throwable tr = new Throwable();
        StackTraceElement[] trace = tr.getStackTrace();
        StackTraceElement[] newTrace = Arrays.copyOfRange( trace, 1,
            trace.length );
        tr.setStackTrace( newTrace );
        System.out.println( printStackTrace( tr ) );
    }

    /***************************************************************************
     * @param tr Throwable
     * @return String
     **************************************************************************/
    public static String printStackTrace( Throwable tr )
    {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter( result );
        tr.printStackTrace( printWriter );
        return result.toString();
    }

    /***************************************************************************
     * @param comp Component
     * @param type Class
     * @return Component
     **************************************************************************/
    public static Component getParentOfType( Component comp, Class<?> type )
    {
        Component parent = null;
        Component parentComp = comp;

        while( parentComp != null )
        {
            if( type.isAssignableFrom( parentComp.getClass() ) )
            {
                parent = parentComp;
                break;
            }
            parentComp = parentComp.getParent();
        }

        return parent;
    }

    /***************************************************************************
     * @param comp Component
     * @return Window
     **************************************************************************/
    public static Window getComponentsWindow( Component comp )
    {
        Object win = getParentOfType( comp, Window.class );
        return win != null ? ( Window )win : null;
    }

    /***************************************************************************
     * @param comp Component
     * @return Frame
     **************************************************************************/
    public static Frame getComponentsFrame( Component comp )
    {
        Object win = getParentOfType( comp, Frame.class );
        return win != null ? ( Frame )win : null;
    }

    /***************************************************************************
     * @param comps
     * @return
     **************************************************************************/
    public static Dimension getMaxComponentSize( Component... comps )
    {
        Dimension max = new Dimension( 0, 0 );
        Dimension dim;

        for( Component comp : comps )
        {
            dim = comp.getPreferredSize();
            max.width = Math.max( max.width, dim.width );
            max.height = Math.max( max.height, dim.height );
        }

        return max;
    }

    /***************************************************************************
     * @param resource String
     * @return URL
     **************************************************************************/
    public static URL loadResourceURL( String resource )
    {
        return loadResourceURL( Utils.class, resource );
    }

    /***************************************************************************
     * @param c Class
     * @param resource String
     * @return URL
     **************************************************************************/
    public static URL loadResourceURL( Class<?> c, String resource )
    {
        URL url = c.getResource( resource );
        return url;
    }

    /***************************************************************************
     * @param array Object[]
     * @return String
     **************************************************************************/
    public static String arrayToString( Object[] array )
    {
        StringBuffer buf = new StringBuffer();
        for( int i = 0; i < array.length; i++ )
        {
            if( i > 0 )
            {
                buf.append( ", " );
            }
            buf.append( array[i].toString() );
        }

        return buf.toString();
    }

    /***************************************************************************
     * @param array Object[]
     * @return String
     **************************************************************************/
    public static String arrayToString( byte[] array )
    {
        StringBuffer buf = new StringBuffer();
        for( int i = 0; i < array.length; i++ )
        {
            if( i > 0 )
            {
                buf.append( ", " );
            }
            buf.append( HexUtils.BYTE_STRINGS[array[i] & 0x0FF] );
        }

        return buf.toString();
    }

    /***************************************************************************
     * @param args
     * @return
     **************************************************************************/
    public static String argsToString( Object... args )
    {
        return arrayToString( args );
    }

    /***************************************************************************
     * @param vec Vector
     * @return String
     **************************************************************************/
    public static String listToString( java.util.List<?> vec )
    {
        return arrayToString( vec.toArray() );
    }

    /***************************************************************************
     * @param c Collection
     * @return String
     **************************************************************************/
    public static String collectionToString( Collection<?> c )
    {
        return arrayToString( c.toArray() );
    }

    /***************************************************************************
     * @param str String
     * @return String
     **************************************************************************/
    public static String escapeRegexMetaChar( String str )
    {
        return escapeAllChars( str, REGEX_METAC );
    }

    /***************************************************************************
     * @param str String
     * @param chars char[]
     * @return String
     **************************************************************************/
    public static String escapeAllChars( String str, char[] chars )
    {
        StringBuffer buffer = new StringBuffer();
        int offset = str.length();

        for( int idx = str.length() - 1; idx > -1; idx-- )
        {
            for( int mIdx = 0; mIdx < chars.length; mIdx++ )
            {
                if( str.charAt( idx ) == chars[mIdx] )
                {
                    if( idx > 0 )
                    {
                        if( str.charAt( idx - 1 ) == '\\' )
                        {
                            // Properly escaped, skip.
                            idx--;
                        }
                        else
                        {
                            // Not properly escaped, escape and add to buffer.
                            buffer.insert( 0,
                                "\\" + str.substring( idx, offset ) );
                            offset = idx;
                        }
                    }
                    else
                    {
                        buffer.insert( 0, "\\" + str.substring( idx, offset ) );
                        offset = idx;
                    }
                    break;
                }
            }
        }

        if( offset > 0 )
        {
            buffer.insert( 0, str.substring( 0, offset ) );
        }

        return buffer.toString();

    }
}
