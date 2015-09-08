package org.jutils.io;

import java.io.*;

import com.thoughtworks.xstream.XStreamException;

/*******************************************************************************
 * 
 ******************************************************************************/
public class XStreamSerializer<T>
{
    /***************************************************************************
     * 
     **************************************************************************/
    public T read( InputStream stream ) throws IOException, XStreamException
    {
        @SuppressWarnings( "unchecked")
        T data = ( T )XStreamUtils.readObjectXStream( stream );
        return data;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void write( T t, OutputStream stream )
        throws IOException, XStreamException
    {
        XStreamUtils.writeObjectXStream( t, stream );
    }
}
