package org.jutils.ui.validators;

import org.jutils.NumberParsingUtils;
import org.jutils.ui.validation.ValidationException;

/*******************************************************************************
 * A validator that ensures that the provided text represents a hexadecimal
 * integer.
 ******************************************************************************/
public class HexIntegerValidator implements IDataValidator<Integer>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Integer validate( String text ) throws ValidationException
    {
        try
        {
            return NumberParsingUtils.parseHexInteger( text );
        }
        catch( NumberFormatException ex )
        {
            throw new ValidationException( ex.getMessage() );
        }
    }
}
