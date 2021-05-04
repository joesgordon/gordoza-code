package org.jutils.gitit;

import java.io.File;

import org.jutils.core.io.IOUtils;
import org.jutils.core.io.options.OptionsSerializer;
import org.jutils.core.io.xs.XsOptions;
import org.jutils.core.ui.app.FrameRunner;
import org.jutils.gitit.data.GititOptions;

/*******************************************************************************
 *
 ******************************************************************************/
public class GititMain
{
    /**  */
    private static final File OPTIONS_FILE = IOUtils.getUsersFile( ".jutils",
        "gitit", "options.xml" );

    /**  */
    private static OptionsSerializer<GititOptions> options = null;

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new GititApp(), false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<GititOptions> getUserOptions()
    {
        if( options == null )
        {
            options = XsOptions.getOptions( GititOptions.class, OPTIONS_FILE );
        }

        return options;
    }
}
