package org.jutils;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;

/*******************************************************************************
 * Utility class for general static functions.
 ******************************************************************************/
public final class Utils
{
    /** A list of all regular expression meta characters. */
    public static final char [] REGEX_METAC;
    /** The new line string for the current system. */
    public static final String NEW_LINE;
    /** The user's home directory. */
    public static final String USER_HOME;

    static
    {
        REGEX_METAC = new char[] { '\\', '^', '|', '[', ']', '(', ')', '$',
            '.', '+', '*', '?', '{', '}' };
        NEW_LINE = System.getProperty( PropConstants.SYS_LINE_SEP );
        USER_HOME = System.getProperty( PropConstants.SYS_USER_DIR );
    }

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private Utils()
    {
    }

    /***************************************************************************
     * @param a
     * @param b
     * @param epsilon
     * @return
     **************************************************************************/
    public static boolean doubleEquals( double a, double b, double epsilon )
    {
        return a == b ? true : Math.abs( a - b ) < epsilon;
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

    /***************************************************************************
     * From SO http://stackoverflow.com/questions/1410168
     * @param s
     * @return
     **************************************************************************/
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
     * Prints the stack trace from the caller.
     **************************************************************************/
    public static void printStackTrace()
    {
        Throwable tr = new Throwable();
        StackTraceElement [] trace = tr.getStackTrace();
        StackTraceElement [] newTrace = Arrays.copyOfRange( trace, 1,
            trace.length );
        tr.setStackTrace( newTrace );
        System.out.println( printStackTrace( tr ) );
    }

    /***************************************************************************
     * Prints the stack trace of the provided throwable to a string.
     * @param tr the throwable containing the stack trace to be printed.
     * @return String the string representation of the stack trace.
     **************************************************************************/
    public static String printStackTrace( Throwable tr )
    {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter( result );
        tr.printStackTrace( printWriter );
        return result.toString();
    }

    /***************************************************************************
     * Search the component's parent tree looking for an object of the provided
     * type.
     * @param comp the child component.
     * @param type the type of parent to be found.
     * @return the component of the type provided or {@code null} if not found.
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
     * Returns the {@link Window} containing the provided component.
     * @param comp the child component.
     * @return the window owning the provided component or {@code null} if the
     * component is orphaned.
     **************************************************************************/
    public static Window getComponentsWindow( Component comp )
    {
        Object win = getParentOfType( comp, Window.class );
        return win != null ? ( Window )win : null;
    }

    /***************************************************************************
     * Returns the {@link Frame} containing the provided component.
     * @param comp the child component.
     * @return the frame owning the provided component or {@code null} if the
     * component is orphaned or the owning window is not a frame.
     **************************************************************************/
    public static Frame getComponentsFrame( Component comp )
    {
        Object win = getParentOfType( comp, Frame.class );
        return win != null ? ( Frame )win : null;
    }

    /***************************************************************************
     * @param comp
     * @return
     **************************************************************************/
    public static JFrame getComponentsJFrame( Component comp )
    {
        Object win = getParentOfType( comp, JFrame.class );
        return win != null ? ( JFrame )win : null;
    }

    /***************************************************************************
     * Finds the maximum width and length of the provided components and sets
     * the preferred size of each to the maximum.
     * @param comps the components to be evaluated.
     * @return the maximum size.
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
     * Finds the maximum width and length of the provided components.
     * @param comps the components to be evaluated.
     * @return the maximum size.
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
     * Builds a URL for a resource relative to the provided class by the
     * provided string.
     * @param c the class from which the relative path of the resource is known.
     * @param resource the path to the resource relative to the class.
     * @return the URL path of the resource.
     **************************************************************************/
    public static URL loadResourceURL( Class<?> c, String resource )
    {
        URL url = c.getResource( resource );
        return url;
    }

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the array and places a
     * comma and a space between each.
     * @param items Object[] the array to convert to a string.
     * @return the comma separated list of items.
     **************************************************************************/
    public static String arrayToString( Object [] items )
    {
        StringBuffer buf = new StringBuffer();
        for( int i = 0; i < items.length; i++ )
        {
            if( i > 0 )
            {
                buf.append( ", " );
            }
            buf.append( items[i].toString() );
        }

        return buf.toString();
    }

    /***************************************************************************
     * Performs the same operations as {@link #arrayToString(Object[])} but with
     * variable argument syntax.
     * @param items the objects to convert to a string.
     * @return the comma separated string of items.
     **************************************************************************/
    public static String argsToString( Object... items )
    {
        return arrayToString( items );
    }

    /***************************************************************************
     * Performs the same operations as {@link #arrayToString(Object[])} but with
     * a list rather than an array.
     * @param items the list to convert to a string.
     * @return the comma separated list of items.
     **************************************************************************/
    public static String listToString( java.util.List<? extends Object> items )
    {
        return arrayToString( items.toArray() );
    }

    /***************************************************************************
     * Performs the same operations as {@link #arrayToString(Object[])} but with
     * a list rather than an array.
     * @param items the collection to convert to a string.
     * @return the comma separated list of items.
     **************************************************************************/
    public static String collectionToString( Collection<?> items )
    {
        return arrayToString( items.toArray() );
    }

    /***************************************************************************
     * Escapes all meta-characters in the regular expression string provided.
     * @param str the regular expression to be cleansed.
     * @return the cleansed string.
     **************************************************************************/
    public static String escapeRegexMetaChar( String str )
    {
        return escapeAllChars( str, REGEX_METAC );
    }

    /***************************************************************************
     * Escapes each character in the provided array found in the provided string
     * with a backslash '\'.
     * @param str the string to be cleansed.
     * @param chars char[] the characters to be escaped.
     * @return the cleansed string.
     **************************************************************************/
    public static String escapeAllChars( String str, char [] chars )
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

    /***************************************************************************
     * Creates a {@link ComboBoxModel} with the provided array of items.
     * @param items the items to be contained within the model.
     * @return the model containing the items.
     **************************************************************************/
    public static <T> ComboBoxModel<T> createModel( T [] items )
    {
        DefaultComboBoxModel<T> model = new DefaultComboBoxModel<T>();

        for( T option : items )
        {
            model.addElement( option );
        }

        return model;
    }

    /***************************************************************************
     * Programmatically closes the provided window. See the StackOverflow
     * question <a href="http://stackoverflow.com/questions/1234912">How to
     * programmatically close a JFrame</a> for more information.
     * @param win the window to be closed.
     **************************************************************************/
    public static void closeWindow( Window win )
    {
        win.dispatchEvent( new WindowEvent( win, WindowEvent.WINDOW_CLOSING ) );
    }

    /***************************************************************************
     * Scrolls to the row and column provided.
     * @param table the table to be scrolled.
     * @param row the row to be scrolled to.
     * @param col the column to be scrolled to.
     **************************************************************************/
    public static void scrollToVisible( JTable table, int row, int col )
    {
        Rectangle rect = table.getCellRect( row, col, true );

        rect = new Rectangle( rect );

        // LogUtils.printDebug( "Scrolling to: " + row + ", " + col + ":" +
        // rect.toString() );

        table.scrollRectToVisible( rect );
    }

    /***************************************************************************
     * Creates a transparent buffered image for the current device.
     * @param width the width of the image to be created.
     * @param height the height of the image to be created.
     **************************************************************************/
    public static BufferedImage createTransparentImage( int width, int height )
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        return gc.createCompatibleImage( width, height,
            Transparency.TRANSLUCENT );
    }

    /***************************************************************************
     * @param src
     * @param srcPos
     * @param dest
     * @param destPos
     * @param length
     **************************************************************************/
    public static <T> void arrayCopy( T [] src, int srcPos, T [] dest,
        int destPos, int length )
    {
        try
        {
            System.arraycopy( src, srcPos, dest, destPos, length );
        }
        catch( ArrayIndexOutOfBoundsException ex )
        {
            throw new ArrayIndexOutOfBoundsException( "Unable to copy " +
                length + " items from array of length " + src.length + " @ " +
                srcPos + " to array of length " + dest.length + " @ " + destPos );
        }
    }

    /***************************************************************************
     * @param src
     * @param srcPos
     * @param dest
     * @param destPos
     * @param length
     **************************************************************************/
    public static <T> void byteArrayCopy( byte [] src, int srcPos,
        byte [] dest, int destPos, int length )
    {
        try
        {
            System.arraycopy( src, srcPos, dest, destPos, length );
        }
        catch( ArrayIndexOutOfBoundsException ex )
        {
            throw new ArrayIndexOutOfBoundsException( "Unable to copy " +
                length + " bytes from array of length " + src.length + " @ " +
                srcPos + " to array of length " + dest.length + " @ " + destPos );
        }
    }
}
