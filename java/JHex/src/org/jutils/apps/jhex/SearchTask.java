package org.jutils.apps.jhex;

import java.io.IOException;

import org.jutils.io.BufferedStream;
import org.jutils.io.IStream;
import org.jutils.task.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SearchTask implements ITask
{
    /**  */
    private final byte [] bytes;
    /**  */
    private final IStream stream;
    /**  */
    private final long offset;

    public long foundOffset;

    /***************************************************************************
     * @param bytes
     * @param stream
     * @param offset
     **************************************************************************/
    public SearchTask( byte [] bytes, IStream stream, long offset )
    {
        this.bytes = bytes;
        this.stream = stream;
        this.offset = offset;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run( ITaskHandler handler )
    {
        try
        {
            search( handler );
        }
        catch( IOException ex )
        {
            handler.signalError( new TaskError( "I/O Error", ex ) );
        }
        catch( Throwable ex )
        {
            handler.signalError( new TaskError( "Error Searching", ex ) );
        }
        finally
        {
            handler.signalFinished();
        }
    }

    /***************************************************************************
     * @param handler
     * @throws IOException
     **************************************************************************/
    private void search( ITaskHandler handler ) throws IOException
    {
        @SuppressWarnings( "resource")
        BufferedStream stream = new BufferedStream( this.stream );

        stream.seek( offset );

        long remaining = stream.getAvailable();
        byte b;
        boolean found = false;

        TaskUpdater updater = new TaskUpdater( handler, remaining );

        while( remaining > 0 && !found )
        {
            b = stream.read();
            remaining--;

            found = true;

            for( int i = 0; i < bytes.length; i++ )
            {
                if( b == bytes[i] )
                {
                    b = stream.read();
                    remaining--;
                }
                else
                {
                    found = false;
                    if( i > 0 )
                    {
                        stream.seek( -i );
                        remaining += i;
                    }
                    break;
                }
            }

            updater.update( updater.length.longValue() - remaining );
        }

        foundOffset = -1;

        if( found )
        {
            foundOffset = stream.getPosition() - bytes.length - 1;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Byte Search";
    }
}
