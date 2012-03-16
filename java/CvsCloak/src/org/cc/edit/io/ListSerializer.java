package org.cc.edit.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.jutils.io.IDataSerializer;

public class ListSerializer<T> implements IDataSerializer<List<T>>
{
    private IDataSerializer<T> serializer;

    public ListSerializer( IDataSerializer<T> itemSerializer )
    {
        this.serializer = itemSerializer;
    }

    @Override
    public List<T> read( DataInput stream ) throws IOException
    {
        List<T> list = new ArrayList<T>();
        int count = stream.readInt();

        for( int i = 0; i < count; i++ )
        {
            list.add( serializer.read( stream ) );
        }

        return list;
    }

    @Override
    public void write( List<T> t, DataOutput stream ) throws IOException
    {
        if( t == null )
        {
            stream.writeInt( 0 );
        }
        else
        {
            stream.writeInt( t.size() );
            for( T item : t )
            {
                serializer.write( item, stream );
            }
        }
    }

}
