package testbed.propEdit;

import java.awt.Color;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.*;

import org.jutils.io.LogUtils;
import org.jutils.ui.app.AppRunnable;
import org.jutils.ui.app.IApplication;

public class PropEditMain
{
    public static void main( String[] args )
    {
        IApplication app = new IApplication()
        {
            @Override
            public String getLookAndFeelName()
            {
                return null;
            }

            @Override
            public void createAndShowUi()
            {
                findInDefaults();
                testUIDefaults();
            }
        };

        AppRunnable appr = new AppRunnable( app );
        SwingUtilities.invokeLater( appr );
    }

    private static void findInDefaults()
    {
        List<String> keys = new ArrayList<String>();

        UIDefaults defaults = UIManager.getDefaults();

        for( Entry<Object, Object> entry : defaults.entrySet() )
        {
            Object key = entry.getKey();
            Object value = defaults.get( key );

            if( value != null &&
                Color.class.isAssignableFrom( value.getClass() ) )
            {
                Color c = ( Color )value;
                int rgb = c.getRGB() & 0xFFFFFF;

                if( rgb == 0x0A246A )
                {
                    keys.add( key.toString() );
                }
            }
        }

        if( !keys.isEmpty() )
        {
            System.out.println( "c = new Color( 0x0A315A );" );
            System.out.println();

            Collections.sort( keys );

            for( String key : keys )
            {
                System.out.println( "UIManager.put( \"" + key + "\", c );" );
            }
        }
    }

    private static void testUIDefaults()
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
                LogUtils.printError( "[" + key + "] = " + valueString +
                    " instead of " + entryValueString );
            }
        }
    }
}
