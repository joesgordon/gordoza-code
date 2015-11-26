package org.jutils.ui.validators;

import org.jutils.ValidationException;
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

        if( text.isEmpty() )
        {
            throw new ValidationException( "The string is empty" );
        }

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
