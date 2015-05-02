package org.jutils.io;

import java.io.*;

import org.jutils.Utils;

public class FastPrintStream implements Closeable
{
    private final BufferedWriter writer;

    public FastPrintStream( File file ) throws IOException
    {
        writer = new BufferedWriter( new FileWriter( file ), 64 * 1024 );
    }

    @Override
    public void close() throws IOException
    {
        writer.close();
    }

    public void println()
    {
        newLine();
    }

    public void println( String line )
    {
        print( line );
        newLine();
    }

    private void print( String str )
    {
        try
        {
            writer.write( str );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
        }
    }

    public void println( String format, Object... args )
    {
        println( String.format( format, args ) );
    }

    private void newLine()
    {
        print( Utils.NEW_LINE );
    }
}
