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
    private static OptionsSerializer<DuakOptions> userio;

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
        if( userio == null )
        {
            userio = OptionsSerializer.getUserIO( DuakOptions.class,
                OPTIONS_FILE );
        }

        return userio;
    }
}
