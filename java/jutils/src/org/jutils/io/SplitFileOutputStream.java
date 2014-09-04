package org.jutils.io;

import java.io.*;

/*******************************************************************************
 *
 ******************************************************************************/
public class SplitFileOutputStream extends OutputStream
{
    private FileOutputStream currentStream = null;
    private FileNameAdvancer advancer = null;

    private boolean append = false;
    private long fileSize = 0;
    private long offset = 0;

    /***************************************************************************
     * @param file File
     * @param splitSize int
     * @throws FileNotFoundException
     **************************************************************************/
    public SplitFileOutputStream( File file, long splitSize )
        throws FileNotFoundException
    {
        this( file, false, splitSize );
    }

    /***************************************************************************
     * @param file File
     * @param append boolean
     * @param splitSize int
     * @throws FileNotFoundException
     **************************************************************************/
    public SplitFileOutputStream( File file, boolean append, long splitSize )
        throws FileNotFoundException
    {
        this.fileSize = splitSize;
        this.append = append;

        advancer = new FileNameAdvancer( file );

        try
        {
            setupNextFile();
        }
        catch( IOException ex )
        {
            System.err.println( "Code is broken" );
            ex.printStackTrace();
            // assert( false );
        }
    }

    /***************************************************************************
     * @throws FileNotFoundException
     **************************************************************************/
    private void setupNextFile() throws IOException
    {
        File nextFile = advancer.getNext();
        if( currentStream != null )
        {
            currentStream.close();
        }
        currentStream = new FileOutputStream( nextFile, append );
        offset = 0;
    }

    /***************************************************************************
     * @return long
     **************************************************************************/
    private long getBytesLeft()
    {
        return fileSize - offset;
    }

    /***************************************************************************
     * @param b int
     * @throws IOException
     **************************************************************************/
    public void write( int b ) throws IOException
    {
        if( getBytesLeft() < 1 )
        {
            setupNextFile();
        }
        currentStream.write( b );
        offset += 1;
    }

    /***************************************************************************
     * @param b byte[]
     * @throws IOException
     **************************************************************************/
    public void write( byte b [] ) throws IOException
    {
        this.write( b, 0, b.length );
    }

    /***************************************************************************
     * @param b byte[]
     * @param off int
     * @param len int
     * @throws IOException
     **************************************************************************/
    public void write( byte b [], int off, int len ) throws IOException
    {
        int thisLen = 0;
        while( len > 0 )
        {
            if( getBytesLeft() < 1 )
            {
                setupNextFile();
            }
            thisLen = Math.min( ( int )getBytesLeft(), len );
            currentStream.write( b, off, thisLen );
            offset += len;
            off += thisLen;
            len -= thisLen;
        }

    }
}
