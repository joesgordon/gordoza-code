package testbed.propEdit;

import java.util.Map.Entry;
import java.util.Set;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

public class UiPropertiesTest
{
    public static void main( String[] args )
    {
        UIDefaults defaults = UIManager.getDefaults();

        Set<Entry<Object, Object>> entries = defaults.entrySet();

        for( Entry<Object, Object> entry : entries )
        {
            Object key = entry.getKey();
            Object val = entry.getValue();

            // if( key.toString().toUpperCase().startsWith( "CONTROL" ) )
            if( key.toString().toUpperCase().contains( "COMBO" ) )
            {
                printObj( key, "Key" );
                printObj( val, "Val" );

                System.out.println( "---------------" );
            }
        }
    }

    private static void printObj( Object obj, String label )
    {
        System.out.print( label + ": " );

        if( obj != null )
        {
            System.out.print( obj.toString() );
            System.out.println( "(" + obj.getClass().toString() + ")" );
        }
        else
        {
            System.out.println( "null" );
        }
    }
}
