package org.cc.edit.io;

import java.io.*;

import org.cc.data.*;
import org.jutils.io.IDataSerializer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RepositorySerializer implements IDataSerializer<Repository>
{
    /**  */
    private FileSerializer fileSerializer;
    /**  */
    private LockInfoSerializer lockInfoSerializer;
    /**  */
    private ListSerializer<Baseline> baselineSerializer;
    /**  */
    private StringSerializer stringSerializer;
    /**  */
    private ListSerializer<Product> productSerializer;

    /***************************************************************************
     * 
     **************************************************************************/
    public RepositorySerializer()
    {
        fileSerializer = new FileSerializer();
        lockInfoSerializer = new LockInfoSerializer();
        baselineSerializer = new ListSerializer<Baseline>(
            new BaselineSerializer() );
        stringSerializer = new StringSerializer();
        productSerializer = new ListSerializer<Product>(
            new ProductSerializer() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Repository read( DataInput stream ) throws IOException
    {
        Repository item = new Repository( fileSerializer.read( stream ) );

        item.setTrunkName( stringSerializer.read( stream ) );
        item.setLockInfo( lockInfoSerializer.read( stream ) );
        item.setBaselines( baselineSerializer.read( stream ) );
        item.setProducts( productSerializer.read( stream ) );

        return item;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( Repository t, DataOutput stream ) throws IOException
    {
        fileSerializer.write( t.getLocation(), stream );
        stringSerializer.write( t.getTrunkName(), stream );
        lockInfoSerializer.write( t.getLockInfo(), stream );
        baselineSerializer.write( t.getBaselines(), stream );
        productSerializer.write( t.getProducts(), stream );
    }
}
