package org.jutils.io;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jutils.Utils;

/*******************************************************************************
 * 
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
     * @param message
     **************************************************************************/
    public static void printDebug( String message )
    {
        printMessage( "DEBUG", message );
    }

    /***************************************************************************
     * @param message
     **************************************************************************/
    public static void printWarning( String message )
    {
        printMessage( "WARNING", message );
    }

    /***************************************************************************
     * @param message
     **************************************************************************/
    public static void printError( String message )
    {
        printMessage( "ERROR", message );
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
     * @param message
     **************************************************************************/
    public static void printInfo( String message )
    {
        printMessage( "INFO", message );
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
