package org.jutils.io;

import java.io.File;
import java.io.IOException;

/*******************************************************************************
 *
 ******************************************************************************/
public abstract class XObject
{
    /**  */
    private transient File file = null;

    /***************************************************************************
     *
     **************************************************************************/
    public XObject()
    {
        super();
    }

    /***************************************************************************
     *
     **************************************************************************/
    protected abstract void init();

    /***************************************************************************
     * @param file File
     * @return XObject
     * @throws IOException
     **************************************************************************/
    public static XObject read( File file ) throws IOException
    {
        XObject obj = ( XObject )XStreamUtils.readObjectXStream( file );
        obj.file = file;
        obj.init();

        return obj;
    }

    /***************************************************************************
     * @return File
     **************************************************************************/
    public File getFile()
    {
        return file;
    }

    /***************************************************************************
     * @param file File
     **************************************************************************/
    public void setFile( File file )
    {
        this.file = file;
    }

    /***************************************************************************
     * @param file File
     * @throws IOException
     **************************************************************************/
    public void write( File file ) throws IOException
    {
        this.file = file;
        write();
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    public void write() throws IOException
    {
        XStreamUtils.writeObjectXStream( this, file );
    }

    /***************************************************************************
     * @return Object
     **************************************************************************/
    public Object clone()
    {
        return XStreamUtils.cloneObject( this );
    }
}
