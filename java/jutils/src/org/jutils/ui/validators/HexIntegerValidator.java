package org.jutils.ui.validators;

import org.jutils.NumberParsingUtils;
import org.jutils.ValidationException;

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
                    Integer.toHexString( i ).toUpperCase() + " < " +
                    Integer.toHexString( min ).toUpperCase() );
            }

            if( max != null && i > max )
            {
                throw new ValidationException( "Value greater than maximum: " +
                    Integer.toHexString( i ).toUpperCase() + " > " +
                    Integer.toHexString( max ).toUpperCase() );
            }

            return i;
        }
        catch( NumberFormatException ex )
        {
            throw new ValidationException( ex.getMessage() );
        }
    }
}
