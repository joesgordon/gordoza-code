package org.jutils.utils;

import java.nio.ByteOrder;

import org.jutils.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum ByteOrdering implements INamedItem
{
    LITTLE_ENDIAN( "Little Endian", ByteOrder.LITTLE_ENDIAN ),
    BIG_ENDIAN( "Big Endian", ByteOrder.BIG_ENDIAN ),
    INTEL_ORDER( "Intel Order", ByteOrder.LITTLE_ENDIAN ),
    NETWORK_ORDER( "Network Order", ByteOrder.BIG_ENDIAN );

    public final String name;
    public final ByteOrder order;

    /***************************************************************************
     * @param name
     * @param order
     **************************************************************************/
    private ByteOrdering( String name, ByteOrder order )
    {
        this.name = name;
        this.order = order;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }
}
