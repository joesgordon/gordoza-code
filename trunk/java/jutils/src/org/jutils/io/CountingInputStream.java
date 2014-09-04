package org.jutils.io;

import java.io.*;

public class CountingInputStream extends FilterInputStream
{
    private long offset;

    public CountingInputStream( InputStream stream )
    {
        super( stream );

        offset = 0;
    }

    public int read() throws IOException
    {
        int b = super.read();

        if( b > -1 )
        {
            offset++;
        }

        return b;
    }

    public int read( byte b [] ) throws IOException
    {
        return read( b, 0, b.length );
    }

    public int read( byte b [], int off, int len ) throws IOException
    {
        int bytesRead = super.read( b, off, len );

        if( bytesRead > -1 )
        {
            offset += bytesRead;
        }

        return bytesRead;
    }

    public long skip( long n ) throws IOException
    {
        long bytesSkipped = super.skip( n );

        offset += bytesSkipped;

        return bytesSkipped;
    }

    public long getOffset()
    {
        return offset;
    }
}
