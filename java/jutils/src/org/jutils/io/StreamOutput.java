package org.jutils.io;

import java.io.IOException;
import java.io.OutputStream;

/******************************************************************************
 * 
 ******************************************************************************/
public class StreamOutput extends OutputStream
{
    /**  */
    private final IStream stream;

    /***************************************************************************
     * @param stream
     **************************************************************************/
    public StreamOutput( IStream stream )
    {
        this.stream = stream;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( int b ) throws IOException
    {
        stream.write( ( byte )b );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte b[], int off, int len ) throws IOException
    {
        stream.write( b, off, len );
    }
}
