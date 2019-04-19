package org.jutils.io;

import java.io.*;

import org.jutils.ValidationException;

import com.thoughtworks.xstream.XStreamException;

/*******************************************************************************
 * Serializes items to XML using XStream.
 * @param <T> the type of item to be serialized.
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
        catch( XStreamException ex )
        {
            throw new ValidationException( ex.getMessage(), ex );
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
