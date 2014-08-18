package org.jutils.io;

import java.io.*;

//TODO comments

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
        this( file, false );
    }

    /***************************************************************************
     * @param file
     * @throws FileNotFoundException
     **************************************************************************/
    public FileStream( File file, boolean readOnly )
        throws FileNotFoundException
    {
        this.file = file;
        this.raf = new RandomAccessFile( file, readOnly ? "r" : "rw" );
    }

    /***************************************************************************
     * @param length
     * @throws IOException
     **************************************************************************/
    public void setLength( long length ) throws IOException
    {
        raf.setLength( length );
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
    public int read( byte [] buf ) throws IOException
    {
        return raf.read( buf );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void readFully( byte [] buf ) throws IOException
    {
        readFully( buf, 0, buf.length );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read( byte [] buf, int off, int len ) throws IOException
    {
        return raf.read( buf, off, len );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void readFully( byte [] buf, int off, int len ) throws IOException
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
        if( pos < 0 )
        {
            throw new IOException( "Negative seek offset" );
        }
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
    public void skip( long count ) throws IOException
    {
        seek( getPosition() + count );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getAvailable() throws IOException
    {
        return getLength() - getPosition();
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
    public void write( byte [] buf ) throws IOException
    {
        raf.write( buf );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte [] buf, int off, int len ) throws IOException
    {
        raf.write( buf, off, len );
    }
}
