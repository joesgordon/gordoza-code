package org.jutils.ui.validators;

import org.jutils.ValidationException;

/*******************************************************************************
 * A validator that ensures that integers fall within a specified range.
 ******************************************************************************/
public class IntegerValidator implements IDataValidator<Integer>
{
    /** The minimum bound, inclusive; not checked if {@code null}. */
    private final Integer min;
    /** The maximum bound, inclusive; not checked if {@code null}. */
    private final Integer max;

    /***************************************************************************
     * Creates a validator that ensures the text is an integer and performs no
     * bounds checking.
     **************************************************************************/
    public IntegerValidator()
    {
        this( null, null );
    }

    /***************************************************************************
     * Creates a validator that ensures the text is an integer and performs the
     * specified bounds checking.
     * @param min The minimum bound, inclusive; not checked if {@code null}.
     * @param max The maximum bound, inclusive; not checked if {@code null}.
     **************************************************************************/
    public IntegerValidator( Integer min, Integer max )
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
            int i = Integer.parseInt( text );

            if( min != null && i < min )
            {
                throw new ValidationException(
                    "Value less than minimum: " + i + " < " + min );
            }

            if( max != null && i > max )
            {
                throw new ValidationException(
                    "Value greater than maximum: " + i + " > " + max );
            }

            return i;
        }
        catch( NumberFormatException ex )
        {
            throw new ValidationException( ex.getMessage() );
        }
    }
}
