package org.jutils.io;

import java.io.IOException;

//TODO comments

public class ItemCloner<T>
{
    private final ByteArrayStream stream;
    private final StreamOutput outputStream;
    private final StreamInput inputStream;

    public ItemCloner()
    {
        this.stream = new ByteArrayStream( 1024 );
        this.outputStream = new StreamOutput( stream );
        this.inputStream = new StreamInput( stream );
    }

    public T cloneItem( T item ) throws IllegalStateException
    {
        T clone = null;

        try
        {
            stream.setLength( 0 );

            stream.seek( 0 );
            XStreamUtils.writeObjectXStream( item, outputStream );

            stream.seek( 0 );
            clone = XStreamUtils.readObjectXStream( inputStream );
        }
        catch( IOException ex )
        {
            throw new IllegalStateException( "Could not clone item: " +
                ex.getMessage(), ex );
        }

        return clone;
    }
}
