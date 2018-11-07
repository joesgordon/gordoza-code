package org.jutils.io;

import java.io.*;

import org.jutils.ValidationException;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class XStreamFileSerializer<T> implements IStdSerializer<T, File>
{
    /**  */
    private final XStreamStreamSerializer<T> serializer;

    /***************************************************************************
     * @param cls
     **************************************************************************/
    public XStreamFileSerializer( Class<? extends T> cls )
    {
        this.serializer = new XStreamStreamSerializer<>( cls );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public T read( File file ) throws IOException, ValidationException
    {
        try( FileInputStream fis = new FileInputStream( file ) )
        {
            return serializer.read( fis );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( T item, File file ) throws IOException
    {
        try( FileOutputStream stream = new FileOutputStream( file ) )
        {
            serializer.write( item, stream );
        }
    }
}
