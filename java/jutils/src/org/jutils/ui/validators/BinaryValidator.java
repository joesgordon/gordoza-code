package org.jutils.ui.validators;

import org.jutils.ui.validation.ValidationException;
import org.jutils.utils.BitArray;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BinaryValidator implements IDataValidator<BitArray>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public BitArray validate( String text ) throws ValidationException
    {
        BitArray bits = new BitArray();

        try
        {
            bits.set( text );
        }
        catch( NumberFormatException ex )
        {
            throw new ValidationException( ex.getMessage(), ex );
        }

        return bits;
    }
}
