package org.cc.edit.io;

import java.io.File;
import java.io.IOException;

import org.cc.data.Sandbox;
import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

public class SandboxSerializer implements IDataSerializer<Sandbox>
{
    private FileSerializer ss;

    public SandboxSerializer()
    {
        ss = new FileSerializer();
    }

    @Override
    public Sandbox read( IDataStream stream ) throws IOException
    {
        File file = ss.read( stream );
        Sandbox s = new Sandbox( file );
        return s;
    }

    @Override
    public void write( Sandbox t, IDataStream stream ) throws IOException
    {
        ss.write( t.getLocation(), stream );
    }
}
