package org.jutils.io;

import java.io.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class XStreamSerializer<T>
{
    /***************************************************************************
     * @param writer
     **************************************************************************/
    public XStreamSerializer()
    {
        ;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @SuppressWarnings( "unchecked")
    public T read( InputStream stream ) throws IOException
    {
        return ( T )XStreamUtils.readObjectXStream( stream );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void write( T t, OutputStream stream ) throws IOException
    {
        XStreamUtils.writeObjectXStream( t, stream );
    }
}
