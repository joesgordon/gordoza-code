package org.jutils.ui.validators;

import org.jutils.ValidationException;
import org.jutils.ui.hex.HexUtils;

/*******************************************************************************
 * A validator that ensures that integers fall within a specified range.
 ******************************************************************************/
public class HexByteValidator implements IDataValidator<Byte>
{
    /** The minimum bound, inclusive; not checked if {@code null}. */
    private final Byte min;
    /** The maximum bound, inclusive; not checked if {@code null}. */
    private final Byte max;

    /***************************************************************************
     * Creates a validator that ensures the text is an integer and performs no
     * bounds checking.
     **************************************************************************/
    public HexByteValidator()
    {
        this( null, null );
    }

    /***************************************************************************
     * Creates a validator that ensures the text is an integer and performs the
     * specified bounds checking.
     * @param min The minimum bound, inclusive; not checked if {@code null}.
     * @param max The maximum bound, inclusive; not checked if {@code null}.
     **************************************************************************/
    public HexByteValidator( Byte min, Byte max )
    {
        this.min = min;
        this.max = max;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Byte validate( String text ) throws ValidationException
    {
        try
        {
            byte i = HexUtils.parseHex( text );

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
