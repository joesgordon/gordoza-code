package org.jutils;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import javax.swing.JTable;

import org.jutils.io.LogUtils;

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
        REGEX_METAC = new char[] { '\\', '^', '|', '[', ']', '(', ')', '$', '.',
            '+', '*', '?', '{', '}' };
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
     * Provides a method of comparing two doubles within a given precision.
     * @param a double to compare.
     * @param b double to compare.
     * @param epsilon precision of comparison.
     * @return {@code true} if the doubles are equal to within the provided
     * precision; {@code false} otherwise.
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
     * Prints the stack trace from the caller.
     **************************************************************************/
    public static String getStackTrace()
    {
        Throwable tr = new Throwable();
        StackTraceElement [] trace = tr.getStackTrace();
        StackTraceElement [] newTrace = Arrays.copyOfRange( trace, 1,
            trace.length );
        tr.setStackTrace( newTrace );

        return printStackTrace( tr );
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
     * Determines the maximum size from the provided sizes.
     * @param max
     * @param sizes
     **************************************************************************/
    public static void getMaxSize( Dimension max, Dimension... sizes )
    {
        max.width = sizes[0].width;
        max.height = sizes[0].height;

        for( int i = 1; i < sizes.length; i++ )
        {
            max.width = Math.max( max.width, sizes[i].width );
            max.height = Math.max( max.height, sizes[i].height );
        }
    }

    /***************************************************************************
     * Returns the maximum value of the provided list.
     * @param values list of values to find a maximum.
     **************************************************************************/
    public static int max( int... values )
    {
        int max = values[0];

        for( int i = 1; i < values.length; i++ )
        {
            Math.max( max, values[i] );
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
     * Splits a string on either a space or a tab character.
     * @param str
     * @return
     **************************************************************************/
    public static List<String> split( String str )
    {
        return splitSkip( str, false, ' ', '\t' );
    }

    /***************************************************************************
     * @param str
     * @return
     **************************************************************************/
    public static List<String> split( String str, char... delimeters )
    {
        return splitSkip( str, false, delimeters );
    }

    /***************************************************************************
     * @param str
     * @return
     **************************************************************************/
    public static List<String> splitSkip( String str )
    {
        return splitSkip( str, true, ' ', '\t' );
    }

    /***************************************************************************
     * @param str
     * @return
     **************************************************************************/
    public static List<String> splitSkip( String str, char... delimeters )
    {
        return splitSkip( str, true, delimeters );
    }

    /***************************************************************************
     * @param str
     * @param delimeters
     * @return
     **************************************************************************/
    public static List<String> splitSkip( String str, boolean skip,
        char... delimeters )
    {
        List<String> fields = new ArrayList<>();
        int start = -1;
        char c;
        boolean isDelim = false;

        for( int i = 0; i < str.length(); i++ )
        {
            c = str.charAt( i );

            isDelim = false;

            for( char delim : delimeters )
            {
                if( c == delim )
                {
                    isDelim = true;
                    break;
                }
            }

            if( isDelim )
            {
                start++;
                if( !skip || i > start )
                {
                    fields.add( str.substring( start, i ) );
                }

                start = i;
            }
        }

        start++;
        if( !skip || start < str.length() )
        {
            fields.add( str.substring( start ) );
        }

        return fields;
    }

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the array and places a
     * comma and a space between each.
     * @param items Object[] the array to convert to a string.
     * @return the comma separated list of items.
     **************************************************************************/
    public static String arrayToString( Object [] items )
    {
        return arrayToString( items, ", " );
    }

    /***************************************************************************
     * @param items
     * @param delimiter
     * @return
     **************************************************************************/
    public static String arrayToString( Object [] items, String delimiter )
    {
        StringBuffer buf = new StringBuffer();
        for( int i = 0; i < items.length; i++ )
        {
            if( i > 0 )
            {
                buf.append( delimiter );
            }
            buf.append( items[i] );
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
     * @param items
     * @return
     **************************************************************************/
    public static String itemsToString( Object... items )
    {
        return arrayToString( items );
    }

    /***************************************************************************
     * @param delimiter
     * @param items
     * @return
     **************************************************************************/
    public static String itemsToString( String delimiter, Object... items )
    {
        return arrayToString( items, delimiter );
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
     * @param items
     * @param delimiter
     * @return
     **************************************************************************/
    public static String collectionToString( Collection<?> items,
        String delimiter )
    {
        return arrayToString( items.toArray(), delimiter );
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
            throw new ArrayIndexOutOfBoundsException(
                "Unable to copy " + length + " items from array of length " +
                    src.length + " @ " + srcPos + " to array of length " +
                    dest.length + " @ " + destPos );
        }
    }

    /***************************************************************************
     * Wraps {@link System#arraycopy(Object, int, Object, int, int)} to provide
     * better exceptions when arguments are not valid.
     * @param src the buffer from which bytes are copied.
     * @param srcPos the starting position in the source buffer.
     * @param dest the buffer to which bytes are copied.
     * @param destPos the starting position in the destination buffer.
     * @param length the number of bytes to be copied.
     * @throws ArrayIndexOutOfBoundsException if (a) the positions or length is
     * negative, or (b) a position + length is greater than the length of the
     * buffer.
     **************************************************************************/
    public static <T> void byteArrayCopy( byte [] src, int srcPos, byte [] dest,
        int destPos, int length ) throws ArrayIndexOutOfBoundsException
    {
        try
        {
            System.arraycopy( src, srcPos, dest, destPos, length );
        }
        catch( ArrayIndexOutOfBoundsException ex )
        {
            throw new ArrayIndexOutOfBoundsException(
                "Unable to copy " + length + " bytes from array of length " +
                    src.length + " @ " + srcPos + " to array of length " +
                    dest.length + " @ " + destPos );
        }
    }

    /***************************************************************************
     * @param colNames
     * @param string
     * @return
     **************************************************************************/
    public static <T> T [] addFirst( T [] array, T first )
    {
        ArrayList<T> list = new ArrayList<T>( Arrays.asList( array ) );

        list.add( 0, first );

        return list.toArray( array );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static String getClipboardText()
    {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents( null );
        boolean hasTransferableText = contents != null &&
            contents.isDataFlavorSupported( DataFlavor.stringFlavor );

        if( hasTransferableText )
        {
            try
            {
                result = ( String )contents.getTransferData(
                    DataFlavor.stringFlavor );
            }
            catch( UnsupportedFlavorException ex )
            {
            }
            catch( IOException ex )
            {
            }
        }

        return result;
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public static void setClipboardText( String text )
    {
        StringSelection selection = new StringSelection( text );
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        ClipboardOwner co = ( cc, tc ) -> {
        };

        clipboard.setContents( selection, co );

    }

    /***************************************************************************
     * Searches the specified list for the specified object using the binary
     * search algorithm. The list must be sorted into ascending order according
     * to the natural ordering of its elements (as by the sort(List) method)
     * prior to making this call. If it is not sorted, the results are
     * undefined. If the list contains multiple elements equal to the specified
     * object, there is no guarantee which one will be found.</p>
     * @param items
     * @param key
     * @param c
     * @return the index of the search key, if it is contained in the list;
     * otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
     * point</i> is defined as the point at which the key would be inserted into
     * the list: the index of the first element greater than the key, or
     * <tt>list.size()</tt> if all elements in the list are less than the
     * specified key. Note that this guarantees that the return value will be
     * &gt;= 0 if and only if the key is found.
     **************************************************************************/
    public static <T, K> int binarySearch( List<T> items, K key,
        IComparator<T, K> c )
    {
        int low = 0;
        int high = items.size() - 1;

        while( low <= high )
        {
            int mid = ( low + high ) >>> 1;
            T midVal = items.get( mid );
            int cmp = c.compare( key, midVal );

            if( cmp > 0 )
            {
                low = mid + 1;
            }
            else if( cmp < 0 )
            {
                high = mid - 1;
            }
            else
            {
                return mid; // key found
            }
        }
        return -( low + 1 ); // key not found
    }

    /***************************************************************************
     * Returns the mask needed to remove the upper bits from the provided value.
     * @param v the value for which the mask will be generated.
     **************************************************************************/
    public static int getMaskForValue( int v )
    {
        v--;
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        v |= v >> 16;
        // v++;

        return v;
    }

    /***************************************************************************
     * Returns a list containing the font family names of all available
     * monospace fonts that can display the ASCII numbers, lower case, and upper
     * case values.
     **************************************************************************/
    public static List<String> getMonospacedFonts()
    {
        List<String> fonts = new ArrayList<>();

        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String [] fontFamilyNames = graphicsEnvironment.getAvailableFontFamilyNames();

        BufferedImage bufferedImage = new BufferedImage( 1, 1,
            BufferedImage.TYPE_INT_ARGB );
        Graphics graphics = bufferedImage.createGraphics();

        for( String fontFamilyName : fontFamilyNames )
        {
            boolean isMonospaced = true;

            int fontStyle = Font.PLAIN;
            int fontSize = 12;
            Font font = new Font( fontFamilyName, fontStyle, fontSize );
            FontMetrics fontMetrics = graphics.getFontMetrics( font );

            int firstCharacterWidth = 0;
            boolean hasFirstCharacterWidth = false;
            int [] chars = new int[62];

            for( int i = 0; i < 10; i++ )
            {
                chars[i] = i + 0x30;
            }
            for( int i = 0; i < 26; i++ )
            {
                chars[i + 10] = i + 0x41;
            }
            for( int i = 0; i < 26; i++ )
            {
                chars[i + 36] = i + 0x61;
            }

            for( int i = 0; i < chars.length; i++ )
            {
                int codePoint = chars[i];

                if( !font.canDisplay( codePoint ) )
                {
                    isMonospaced = false;
                    LogUtils.printDebug( "%s cannot display 0x%02X '%c' @ %d",
                        fontFamilyName, codePoint, ( char )codePoint, i );
                    break;
                }

                if( Character.isValidCodePoint( codePoint ) &&
                    ( Character.isLetter( codePoint ) ||
                        Character.isDigit( codePoint ) ) )
                {
                    char character = ( char )codePoint;
                    int characterWidth = fontMetrics.charWidth( character );
                    if( hasFirstCharacterWidth )
                    {
                        if( characterWidth != firstCharacterWidth )
                        {
                            isMonospaced = false;
                            break;
                        }
                    }
                    else
                    {
                        firstCharacterWidth = characterWidth;
                        hasFirstCharacterWidth = true;
                    }
                }
            }

            if( isMonospaced )
            {
                fonts.add( fontFamilyName );
            }
        }

        graphics.dispose();

        return fonts;
    }

    /***************************************************************************
     * Object that can compare an item object to a key object. Used for sorting
     * or searching a list of the items type by a field of the item.
     * @see Comparator#compare(Object, Object)
     **************************************************************************/
    public static interface IComparator<T, K>
    {
        /***********************************************************************
         * Compares its two arguments for order. Returns <ul> <li>a negative
         * integer if {@code thisKey < thatItem}</li> <li>zero if
         * {@code thisKey == thatItem}, or </li> <li>a positive integer if
         * {@code thisKey > thatItem},</li> </ul>
         * @param item
         * @param key
         * @return
         **********************************************************************/
        public int compare( K thisKey, T thatItem );
    }
}
