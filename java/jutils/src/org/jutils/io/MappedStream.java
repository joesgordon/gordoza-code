package org.jutils.io;

import java.io.*;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

/*******************************************************************************
 * According to <a
 * href="http://stackoverflow.com/a/5623638/1741">stackoverflow</a> and several
 * other sources, a {@link RandomAccessFile} should be buffered using a
 * memory-mapped file via {@link FileChannel#map(MapMode, long, long)}. This is
 * an implementation of said buffering.
 ******************************************************************************/
public class MappedStream implements IDataStream
{
    /**  */
    public static int DEFAULT_BUFFER_SIZE = 8 * 1024 * 1024;

    /**  */
    private final FileChannel channel;
    /**  */
    private final RandomAccessFile raf;
    /**  */
    private final MapMode mode;
    /**  */
    private final int bufferSize;
    /**  */
    private final ByteOrder order;

    /**  */
    private long length;
    /**  */
    private long bufferPos;
    /**  */
    private MappedByteBuffer buffer;

    /***************************************************************************
     * @param file
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    public MappedStream( File file ) throws FileNotFoundException, IOException
    {
        this( file, false );
    }

    /***************************************************************************
     * @param file
     * @param readOnly
     * @throws FileNotFoundException
     **************************************************************************/
    public MappedStream( File file, boolean readOnly )
        throws FileNotFoundException
    {
        this( file, readOnly, ByteOrder.BIG_ENDIAN );
    }

    /***************************************************************************
     * @param file
     * @param readOnly
     * @param order
     * @throws FileNotFoundException
     **************************************************************************/
    public MappedStream( File file, boolean readOnly, ByteOrder order )
        throws FileNotFoundException
    {
        this( file, readOnly, order, DEFAULT_BUFFER_SIZE );
    }

    /***************************************************************************
     * @param file
     * @param readOnly
     * @param order
     * @param bufferSize
     * @throws FileNotFoundException
     **************************************************************************/
    public MappedStream( File file, boolean readOnly, ByteOrder order,
        int bufferSize ) throws FileNotFoundException
    {
        if( bufferSize < 1024 )
        {
            bufferSize = 1024;
        }

        if( !readOnly )
        {
            throw new IllegalArgumentException(
                "Writing is not yet supported." );
        }

        this.raf = new RandomAccessFile( file, "r" );
        this.channel = raf.getChannel();
        this.mode = readOnly ? MapMode.READ_ONLY : MapMode.READ_WRITE;
        this.order = order;
        this.bufferSize = bufferSize;

        this.buffer = null;
        this.length = -1;
    }

    /***************************************************************************
     * @param position
     * @throws IOException
     **************************************************************************/
    private void readBuffer( long position ) throws IOException
    {
        int size = bufferSize;

        if( position > -1 && position < getLength() )
        {
            if( getAvailable() < size )
            {
                size = ( int )( getAvailable() );
            }
        }
        else
        {
            throw new EOFException( "End of file" );
        }

        buffer = channel.map( mode, position, size );
        buffer.load();
        buffer.order( order );

        bufferPos = position;
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    private void checkBuffer() throws IOException
    {
        if( buffer == null )
        {
            readBuffer( bufferPos );
        }
        else if( !buffer.hasRemaining() )
        {
            readBuffer( bufferPos + buffer.position() );
        }
    }

    /***************************************************************************
     * @param smallSize
     * @throws IOException
     **************************************************************************/
    private void checkBuffer( int smallSize ) throws IOException
    {
        if( buffer == null )
        {
            readBuffer( bufferPos );
        }
        else if( getAvailable() < smallSize )
        {
            throw new EOFException( "Too few bytes left in stream (" +
                getAvailable() + ") to read " + smallSize );
        }
        else if( buffer.remaining() < smallSize )
        {
            readBuffer( bufferPos + buffer.position() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public byte read() throws EOFException, IOException
    {
        checkBuffer();

        return buffer.get();
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
        int totalRead = 0;
        int toCopy;

        while( totalRead < len && getAvailable() > 0 )
        {
            try
            {
                checkBuffer();
            }
            catch( EOFException ex )
            {
                return totalRead;
            }

            toCopy = Math.min( len - totalRead, buffer.remaining() );

            buffer.get( buf, off, toCopy );

            totalRead += toCopy;
            off += toCopy;
        }

        return len;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void readFully( byte [] buf, int off, int len )
        throws EOFException, IOException
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
    public void close() throws IOException
    {
        raf.close();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void seek( long pos ) throws IOException
    {
        if( pos >= this.bufferPos &&
            pos < ( this.bufferPos + this.buffer.position() ) )
        {
            buffer.position( ( int )( pos - bufferPos ) );
        }
        else if( pos < getLength() )
        {
            readBuffer( pos );
        }
        else
        {
            this.bufferPos = getLength();
            this.buffer = null;
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
        return bufferPos + ( buffer != null ? buffer.position() : 0 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getLength() throws IOException
    {
        if( length < 0 )
        {
            length = raf.length();
        }

        return length;
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

    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // IDataStream specific methods.
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ByteOrder getOrder()
    {
        return order;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean readBoolean() throws IOException
    {
        checkBuffer();

        return buffer.get() != 0;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public short readShort() throws IOException
    {
        checkBuffer( 2 );

        return buffer.getShort();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int readInt() throws IOException
    {
        checkBuffer( 4 );

        return buffer.getInt();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long readLong() throws IOException
    {
        checkBuffer( 8 );

        return buffer.getLong();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public float readFloat() throws IOException
    {
        checkBuffer( 4 );

        return buffer.getFloat();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public double readDouble() throws IOException
    {
        checkBuffer( 8 );

        return buffer.getDouble();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeBoolean( boolean v ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeShort( short v ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeInt( int v ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeLong( long v ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeFloat( float v ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeDouble( double v ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }
}
