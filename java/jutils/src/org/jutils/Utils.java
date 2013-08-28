package org.jutils;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import org.jutils.ui.hex.HexUtils;

/*******************************************************************************
 *
 ******************************************************************************/
public final class Utils
{
    /**  */
    public static final char[] REGEX_METAC;
    /**  */
    public static final String NEW_LINE;
    /**  */
    public static final String USER_HOME;
    /**  */
    private static final SimpleDateFormat debugDateFormatter;

    static
    {
        REGEX_METAC = new char[] { '\\', '^', '|', '[', ']', '(', ')', '$',
            '.', '+', '*', '?', '{', '}' };
        NEW_LINE = System.getProperty( PropConstants.SYS_LINE_SEP );
        USER_HOME = System.getProperty( PropConstants.SYS_USER_DIR );
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
    public static Dimension setMaxComponentSize( Component... comps )
    {
        Dimension dim = getMaxComponentSize( comps );

        for( Component comp : comps )
        {
            comp.setPreferredSize( dim );
        }

        return dim;
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

    public static <T> ComboBoxModel<T> createModel( T[] options )
    {
        DefaultComboBoxModel<T> model = new DefaultComboBoxModel<T>();

        for( T option : options )
        {
            model.addElement( option );
        }

        return model;
    }
}
