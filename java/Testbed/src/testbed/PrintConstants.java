package testbed;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

import org.jutils.EnumerationIteratorAdapter;

public class PrintConstants
{
    public static void main( String[] args )
    {
        UIDefaults defaults = UIManager.getDefaults();

        for( Object key : new EnumerationIteratorAdapter<Object>(
            defaults.keys() ) )
        {

            if( key != null )
            {
                Object val = defaults.get( key );
                if( val != null )
                {
                    System.out.println( key + ": '" + val.toString() + "'" );
                }
            }
        }
    }
}
