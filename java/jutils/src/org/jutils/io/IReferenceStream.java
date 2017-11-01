package org.jutils.io;

import java.io.*;
import java.util.List;

import org.jutils.ValidationException;

public interface IReferenceStream<T> extends Closeable
{
    public long getCount();

    public void write( T item ) throws IOException;

    public T read( long index ) throws IOException, ValidationException;

    public List<T> read( long index, int count )
        throws IOException, ValidationException;

    public IStream getItemsStream();

    public void setItemsFile( File file ) throws IOException;

    public void removeAll() throws IOException;
}
