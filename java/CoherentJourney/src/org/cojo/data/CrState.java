package org.cojo.data;

import org.jutils.INamedItem;

/***************************************************************************
 *
 **************************************************************************/
public enum CrState implements INamedItem
{
    /**  */
    NEW( "New" ),
    /**  */
    AWATING( "Awaiting" ),
    /**  */
    DEFERRED( "Deferred" ),
    /**  */
    APPROVED( "Approved" ),
    /**  */
    IN_WORK( "In Work" ),
    /**  */
    CLOSED( "Closed" );

    /**  */
    public final String name;

    /***************************************************************************
     * @param name
     **************************************************************************/
    private CrState( String name )
    {
        this.name = name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }
}
