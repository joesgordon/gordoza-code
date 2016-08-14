package org.jutils.time;

import org.jutils.ValidationException;
import org.jutils.ui.validators.IDataValidator;
import org.jutils.ui.validators.LongValidator;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MicrosecondsValidator implements IDataValidator<Long>
{
    /**  */
    private final LongValidator lv;

    /***************************************************************************
     * 
     **************************************************************************/
    public MicrosecondsValidator()
    {
        this( null, null );
    }

    /***************************************************************************
     * @param min
     * @param max
     **************************************************************************/
    public MicrosecondsValidator( Long min, Long max )
    {
        this.lv = new LongValidator( min, max );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Long validate( String text ) throws ValidationException
    {
        int dot = text.indexOf( '.' );
        String seconds = text;

        if( text.isEmpty() )
        {
            throw new ValidationException( "Empty string" );
        }

        if( dot > -1 )
        {
            seconds = text.substring( 0, dot );
            String fraction = text.substring( dot + 1 );

            if( fraction.length() > 6 )
            {
                throw new ValidationException(
                    "Too many fractional digits. Expected <= 6, found " +
                        fraction.length() );
            }

            for( int i = fraction.length(); i < 6; i++ )
            {
                fraction += "0";
            }
            seconds += fraction;
        }
        else
        {
            seconds += "000000";
        }

        long micros = lv.validate( seconds );

        // LogUtils.printDebug( "parsed Long: " + micros );

        return micros;
    }
}
