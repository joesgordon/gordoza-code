package org.jutils.io;

import java.io.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class XStreamUtils
{
    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private XStreamUtils()
    {
        ;
    }

    /***************************************************************************
     * @param obj Object
     * @param file File
     * @throws IOException
     **************************************************************************/
    public static void writeObjectXStream( Object obj, File file )
        throws IOException, XStreamException
    {
        try( FileOutputStream stream = new FileOutputStream( file ) )
        {
            writeObjectXStream( obj, stream );
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
     * @param obj
     * @param outStream
     * @throws IOException
     **************************************************************************/
    public static String writeObjectXStream( Object obj )
        throws IOException, XStreamException
    {
        XStream xstream = new XStream();

        return xstream.toXML( obj );
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
        Object obj;

        try( FileInputStream fis = new FileInputStream( file ) )
        {
            obj = readObjectXStream( fis );
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
     * @param str
     * @return
     **************************************************************************/
    public static <T> T readObjectXStream( String str )
    {
        XStream xstream = new XStream();

        Object obj = xstream.fromXML( str );
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

        try( ByteArrayOutputStream outputStream = new ByteArrayOutputStream() )
        {
            writeObjectXStream( obj, outputStream );

            byte [] buf = outputStream.toByteArray();

            try( ByteArrayInputStream inputStream = new ByteArrayInputStream(
                buf, 0, buf.length ) )
            {
                outputStream.close();

                @SuppressWarnings( "unchecked")
                T t = ( T )readObjectXStream( inputStream );
                inputStream.close();
                clone = t;
            }
        }
        catch( IOException ex )
        {
            throw new IllegalStateException( ex.getMessage() );
        }

        return clone;
    }
}
