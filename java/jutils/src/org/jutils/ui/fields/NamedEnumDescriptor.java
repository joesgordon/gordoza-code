package org.jutils.ui.fields;

import org.jutils.INamedEnum;

/*******************************************************************************
 * Defines an {@link IDescriptor} for {@link INamedEnum}s.
 ******************************************************************************/
public class NamedEnumDescriptor<T extends INamedEnum> implements IDescriptor<T>
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
