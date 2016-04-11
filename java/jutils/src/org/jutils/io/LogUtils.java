package org.jutils.io;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jutils.Utils;

/*******************************************************************************
 * Defines a set of standard utility functions to printing messages to the
 * standard output.
 ******************************************************************************/
public class LogUtils
{
    /** A date formatter for displaying debug statements. */
    private static final SimpleDateFormat dateFormatter;

    static
    {
        dateFormatter = new SimpleDateFormat( "HH:mm:ss:SSSS" );
    }

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private LogUtils()
    {
    }

    /***************************************************************************
     * Prints the message to the log with the header "DEBUG: ".
     * @param message the message to be written.
     **************************************************************************/
    public static void printDebug( String message )
    {
        printMessage( "DEBUG", message );
    }

    /***************************************************************************
     * Prints the formatted message to the log with the header "DEBUG: ".
     * @param format
     * @param args
     **************************************************************************/
    public static void printDebug( String format, Object... args )
    {
        printMessage( "DEBUG", format, args );
    }

    /***************************************************************************
     * Prints the message to the log with the header "WARNING: ".
     * @param message the message to be written.
     **************************************************************************/
    public static void printWarning( String message )
    {
        printMessage( "WARNING", message );
    }

    /***************************************************************************
     * @param format
     * @param args
     **************************************************************************/
    public static void printWarning( String format, Object... args )
    {
        printMessage( "WARNING", format, args );
    }

    /***************************************************************************
     * Prints the message to the log with the header "ERROR: ".
     * @param message
     **************************************************************************/
    public static void printError( String message )
    {
        printMessage( "ERROR", message );
    }

    /***************************************************************************
     * @param format
     * @param args
     **************************************************************************/
    public static void printError( String format, Object... args )
    {
        printMessage( "ERROR", format, args );
    }

    /***************************************************************************
     * @param message
     * @param ex
     **************************************************************************/
    public static void printError( String message, Exception ex )
    {
        printMessage( "ERROR", message );
        Utils.printStackTrace( ex );
    }

    /***************************************************************************
     * Prints the message to the log with the header "INFO: ".
     * @param message
     **************************************************************************/
    public static void printInfo( String message )
    {
        printMessage( "INFO", message );
    }

    /***************************************************************************
     * @param format
     * @param args
     **************************************************************************/
    public static void printInfo( String format, Object... args )
    {
        printMessage( "INFO", format, args );
    }

    /***************************************************************************
     * @param msgClass
     * @param message
     **************************************************************************/
    private static void printMessage( String msgClass, String format,
        Object... args )
    {
        printMessage( msgClass, String.format( format, args ) );
    }

    /***************************************************************************
     * @param msgClass
     * @param message
     **************************************************************************/
    private static void printMessage( String msgClass, String message )
    {
        System.out.print( msgClass );
        System.out.print( "[" );
        System.out.print( dateFormatter.format( new Date() ) );
        System.out.print( "]: " );
        System.out.println( message );
    }
}
