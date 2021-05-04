package org.cojo.data;

import org.jutils.core.INamedItem;

/***************************************************************************
 *
 **************************************************************************/
public enum TaskState implements INamedItem
{
    /**  */
    NEW( "New" ),
    /**  */
    DEFERRED( "Deferred" ),
    /**  */
    APPROVED( "Approved" ),
    /**  */
    IN_WORK( "In Work" ),
    /**  */
    COMPLETE( "Complete" ),
    /**  */
    REJECTED( "Rejected" );

    /**  */
    public final String name;

    /***************************************************************************
     * @param name
     **************************************************************************/
    private TaskState( String name )
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
