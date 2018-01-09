package org.mc;

import java.io.File;

import org.jutils.io.IOUtils;
import org.jutils.io.LogUtils;
import org.jutils.io.options.IOptionsCreator;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.app.FrameRunner;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McMain
{
    /**  */
    private static final File OPTIONS_FILE = IOUtils.getUsersFile( ".jutils",
        "mccomm", "options.xml" );

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        FrameRunner.invokeLater( new McApp(), false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<McOptions> getUserData()
    {
        return OptionsSerializer.getOptions( new McCommOptionsCreator(),
            OPTIONS_FILE );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class McCommOptionsCreator
        implements IOptionsCreator<McOptions>
    {

        @Override
        public McOptions createDefaultOptions()
        {
            return new McOptions();
        }

        @Override
        public McOptions initialize( McOptions options )
        {
            return new McOptions( options );
        }

        @Override
        public void warn( String message )
        {
            LogUtils.printWarning( message );
        }
    }
}
