package org.jutils.io;

import java.io.EOFException;
import java.io.IOException;

//TODO comments

public class BufferedReadOnlyStream implements IStream
{
    public static int DEFAULT_BUFFER_SIZE = 8 * 1024 * 1024;

    private final IStream stream;
    private final byte[] buffer;

    private int index;
    private int fillCount;
    private long position;
    private Mutator<Long> length;

    public BufferedReadOnlyStream( IStream stream )
    {
        this( stream, DEFAULT_BUFFER_SIZE );
    }

    public BufferedReadOnlyStream( IStream stream, int bufferSize )
    {
        this.stream = stream;
        this.buffer = new byte[bufferSize];
        this.length = null;

        this.index = 0;
        this.position = 0;
        this.fillCount = 0;
        this.length = null;
    }

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

    @Override
    public int read( byte[] buf ) throws IOException
    {
        return read( buf, 0, buf.length );
    }

    @Override
    public void readFully( byte[] buf ) throws EOFException, IOException
    {
        readFully( buf, 0, buf.length );
    }

    @Override
    public int read( byte[] buf, int off, int len ) throws IOException
    {
        int bufAvailable;
        int totalRead = 0;
        int toCopy;

        while( totalRead < len && getAvailable() > 0 )
        {
            bufAvailable = fillCount - index;

            if( bufAvailable < 1 )
            {
                long pos = position + index;

                if( pos < getLength() )
                {
                    fillBuffer( pos );

                    bufAvailable = fillCount;
                }
                else
                {
                    break;
                }
            }

            toCopy = Math.min( len - totalRead, bufAvailable );

            // try
            // {
            System.arraycopy( buffer, index, buf, off, toCopy );
            // }
            // catch( ArrayIndexOutOfBoundsException ex )
            // {
            // throw new RuntimeException( "Src len: " + buffer.length +
            // ", src pos: " + index + ", dest len: " + buf.length +
            // ", dest pos: " + off + ", count: " + toCopy, ex );
            // }

            totalRead += toCopy;
            off += toCopy;
            this.index += toCopy;
        }

        return len;
    }

    @Override
    public void readFully( byte[] buf, int off, int len ) throws EOFException,
        IOException
    {
        int bytesRead = 0;

        if( getAvailable() < len )
        {
            throw new EOFException( "Cannot read " + len +
                " bytes from the stream; only " + getAvailable() +
                " bytes available." );
        }

        while( bytesRead < len )
        {
            bytesRead += read( buf, off + bytesRead, len - bytesRead );
        }
    }

    @Override
    public void close() throws IOException
    {
        stream.close();
    }

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

    private void fillBuffer( long pos ) throws EOFException, IOException
    {
        stream.seek( pos );

        // System.out.println( "Filling buffer" );

        this.position = pos;
        this.index = 0;
        this.fillCount = stream.read( buffer, 0, buffer.length );

        if( fillCount < 1 )
        {
            throw new EOFException();
        }
    }

    @Override
    public void skip( long count ) throws IOException
    {
        seek( getPosition() + count );
    }

    @Override
    public long getAvailable() throws IOException
    {
        return getLength() - getPosition();
    }

    @Override
    public long getPosition() throws IOException
    {
        return position + index;
    }

    @Override
    public long getLength() throws IOException
    {
        long len = -1;

        try
        {
            len = this.length.item;
        }
        catch( NullPointerException ex )
        {
            len = stream.getLength();
            this.length = new Mutator<Long>( len );
        }

        return len;
    }

    @Override
    public void write( byte b ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }

    @Override
    public void write( byte[] buf ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }

    @Override
    public void write( byte[] buf, int off, int len ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }

    private static class Mutator<T>
    {
        public T item;

        // public Mutator()
        // {
        // this( null );
        // }

        public Mutator( T item )
        {
            this.item = item;
        }
    }
}
