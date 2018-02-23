package org.jutils.apps.summer.tasks;

import java.io.IOException;

import org.jutils.apps.summer.data.ChecksumResult;
import org.jutils.apps.summer.data.SumFile;
import org.jutils.io.cksum.*;
import org.jutils.task.*;
import org.jutils.ui.hex.HexUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChecksumCreationTask implements ITask
{
    /**  */
    public static final int NUM_THREADS = 8;
    /**  */
    private final ChecksumResult input;

    /***************************************************************************
     * @param files
     * @param metrics
     * @param type
     * @param commonDir
     * @param resultListener
     **************************************************************************/
    public ChecksumCreationTask( ChecksumResult input )
    {
        this.input = input;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Checksum Creation";
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run( ITaskStatusHandler handler )
    {
        try
        {
            createChecksums( handler, input );
        }
        catch( IOException ex )
        {
            handler.signalError( new TaskError( "I/O Error", ex ) );
        }
        finally
        {
            handler.signalFinished();
        }
    }

    /***************************************************************************
     * @param stopper
     * @param progress
     * @return
     * @throws IOException
     **************************************************************************/
    public static void createChecksums( ITaskStatusHandler handler,
        ChecksumResult input ) throws IOException
    {
        TaskUpdater updater = new TaskUpdater( handler, input.calculateSize() );
        IChecksum checksummer = CheckSumFactory.createSummer( input.type );
        ChecksumGenenerator generator = new ChecksumGenenerator( checksummer,
            updater );

        long bytesProcessed = 0;

        for( int i = 0; i < input.files.size() && handler.canContinue(); i++ )
        {
            SumFile sf = input.files.get( i );
            byte [] csBytes;
            String cs;

            handler.signalMessage( "Processing " + ( i + 1 ) + " of " +
                input.files.size() + ": " + sf.path );

            csBytes = generator.generateChecksum( sf.file, bytesProcessed );
            cs = HexUtils.toHexString(
                HexUtils.asList( csBytes ) ).toLowerCase();
            sf.checksum = cs;

            bytesProcessed += sf.length;

            updater.update( bytesProcessed );
        }
    }
}
