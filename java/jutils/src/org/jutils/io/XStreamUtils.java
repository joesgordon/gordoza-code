package org.jutils.io;

import java.io.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;

// TODO comments

public class XStreamUtils
{
    /***************************************************************************
     * @param obj Object
     * @param file File
     * @throws IOException
     **************************************************************************/
    public static void writeObjectXStream( Object obj, File file )
        throws IOException, XStreamException
    {
        FileOutputStream stream = new FileOutputStream( file );

        try
        {
            writeObjectXStream( obj, stream );
        }
        finally
        {
            stream.close();
        }
    }

    /***************************************************************************
     * @param obj
     * @param outStream
     * @throws IOException
     **************************************************************************/
    public static void writeObjectXStream( Object obj, OutputStream outStream )
        throws IOException, XStreamException
    {
        XStream xstream = new XStream();

        xstream.toXML( obj, outStream );
    }

    /***************************************************************************
     * @param file File
     * @return Object
     * @throws IOException
     **************************************************************************/
    @SuppressWarnings( "unchecked")
    public static <T> T readObjectXStream( File file )
        throws FileNotFoundException, IOException, XStreamException
    {
        FileInputStream fis = null;
        Object obj;

        try
        {
            fis = new FileInputStream( file );
            obj = readObjectXStream( fis );
        }
        finally
        {
            if( fis != null )
            {
                fis.close();
            }
        }

        return ( T )obj;
    }

    /***************************************************************************
     * @param file File
     * @return Object
     * @throws IOException
     **************************************************************************/
    public static <T> T readObjectXStream( InputStream inputStream )
        throws IOException, XStreamException
    {
        XStream xstream = new XStream();

        Object obj = xstream.fromXML( inputStream );
        @SuppressWarnings( "unchecked")
        T t = ( T )obj;

        return t;
    }

    /***************************************************************************
     * Performs a "deep copy" clone of the given object.
     * @param obj the object to be cloned.
     * @return a "deep copy" clone of the given object.
     **************************************************************************/
    public static <T> T cloneObject( T obj )
    {
        T clone = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = null;

        try
        {
            writeObjectXStream( obj, outputStream );
            inputStream = new ByteArrayInputStream( outputStream.toByteArray(),
                0, outputStream.size() );
            outputStream.close();

            @SuppressWarnings( "unchecked")
            T t = ( T )readObjectXStream( inputStream );
            inputStream.close();
            clone = t;
        }
        catch( IOException ex )
        {
            throw new IllegalStateException( ex.getMessage() );
        }

        return clone;
    }
}
