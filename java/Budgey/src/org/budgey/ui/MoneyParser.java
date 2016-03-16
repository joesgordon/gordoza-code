package org.budgey.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.budgey.data.Money;
import org.jutils.ValidationException;
import org.jutils.io.IParser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MoneyParser implements IParser<Money>
{
    /**  */
    private static final Pattern p;

    static
    {
        p = Pattern.compile( "\\$?(\\d+)\\.?(\\d{0,2})" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Money parse( String text ) throws ValidationException
    {
        Money m = null;
        long cents = 0;

        Matcher matcher = p.matcher( text );
        if( matcher.matches() )
        {
            String str;
            long num;

            str = matcher.group( 1 );
            if( str != null && str.length() > 0 )
            {
                num = Long.parseLong( str );
                cents += num * 100;
            }

            str = matcher.group( 2 );
            if( str != null && str.length() > 0 )
            {
                num = Long.parseLong( str );
                if( str.length() < 2 )
                {
                    num *= 10;
                }
                cents += num;
            }

            m = new Money( cents );
        }
        else
        {
            throw new ValidationException( "Not a money value: " + text );
        }

        return m;
    }

}
