package org.cojo.data;

import org.jutils.core.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum TaskPriority implements INamedItem
{
    /**  */
    CRITICAL( "Critical", 1 ),
    /**  */
    URGENT( "Urgent", 2 ),
    /**  */
    IMPORTANT( "Important", 3 ),
    /**  */
    SECONDARY( "Secondary", 4 ),
    /**  */
    DEFERRED( "Deferred", 5 );

    /**  */
    public final String name;
    /**  */
    public final int value;

    /**
     * @param name
     * @param value
     */
    private TaskPriority( String name, int value )
    {
        this.name = name + " (" + value + ")";
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
