package org.jutils.io;

import java.io.IOException;
import java.io.InputStream;

//TODO comments

public class StreamInput extends InputStream
{
    private final IStream stream;

    public StreamInput( IStream stream )
    {
        this.stream = stream;
    }

    @Override
    public int read() throws IOException
    {
        return stream.read() & 0xFF;
    }

    @Override
    public int read( byte b [], int off, int len ) throws IOException
    {
        return stream.read( b, off, len );
    }

    @Override
    public int available() throws IOException
    {
        return ( int )stream.getAvailable();
    }
}
