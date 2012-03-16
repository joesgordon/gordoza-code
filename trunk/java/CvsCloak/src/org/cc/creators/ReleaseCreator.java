package org.cc.creators;

import org.cc.data.Release;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ReleaseCreator implements ItemCreator<Release>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Release createItem( String name )
    {
        Release r = new Release();

        r.setName( name );

        return r;
    }
}
