package org.jutils.io;

import java.io.*;

/*******************************************************************************
 *
 ******************************************************************************/
public class SplitFileInputStream extends InputStream
{
    private FileInputStream currentStream = null;

    private FileNameAdvancer advancer = null;

    private long fileSize = 0;

    private long offset = 0;

    /***************************************************************************
     * @param file File
     * @throws FileNotFoundException
     **************************************************************************/
    public SplitFileInputStream( File file ) throws FileNotFoundException
    {
        advancer = new FileNameAdvancer( file );

        try
        {
            if( !setupNextFile() )
            {
                throw new FileNotFoundException( "File cannot be found: " +
                    file.getAbsolutePath() );
            }
        }
        catch( IOException ex )
        {
            System.err.println( "Code is broken" );
            ex.printStackTrace();
            // assert( false );
        }
    }

    /***************************************************************************
     * @return boolean
     * @throws IOException
     **************************************************************************/
    private boolean setupNextFile() throws IOException
    {
        File nextFile = advancer.getNext();

        fileSize = nextFile.length();
        try
        {
            if( currentStream != null )
            {
                currentStream.close();
            }
            currentStream = new FileInputStream( nextFile );
            offset = 0;
        }
        catch( FileNotFoundException ex )
        {
            return false;
        }
        return true;
    }

    /***************************************************************************
     * @return long
     **************************************************************************/
    private long getBytesLeft()
    {
        return fileSize - offset;
    }

    /***************************************************************************
     * @return int
     * @throws IOException
     **************************************************************************/
    public int read() throws IOException
    {
        if( getBytesLeft() < 1 )
        {
            setupNextFile();
        }
        offset += 1;
        return currentStream.read();
    }

    /***************************************************************************
     * @param b byte[]
     * @return int
     * @throws IOException
     **************************************************************************/
    public int read( byte b[] ) throws IOException
    {
        return this.read( b, 0, b.length );
    }

    /***************************************************************************
     * @param b byte[]
     * @param off int
     * @param len int
     * @return int
     * @throws IOException
     **************************************************************************/
    public int read( byte b[], int off, int len ) throws IOException
    {
        int thisLen = 0;
        int bytesRead = 0;
        boolean avail = true;

        while( thisLen < len && avail )
        {
            if( getBytesLeft() < 1 )
            {
                avail = setupNextFile();
            }

            if( avail )
            {
                thisLen = Math.min( ( int )getBytesLeft(), len );
                thisLen = currentStream.read( b, off, thisLen );
                offset += len;
                off += thisLen;
                len -= thisLen;
                bytesRead += thisLen;
            }
        }

        return bytesRead;
    }
}
