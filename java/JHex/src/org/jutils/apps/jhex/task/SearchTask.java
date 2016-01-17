package org.jutils.apps.jhex.task;

import java.io.IOException;

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
    /**  */
    private final boolean forward;

    public long foundOffset;

    /***************************************************************************
     * @param bytes
     * @param stream
     * @param offset
     * @param isForward
     **************************************************************************/
    public SearchTask( byte [] bytes, IStream stream, long offset,
        boolean isForward )
    {
        this.bytes = bytes;
        this.stream = stream;
        this.offset = offset;
        this.forward = isForward;
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
        // @SuppressWarnings( "resource")
        // BufferedStream stream = new BufferedStream( this.stream,
        // 8 * 1024 * 1024 );

        // LogUtils.printDebug( "Searching for: " + HexUtils.toHexString( bytes
        // ) +
        // " @ " + String.format( "%016X", offset ) + " " +
        // ( forward ? "Forward" : "Backward" ) );

        stream.seek( offset );

        long count = ( forward ? stream.getAvailable() : offset ) -
            bytes.length + 1;
        byte b;
        boolean found = false;
        int seekInc = forward ? 0 : 2;

        TaskUpdater updater = new TaskUpdater( handler, count );

        for( long idx = 0; idx < count && !found &&
            handler.canContinue(); idx++ )
        {
            if( bytes.length > stream.getAvailable() )
            {
                break;
            }

            found = true;

            for( int i = 0; i < bytes.length; i++ )
            {
                b = stream.read();

                if( b != bytes[i] )
                {
                    found = false;
                    long seek = -( i + seekInc );
                    if( seekInc != 0 )
                    {
                        // LogUtils.printDebug( "skipping: " + seek );
                        stream.skip( seek );
                    }
                    break;
                }
            }

            updater.update( idx );
        }

        foundOffset = -1;

        if( found )
        {
            foundOffset = stream.getPosition() - bytes.length;
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
