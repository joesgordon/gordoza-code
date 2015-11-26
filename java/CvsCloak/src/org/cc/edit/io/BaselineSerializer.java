package org.cc.edit.io;

import java.io.IOException;

import org.cc.data.*;
import org.jutils.ValidationException;
import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BaselineSerializer implements IDataSerializer<Baseline>
{
    /**  */
    private StringSerializer ss;
    /**  */
    private LockInfoSerializer lockInfoSerializer;
    /**  */
    private ListSerializer<ClosedTask> ctsSerializer;
    /**  */
    private ListSerializer<OpenTask> otsSerializer;

    /***************************************************************************
     * 
     **************************************************************************/
    public BaselineSerializer()
    {
        ss = new StringSerializer();
        lockInfoSerializer = new LockInfoSerializer();
        ctsSerializer = new ListSerializer<ClosedTask>(
            new ClosedTaskSerializer() );
        otsSerializer = new ListSerializer<OpenTask>(
            new OpenTaskSerializer() );
    }

    /***************************************************************************
     * @throws ValidationException
     **************************************************************************/
    @Override
    public Baseline read( IDataStream stream )
        throws IOException, ValidationException
    {
        Baseline item = new Baseline();

        item.setName( ss.read( stream ) );
        item.setLockInfo( lockInfoSerializer.read( stream ) );
        item.setUpdatingTask( ss.read( stream ) );
        item.setLastClosedTask( ss.read( stream ) );
        item.setStartingRelease( ss.read( stream ) );
        item.setClosedTasks( ctsSerializer.read( stream ) );
        item.setOpenTasks( otsSerializer.read( stream ) );

        return item;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( Baseline t, IDataStream stream ) throws IOException
    {
        ss.write( t.getName(), stream );
        lockInfoSerializer.write( t.getLockInfo(), stream );
        ss.write( t.getUpdatingTask(), stream );
        ss.write( t.getLastClosedTask(), stream );
        ss.write( t.getStartingRelease(), stream );
        ctsSerializer.write( t.getClosedTasks(), stream );
        otsSerializer.write( t.getOpenTasks(), stream );
    }
}
