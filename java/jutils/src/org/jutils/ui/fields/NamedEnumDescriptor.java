package org.jutils.ui.fields;

import org.jutils.INamedEnum;
import org.jutils.ui.fields.ValidationComboField.IDescriptor;

public class NamedEnumDescriptor<T extends INamedEnum> implements IDescriptor<T>
{
    @Override
    public String getDescription( T item )
    {
        return item.getName();
    }
}
