package org.jutils.ui.fields;

public class DefaultItemDescriptor<T> implements IDescriptor<T>
{
    @Override
    public String getDescription( T item )
    {
        return item == null ? "" : item.toString();
    }
}
