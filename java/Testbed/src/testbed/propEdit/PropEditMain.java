package testbed.propEdit;

import java.awt.Color;
import java.util.Map.Entry;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

public class PropEditMain
{
    public static void main( String[] args )
    {
        Color stadBg = new Color( 0x808080 );
        UIDefaults defaults = UIManager.getDefaults();
        String propStr = "Panel.background";

        UIManager.put( propStr, stadBg );

        for( Entry<Object, Object> entry : defaults.entrySet() )
        {
            Object key = entry.getKey();
            Object value = defaults.get( key );
            Object entryValue = entry.getValue();

            String valueString = null;
            String entryValueString = null;

            if( value == null && entryValue != null )
            {
                valueString = "null";
                entryValueString = entryValue.toString();
            }
            else if( value != null && entryValue == null )
            {
                valueString = value.toString();
                entryValueString = "null";
            }
            else if( value != null && entryValue != null &&
                !entryValue.equals( value ) )
            {
                valueString = value.toString();
                entryValueString = entryValue.toString();
            }

            if( valueString != null )
            {
                System.out.println( "ERROR: [" + key + "] = " + valueString +
                    " instead of " + entryValueString );
            }
        }
    }
}
