package org.jutils.io;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtils
{

    /** A date formatter for displaying debug statements. */
    private static final SimpleDateFormat dateFormatter;

    static
    {
        dateFormatter = new SimpleDateFormat( "HH:mm:ss:SSSS" );
    }

    public static void printDebug( String message )
    {
        printMessage( "DEBUG", message );
    }

    private static void printMessage( String msgClass, String message )
    {
        System.out.print( msgClass );
        System.out.print( "[" );
        System.out.print( dateFormatter.format( new Date() ) );
        System.out.print( "]: " );
        System.out.println( message );
    }
}
