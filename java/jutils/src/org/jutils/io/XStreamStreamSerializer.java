package org.jutils.io;

import java.io.*;

import org.jutils.ValidationException;

import com.thoughtworks.xstream.XStream;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class XStreamStreamSerializer<T>
    implements ISerializer<T, InputStream, OutputStream>
{
    /**  */
    private final XStream xstream;

    /***************************************************************************
     * @param cls
     **************************************************************************/
    public XStreamStreamSerializer( Class<? extends T> cls )
    {
        this.xstream = XStreamUtils.createXStream( cls );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public T read( InputStream stream ) throws IOException, ValidationException
    {
        Object obj = xstream.fromXML( stream );
        @SuppressWarnings( "unchecked")
        T item = ( T )obj;
        return item;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( T item, OutputStream stream ) throws IOException
    {
        xstream.toXML( item, stream );
    }
}
