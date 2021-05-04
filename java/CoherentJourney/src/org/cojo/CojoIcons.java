package org.cojo;

import org.jutils.core.io.IconLoader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CojoIcons extends IconLoader
{
    /**  */
    public static final String PRINT_16 = "printer16.png";
    /**  */
    public static final String HELP_16 = "help16.png";
    /**  */
    public static final String SWITCH_16 = "switch16.png";

    /**  */
    private static CojoIcons loader;

    /***************************************************************************
     * 
     **************************************************************************/
    private CojoIcons()
    {
        super( CojoIcons.class, "icons" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static CojoIcons getloader()
    {
        if( loader == null )
        {
            loader = new CojoIcons();
        }
        return loader;
    }
}
