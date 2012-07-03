package org.jutils.io;

import java.io.File;
import java.io.IOException;

// TODO add a way for the errors to become warnings.
/**
 * @param <T>
 */
public class UserOptionsSerializer<T>
{
    private final UserOptionsCreator<T> creator;
    private final File file;

    private T options;

    public UserOptionsSerializer( UserOptionsCreator<T> creator, File file )
    {
        this.creator = creator;
        this.file = file;
    }

    public T getOptions()
    {
        if( options == null )
        {
            options = read();
        }

        return options;
    }

    public T read()
    {
        T data = null;

        try
        {
            @SuppressWarnings( "unchecked")
            T t = ( T )XStreamUtils.readObjectXStream( file );
            data = t;
        }
        catch( IOException ex )
        {
            data = creator.createDefaultOptions();
        }

        return data;
    }

    public void write()
    {
        if( options != null )
        {
            write( options );
        }
    }

    public void write( T data )
    {
        try
        {
            XStreamUtils.writeObjectXStream( data, file );
        }
        catch( IOException ex )
        {
        }
    }

    public static interface UserOptionsCreator<T>
    {
        public T createDefaultOptions();
    }

}
