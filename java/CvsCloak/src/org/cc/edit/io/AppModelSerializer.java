package org.cc.edit.io;

import java.io.IOException;

import org.cc.data.Repository;
import org.cc.data.VersioningSystem;
import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AppModelSerializer implements IDataSerializer<VersioningSystem>
{
    /**  */
    private StringSerializer ss;
    /**  */
    private LockInfoSerializer lockInfoSerializer;
    /**  */
    private ListSerializer<Repository> rsSerializer;

    /***************************************************************************
     * 
     **************************************************************************/
    public AppModelSerializer()
    {
        ss = new StringSerializer();
        lockInfoSerializer = new LockInfoSerializer();
        rsSerializer = new ListSerializer<Repository>(
            new RepositorySerializer() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public VersioningSystem read( IDataStream stream ) throws IOException
    {
        VersioningSystem item = new VersioningSystem();

        item.setVersion( ss.read( stream ) );
        item.setLockInfo( lockInfoSerializer.read( stream ) );
        if( stream.readBoolean() )
        {
            item.setDefaultRepository( ss.read( stream ) );
        }
        else
        {
            item.setDefaultRepository( null );
        }
        item.setRepostories( rsSerializer.read( stream ) );

        return item;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( VersioningSystem t, IDataStream stream )
        throws IOException
    {
        boolean hasDefault = t.getDefaultRepository() != null;

        ss.write( t.getVersion(), stream );
        lockInfoSerializer.write( t.getLockInfo(), stream );
        stream.writeBoolean( hasDefault );
        if( hasDefault )
        {
            ss.write( t.getDefaultRepository(), stream );
        }
        rsSerializer.write( t.getRepositories(), stream );
    }
}
