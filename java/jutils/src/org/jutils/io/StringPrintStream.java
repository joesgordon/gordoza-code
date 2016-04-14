package org.jutils.io;

import org.jutils.Utils;

/*******************************************************************************
 * Defines a print stream that writes to a string.
 ******************************************************************************/
public class StringPrintStream implements IPrintStream
{
    /** The buffer to hold the strings written. */
    private final StringBuilder buffer;

    /***************************************************************************
     * Creates a new print stream with an empty buffer.
     **************************************************************************/
    public StringPrintStream()
    {
        this.buffer = new StringBuilder();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void print( String str )
    {
        buffer.append( str );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void println( String str )
    {
        buffer.append( str );
        println();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void println()
    {
        buffer.append( Utils.NEW_LINE );
    }

    /***************************************************************************
     * Returns {@code true} if the stream has never been written to.
     **************************************************************************/
    public boolean isEmpty()
    {
        return buffer.length() > 0;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return buffer.toString();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void println( String format, Object... args )
    {
        println( String.format( format, args ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void print( String format, Object... args )
    {
        print( String.format( format, args ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void println( char [] chars )
    {
        buffer.append( chars );
    }

    @Override
    public void close()
    {
        buffer.setLength( 0 );
    }
}
