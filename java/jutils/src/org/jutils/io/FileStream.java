package org.jutils.io;

import java.io.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FileStream implements IStream
{
    /**  */
    private final File file;
    /**  */
    private final RandomAccessFile raf;

    /***************************************************************************
     * @param file
     * @throws FileNotFoundException
     **************************************************************************/
    public FileStream( File file ) throws FileNotFoundException
    {
        this.file = file;
        this.raf = new RandomAccessFile( file, "r" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public File getFile()
    {
        return file;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public byte read() throws IOException
    {
        byte b = raf.readByte();
        return b;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read( byte[] buf ) throws IOException
    {
        return raf.read( buf );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void readFully( byte[] buf ) throws IOException
    {
        readFully( buf, 0, buf.length );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read( byte[] buf, int off, int len ) throws IOException
    {
        return raf.read( buf, off, len );
    }

    @Override
    public void readFully( byte[] buf, int off, int len ) throws IOException
    {
        int bytesRead = 0;

        while( bytesRead < len )
        {
            bytesRead += read( buf, off + bytesRead, len - bytesRead );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void seek( long pos ) throws IOException
    {
        raf.seek( pos );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        raf.close();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getPosition() throws IOException
    {
        return raf.getFilePointer();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getLength() throws IOException
    {
        return raf.length();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte b ) throws IOException
    {
        raf.write( b & 0xFF );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte[] buf ) throws IOException
    {
        raf.write( buf );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte[] buf, int off, int len ) throws IOException
    {
        raf.write( buf, off, len );
    }
}
