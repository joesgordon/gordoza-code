package org.cc.edit.io;

import java.io.IOException;

import org.cc.data.ClosedTask;
import org.cc.data.Release;
import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

public class ReleaseSerializer implements IDataSerializer<Release>
{
    private StringSerializer ss;
    private ListSerializer<ClosedTask> ctsSerializer;

    public ReleaseSerializer()
    {
        ss = new StringSerializer();
        ctsSerializer = new ListSerializer<ClosedTask>(
            new ClosedTaskSerializer() );
    }

    @Override
    public Release read( IDataStream stream ) throws IOException
    {
        Release item = new Release();

        item.setName( ss.read( stream ) );
        item.setBaseline( ss.read( stream ) );
        item.setClosedTasks( ctsSerializer.read( stream ) );

        return item;
    }

    @Override
    public void write( Release t, IDataStream stream ) throws IOException
    {
        ss.write( t.getName(), stream );
        ss.write( t.getBaseline(), stream );
        ctsSerializer.write( t.getClosedTasks(), stream );
    }
}
