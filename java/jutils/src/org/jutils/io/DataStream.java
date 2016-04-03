package org.jutils.io;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.jutils.utils.ByteOrdering;

/*******************************************************************************
 * Implements a stream that reads/writes intrinsic data types.
 ******************************************************************************/
public class DataStream implements IDataStream
{
    /** The underlying stream. */
    private IStream stream;

    /** Buffer to be used for intrinsic reading/writing. */
    private final ByteBuffer buffer;
    /** The bytes that seed the buffer. */
    private final byte [] bytes;
    /** The byte order used for reading/writing. */
    private final ByteOrdering order;

    /***************************************************************************
     * Creates a data stream that reads data with big endian byte ordering.
     * @param stream the stream to be wrapped as a data stream.
     **************************************************************************/
    public DataStream( IStream stream )
    {
        this( stream, ByteOrdering.BIG_ENDIAN );
    }

    /***************************************************************************
     * Creates a data stream that reads data with the provided byte ordering.
     * @param stream the stream to be wrapped as a data stream.
     * @param order the byte order of data in the stream.
     **************************************************************************/
    public DataStream( IStream stream, ByteOrdering order )
    {
        this.stream = stream;

        this.bytes = new byte[8];

        this.order = order;

        this.buffer = ByteBuffer.wrap( bytes );
        this.buffer.order( order.order );
    }

    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // IStream specific methods.
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public byte read() throws EOFException, IOException
    {
        return stream.read();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read( byte [] buf ) throws IOException
    {
        return stream.read( buf );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void readFully( byte [] buf ) throws EOFException, IOException
    {
        stream.readFully( buf );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read( byte [] buf, int off, int len ) throws IOException
    {
        return stream.read( buf, off, len );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void readFully( byte [] buf, int off, int len )
        throws EOFException, IOException
    {
        stream.readFully( buf, off, len );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void seek( long pos ) throws IOException
    {
        stream.seek( pos );
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
    public long getPosition() throws IOException
    {
        return stream.getPosition();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getLength() throws IOException
    {
        return stream.getLength();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte b ) throws IOException
    {
        stream.write( b );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte [] buf ) throws IOException
    {
        stream.write( buf );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte [] buf, int off, int len ) throws IOException
    {
        stream.write( buf, off, len );
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
    public boolean readBoolean() throws IOException
    {
        return read() != 0;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public short readShort() throws IOException
    {
        readFully( bytes, 0, 2 );
        buffer.rewind();

        return buffer.getShort();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int readInt() throws IOException
    {
        readFully( bytes, 0, 4 );
        buffer.rewind();

        return buffer.getInt();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long readLong() throws IOException
    {
        readFully( bytes, 0, 8 );
        buffer.rewind();

        return buffer.getLong();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public float readFloat() throws IOException
    {
        readFully( bytes, 0, 4 );
        buffer.rewind();

        return buffer.getFloat();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public double readDouble() throws IOException
    {
        readFully( bytes, 0, 8 );
        buffer.rewind();

        return buffer.getDouble();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeBoolean( boolean v ) throws IOException
    {
        write( ( byte )( v ? 1 : 0 ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeShort( short v ) throws IOException
    {
        buffer.rewind();
        buffer.putShort( v );

        write( bytes, 0, 2 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeInt( int v ) throws IOException
    {
        buffer.rewind();
        buffer.putInt( v );

        write( bytes, 0, 4 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeLong( long v ) throws IOException
    {
        buffer.rewind();
        buffer.putLong( v );

        write( bytes, 0, 8 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeFloat( float v ) throws IOException
    {
        buffer.rewind();
        buffer.putFloat( v );

        write( bytes, 0, 4 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeDouble( double v ) throws IOException
    {
        buffer.rewind();
        buffer.putDouble( v );

        write( bytes, 0, 8 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void skip( long count ) throws IOException
    {
        stream.skip( count );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getAvailable() throws IOException
    {
        return stream.getAvailable();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ByteOrdering getOrder()
    {
        return order;
    }
}
