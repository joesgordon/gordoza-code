package org.jutils.io;

import java.io.EOFException;
import java.io.IOException;

import org.jutils.Utils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BufferedReadOnlyStream implements IStream
{
    /**  */
    public static int DEFAULT_BUFFER_SIZE = 8 * 1024 * 1024;

    /**  */
    private final IStream stream;
    /**  */
    private final byte [] buffer;

    /**  */
    private int index;
    /**  */
    private int fillCount;
    /**  */
    private long position;
    /**  */
    private Long length;

    /***************************************************************************
     * @param stream
     **************************************************************************/
    public BufferedReadOnlyStream( IStream stream )
    {
        this( stream, DEFAULT_BUFFER_SIZE );
    }

    /***************************************************************************
     * @param stream
     * @param bufferSize
     **************************************************************************/
    public BufferedReadOnlyStream( IStream stream, int bufferSize )
    {
        this.stream = stream;
        this.buffer = new byte[bufferSize];

        this.index = 0;
        this.fillCount = 0;
        this.position = 0;
        this.length = null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public byte read() throws EOFException, IOException
    {
        if( index < fillCount )
        {
            return buffer[index++];
        }
        else if( getAvailable() < 1 )
        {
            throw new EOFException();
        }

        seek( position + index );

        return buffer[index++];
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read( byte [] buf ) throws IOException
    {
        return read( buf, 0, buf.length );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void readFully( byte [] buf ) throws EOFException, IOException
    {
        readFully( buf, 0, buf.length );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read( byte [] buf, int off, int len ) throws IOException
    {
        int bufAvailable = fillCount - index;
        int totalRead = 0;
        int toCopy;

        // ---------------------------------------------------------------------
        // Read from the available buffer if possible.
        // ---------------------------------------------------------------------
        if( bufAvailable > 0 )
        {
            toCopy = Math.min( len - totalRead, bufAvailable );
            Utils.byteArrayCopy( buffer, index, buf, off, toCopy );

            totalRead += toCopy;
            this.index += toCopy;
        }

        // ---------------------------------------------------------------------
        // Read directly from the base stream if needed. Cache next afterwards.
        // ---------------------------------------------------------------------
        if( totalRead < len )
        {
            toCopy = stream.read( buf, off + totalRead, len - totalRead );

            if( toCopy > -1 )
            {
                totalRead += toCopy;

                try
                {
                    fillBuffer( stream.getPosition() );
                }
                catch( EOFException ex )
                {
                    ;
                }
            }
        }

        return totalRead;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void readFully( byte [] buf, int off, int len )
        throws EOFException, IOException
    {
        int bytesRead = 0;

        if( getAvailable() < len )
        {
            throw new EOFException(
                "Cannot read " + len + " bytes from the stream; only " +
                    getAvailable() + " bytes available." );
        }

        while( bytesRead < len )
        {
            bytesRead += read( buf, off + bytesRead, len - bytesRead );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        stream.close();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void seek( long pos ) throws IOException
    {
        if( pos >= this.position && pos < ( this.position + this.fillCount ) )
        {
            this.index = ( int )( pos - position );
        }
        else if( pos < getLength() )
        {
            fillBuffer( pos );
        }
        else
        {
            this.position = getLength();
            this.index = 0;
            this.fillCount = 0;
        }
    }

    /***************************************************************************
     * @param pos
     * @throws EOFException
     * @throws IOException
     **************************************************************************/
    private void fillBuffer( long pos ) throws EOFException, IOException
    {
        stream.seek( pos );

        // LogUtils.printDebug( "Filling buffer" );

        this.position = pos;
        this.index = 0;
        this.fillCount = stream.read( buffer, 0, buffer.length );

        if( fillCount < 1 )
        {
            throw new EOFException();
        }
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
        return position + index;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getLength() throws IOException
    {
        long len = -1;

        try
        {
            len = length.longValue();
        }
        catch( NullPointerException ex )
        {
            len = stream.getLength();
            this.length = len;
        }

        return len;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte b ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte [] buf ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte [] buf, int off, int len ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }
}
