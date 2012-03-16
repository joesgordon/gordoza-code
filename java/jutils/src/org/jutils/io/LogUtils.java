package org.jutils.io;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtils
{
    private static SimpleDateFormat dateFormatter;

    static
    {
        dateFormatter = new SimpleDateFormat( "HH:mm:ss:SS" );
    }

    public static void printDebug( String str )
    {
        printDate();
        System.out.print( ' ' );
        System.out.println( str );
    }

    private static void printDate()
    {
        System.out.print( dateFormatter.format( new Date() ) );
    }
}
