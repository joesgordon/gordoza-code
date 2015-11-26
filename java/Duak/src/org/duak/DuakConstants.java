package org.duak;

import java.io.File;

import org.jutils.io.IOUtils;
import org.jutils.io.OptionsSerializer;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class DuakConstants
{
    /**  */
    private final static File OPTIONS_FILE = IOUtils.getUsersFile( ".jutils",
        "duak", "options.xml" );

    /**  */
    private static OptionsSerializer<DuakOptions> options;

    /***************************************************************************
     * 
     **************************************************************************/
    private DuakConstants()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<DuakOptions> getOptions()
    {
        if( options == null )
        {
            options = OptionsSerializer.getOptions( DuakOptions.class,
                OPTIONS_FILE );
        }

        return options;
    }
}
