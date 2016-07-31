package org.jutils.apps.filespy;

import org.jutils.io.IOUtils;

public class ByteCharSequence implements CharSequence
{
    private int offset = -1;

    private int length = -1;

    private byte [] buffer = null;

    public ByteCharSequence( byte [] buf )
    {
        this( buf, 0, buf.length );
    }

    public ByteCharSequence( byte [] buf, int off, int len )
    {
        buffer = buf;
        offset = off;
        length = len;
    }

    @Override
    public int length()
    {
        return length;
    }

    @Override
    public char charAt( int index )
    {
        return ( char )buffer[offset + index];
    }

    @Override
    public CharSequence subSequence( int start, int end )
    {
        return new ByteCharSequence( buffer, offset + start, offset + end );
    }

    @Override
    public String toString()
    {
        return new String( buffer, offset, length, IOUtils.US_ASCII );
    }
}
