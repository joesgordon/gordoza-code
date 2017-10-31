package org.jutils.io;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import org.jutils.ValidationException;

public interface IReferenceStream<T> extends Closeable
{
    public long getCount();

    public void write( T item ) throws IOException;

    public T read( long index ) throws IOException, ValidationException;

    public List<T> read( long index, int count )
        throws IOException, ValidationException;
}
