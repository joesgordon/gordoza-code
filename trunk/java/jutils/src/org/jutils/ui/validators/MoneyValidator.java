package org.jutils.ui.validators;

import org.jutils.ui.validation.ValidationException;

/*******************************************************************************
 * A validator that ensures that integers fall within a specified range.
 ******************************************************************************/
public class MoneyValidator implements IDataValidator<Integer>
{
    /** The minimum bound, inclusive; not checked if {@code null}. */
    private final Integer min;
    /** The maximum bound, inclusive; not checked if {@code null}. */
    private final Integer max;

    /***************************************************************************
     * Creates a validator that ensures the text is an integer and performs no
     * bounds checking.
     **************************************************************************/
    public MoneyValidator()
    {
        this( null, null );
    }

    /***************************************************************************
     * Creates a validator that ensures the text is an integer and performs the
     * specified bounds checking.
     * @param min The minimum bound, inclusive; not checked if {@code null}.
     * @param max The maximum bound, inclusive; not checked if {@code null}.
     **************************************************************************/
    public MoneyValidator( Integer min, Integer max )
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
        if( text.isEmpty() )
        {
            throw new ValidationException( "Cannot parse empty string" );
        }

        try
        {
            if( text.charAt( 0 ) == '$' )
            {
                text = text.substring( 1 );
            }

            int idx = text.indexOf( '.' );

            String dStr = text.substring( 0, idx );
            String pStr = text.substring( idx, text.length() );

            int dollars = Integer.parseInt( dStr );
            int pennies = Integer.parseInt( pStr );

            int amount = dollars * 100 + pennies;

            if( min != null && amount < min )
            {
                throw new ValidationException( "Value less than minimum: " +
                    amount + " < " + min );
            }

            if( max != null && amount > max )
            {
                throw new ValidationException( "Value greater than maximum: " +
                    amount + " > " + max );
            }

            return amount;
        }
        catch( NumberFormatException ex )
        {
            throw new ValidationException( ex.getMessage() );
        }
    }
}
