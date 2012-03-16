package org.cc.creators;

import org.cc.data.Product;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ProductCreator implements ItemCreator<Product>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Product createItem( String name )
    {
        Product p = new Product();

        p.setName( name );

        return p;
    }
}
