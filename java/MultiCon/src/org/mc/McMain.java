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
        "multicon", "options.xml" );

    /**  */
    private static OptionsSerializer<McOptions> userOptions;

    /***************************************************************************
     * @param args ignored
     **************************************************************************/
    public static void main( String[] args )
    {
        FrameRunner.invokeLater( new McApp(), false );
    }

    /***************************************************************************
     * @return the user options for this application/library instance.
     **************************************************************************/
    public static OptionsSerializer<McOptions> getUserData()
    {
        if( userOptions == null )
        {
            userOptions = OptionsSerializer.getOptions(
                new McCommOptionsCreator(), OPTIONS_FILE );
        }

        return userOptions;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class McCommOptionsCreator
        implements IOptionsCreator<McOptions>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public McOptions createDefaultOptions()
        {
            return new McOptions();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public McOptions initialize( McOptions options )
        {
            return new McOptions( options );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void warn( String message )
        {
            LogUtils.printWarning( message );
        }
    }
}
