package org.jutils.ui.validators;

import org.jutils.NumberParsingUtils;
import org.jutils.ui.hex.HexUtils;
import org.jutils.ui.validation.ValidationException;

/*******************************************************************************
 * A validator that ensures that the provided text represents a hexadecimal
 * integer.
 ******************************************************************************/
public class HexIntegerValidator implements IDataValidator<Integer>
{
    /** The minimum bound, inclusive; not checked if {@code null}. */
    private final Integer min;
    /** The maximum bound, inclusive; not checked if {@code null}. */
    private final Integer max;

    public HexIntegerValidator()
    {
        this( null, null );
    }

    public HexIntegerValidator( Integer min, Integer max )
    {
        this.min = min;
        this.max = max;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Integer validate( String text ) throws ValidationException
    {
        try
        {
            int i = NumberParsingUtils.parseHexInteger( text );

            if( min != null && i < min )
            {
                throw new ValidationException( "Value less than minimum: " +
                    HexUtils.toHexString( i ) + " < " +
                    HexUtils.toHexString( min ) );
            }

            if( max != null && i > max )
            {
                throw new ValidationException( "Value greater than maximum: " +
                    HexUtils.toHexString( i ) + " > " +
                    HexUtils.toHexString( max ) );
            }

            return i;
        }
        catch( NumberFormatException ex )
        {
            throw new ValidationException( ex.getMessage() );
        }
    }
}
