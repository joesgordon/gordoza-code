package org.jutils.ui.fields;

import org.jutils.INamedItem;

/*******************************************************************************
 * Defines an {@link IDescriptor} for {@link INamedItem}s.
 ******************************************************************************/
public class NamedItemDescriptor<T extends INamedItem> implements IDescriptor<T>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getDescription( T item )
    {
        return item == null ? "" : item.getName();
    }
}
