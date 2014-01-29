package org.jutils.io;

import java.io.IOException;
import java.nio.ByteOrder;

//TODO comments

public interface IDataStream extends IStream
{
    public ByteOrder getOrder();

    public boolean readBoolean() throws IOException;

    public short readShort() throws IOException;

    public int readInt() throws IOException;

    public long readLong() throws IOException;

    public float readFloat() throws IOException;

    public double readDouble() throws IOException;

    public void writeBoolean( boolean v ) throws IOException;

    public void writeShort( short v ) throws IOException;

    public void writeInt( int v ) throws IOException;

    public void writeLong( long v ) throws IOException;

    public void writeFloat( float v ) throws IOException;

    public void writeDouble( double v ) throws IOException;
}
