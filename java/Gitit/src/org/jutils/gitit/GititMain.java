package org.jutils.gitit;

import java.io.File;

import org.jutils.gitit.data.GititOptions;
import org.jutils.io.IOUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.app.FrameRunner;

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
            options = OptionsSerializer.getOptions( GititOptions.class,
                OPTIONS_FILE );
        }

        return options;
    }
}
