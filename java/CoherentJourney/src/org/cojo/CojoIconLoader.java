package org.cojo;

import org.jutils.io.IconLoader;

public class CojoIconLoader extends IconLoader
{
    public static final String PRINT_16 = "printer16.png";
    public static final String HELP_16 = "help16.png";
    public static final String SWITCH_16 = "switch16.png";

    private static CojoIconLoader loader;

    private CojoIconLoader()
    {
        super( CojoIconLoader.class, "icons" );
    }

    public static CojoIconLoader getloader()
    {
        if( loader == null )
        {
            loader = new CojoIconLoader();
        }
        return loader;
    }
}
