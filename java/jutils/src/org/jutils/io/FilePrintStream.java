package org.jutils.io;

import java.io.*;

import org.jutils.Utils;

/*******************************************************************************
 * Defines a print stream that prints to a file in a buffered fashion.
 ******************************************************************************/
public class FilePrintStream implements IPrintStream
{
    /** The buffer size used to create the underlying writer. */
    private static final int BUFFER_SIZE = 64 * 1024;

    /** The buffer to use for printing. */
    private final BufferedWriter writer;

    /***************************************************************************
     * Creates a new print string that writes to the provided file.
     * @param file the file to be written to.
     * @throws IOException if the file exists but is a directory rather than a
     * regular file, does not exist but cannot be created, or cannot be opened
     * for any other reason.
     **************************************************************************/
    public FilePrintStream( File file ) throws IOException
    {
        FileOutputStream fos = new FileOutputStream( file );
        Writer w = new OutputStreamWriter( fos, IOUtils.US_ASCII );

        writer = new BufferedWriter( w, BUFFER_SIZE );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        writer.close();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void print( String str )
    {
        write( str );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void println()
    {
        writeNewLine();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void println( String line )
    {
        write( line );
        writeNewLine();
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
        write( chars );
        writeNewLine();
    }

    /***************************************************************************
     * Writes the system line separator to the underlying stream.
     **************************************************************************/
    private void writeNewLine()
    {
        write( Utils.NEW_LINE );
    }

    /***************************************************************************
     * Writes the provided string to the underlying stream, suppressing any
     * exception that occurs.
     * @param str the string to be written.
     **************************************************************************/
    private void write( String str )
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

    /***************************************************************************
     * Writes the provided characters to the underlying stream, suppressing any
     * exception that occurs.
     * @param chars the characters to be written.
     **************************************************************************/
    private void write( char [] chars )
    {
        try
        {
            writer.write( chars );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
        }
    }
}
