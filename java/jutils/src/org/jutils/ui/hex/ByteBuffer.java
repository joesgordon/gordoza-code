package org.jutils.ui.hex;

//TODO comments

public class ByteBuffer implements IByteBuffer
{
    private final byte[] buf;

    public ByteBuffer( byte[] buffer )
    {
        buf = buffer;
    }

    @Override
    public byte get( int index )
    {
        return buf[index];
    }

    @Override
    public int size()
    {
        return buf.length;
    }

    @Override
    public boolean isEditable()
    {
        return true;
    }

    @Override
    public void set( int index, byte value )
    {
        buf[index] = value;
    }

    @Override
    public byte[] getBytes()
    {
        return buf;
    }
}
