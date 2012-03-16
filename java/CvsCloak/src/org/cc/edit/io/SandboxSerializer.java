package org.cc.edit.io;

import java.io.*;

import org.cc.data.Sandbox;
import org.jutils.io.IDataSerializer;

public class SandboxSerializer implements IDataSerializer<Sandbox>
{
    private FileSerializer ss;

    public SandboxSerializer()
    {
        ss = new FileSerializer();
    }

    @Override
    public Sandbox read( DataInput stream ) throws IOException
    {
        File file = ss.read( stream );
        Sandbox s = new Sandbox( file );
        return s;
    }

    @Override
    public void write( Sandbox t, DataOutput stream ) throws IOException
    {
        ss.write( t.getLocation(), stream );
    }
}
