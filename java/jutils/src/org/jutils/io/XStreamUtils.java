package org.jutils.io;

import java.io.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;

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
        writeObjectXStream( obj, stream );
        stream.close();
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
    public static Object readObjectXStream( File file )
        throws FileNotFoundException, IOException, XStreamException
    {
        FileInputStream fis = new FileInputStream( file );
        Object obj = readObjectXStream( fis );
        fis.close();
        return obj;
    }

    /***************************************************************************
     * @param file File
     * @return Object
     * @throws IOException
     **************************************************************************/
    public static Object readObjectXStream( InputStream inputStream )
        throws IOException, XStreamException
    {
        XStream xstream = new XStream();

        return xstream.fromXML( inputStream );
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
