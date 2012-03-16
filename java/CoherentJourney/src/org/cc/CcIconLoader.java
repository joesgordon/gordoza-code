package org.cc;

import org.jutils.IconLoader;

public class CcIconLoader extends IconLoader
{
    public static final String PRINT_16 = "printer16.png";
    public static final String HELP_16 = "help16.png";
    public static final String SWITCH_16 = "switch16.png";

    private static CcIconLoader loader;

    private CcIconLoader()
    {
        super( CcIconLoader.class, "icons" );
    }

    public static CcIconLoader getloader()
    {
        if( loader == null )
        {
            loader = new CcIconLoader();
        }
        return loader;
    }
}
