package org.jutils.io;

/*******************************************************************************
 * Defines a print stream that prints to no resource. Used to prevent if checks
 * in logging code.
 ******************************************************************************/
public class NullPrintStream implements IPrintStream
{
    @Override
    public void close()
    {
    }

    @Override
    public void print( String str )
    {
    }

    @Override
    public void println( String str )
    {
    }

    @Override
    public void println()
    {
    }

    @Override
    public void println( String format, Object... args )
    {
    }

    @Override
    public void print( String format, Object... args )
    {
    }

    @Override
    public void println( char [] chars )
    {
    }
}
