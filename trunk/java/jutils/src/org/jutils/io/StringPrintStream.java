package org.jutils.io;

import org.jutils.Utils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StringPrintStream
{
    /**  */
    private final StringBuilder buffer;

    /***************************************************************************
     * 
     **************************************************************************/
    public StringPrintStream()
    {
        this.buffer = new StringBuilder();
    }

    /***************************************************************************
     * @param str
     **************************************************************************/
    public void print( String str )
    {
        buffer.append( str );
    }

    /***************************************************************************
     * @param str
     **************************************************************************/
    public void println( String str )
    {
        buffer.append( str );
        println();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void println()
    {
        buffer.append( Utils.NEW_LINE );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return buffer.toString();
    }
}
