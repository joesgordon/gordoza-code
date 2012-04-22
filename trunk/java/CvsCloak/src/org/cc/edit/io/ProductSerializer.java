package org.cc.edit.io;

import java.io.IOException;

import org.cc.data.Product;
import org.cc.data.Release;
import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ProductSerializer implements IDataSerializer<Product>
{
    /**  */
    private StringSerializer strSerializer;
    /**  */
    private ListSerializer<String> moduleSerializer;
    /**  */
    private ListSerializer<Release> releasesSerializer;

    /***************************************************************************
     * 
     **************************************************************************/
    public ProductSerializer()
    {
        strSerializer = new StringSerializer();
        moduleSerializer = new ListSerializer<String>( strSerializer );
        releasesSerializer = new ListSerializer<Release>(
            new ReleaseSerializer() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Product read( IDataStream stream ) throws IOException
    {
        Product item = new Product();

        item.setName( strSerializer.read( stream ) );
        item.setModules( moduleSerializer.read( stream ) );
        item.setReleases( releasesSerializer.read( stream ) );

        return item;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( Product t, IDataStream stream ) throws IOException
    {
        strSerializer.write( t.getName(), stream );
        moduleSerializer.write( t.getModules(), stream );
        releasesSerializer.write( t.getReleases(), stream );
    }
}
