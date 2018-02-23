package org.jutils.apps.jhex.task;

import java.io.IOException;

import org.jutils.datadist.DataDistribution;
import org.jutils.datadist.DistributionBuilder;
import org.jutils.io.IStream;
import org.jutils.task.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataDistributionTask implements ITask
{
    /**  */
    private final IStream stream;
    /**  */
    private DataDistribution dist;

    /***************************************************************************
     * @param stream
     **************************************************************************/
    public DataDistributionTask( IStream stream )
    {
        this.stream = stream;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run( ITaskStatusHandler handler )
    {
        try
        {
            readStream( handler );
        }
        catch( IOException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     * @param handler
     * @throws IOException
     **************************************************************************/
    private void readStream( ITaskStatusHandler handler ) throws IOException
    {
        TaskUpdater updater = new TaskUpdater( handler, stream.getLength() );
        byte [] buffer = new byte[2 * 1024 * 1024];
        DistributionBuilder builder = new DistributionBuilder();
        long analyzed = 0;
        long count = stream.getLength() - 3;

        stream.seek( 0 );

        while( analyzed < count && handler.canContinue() )
        {
            long offset = stream.getPosition();
            int bytesRead = stream.read( buffer );

            if( bytesRead < 4 )
            {
                break;
            }

            updater.update( offset );

            builder.addData( buffer, 0, bytesRead );

            stream.seek( offset + bytesRead - 3 );
            analyzed += bytesRead;
        }

        dist = builder.buildDistribution();

        // LogUtils.printInfo( Utils.NEW_LINE + dist.getDescription() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public DataDistribution getDistribution()
    {
        return dist;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Analyzing Data";
    }
}
