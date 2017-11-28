package org.jutils.apps.jhex;

import java.io.File;

import org.jutils.apps.jhex.data.JHexOptions;
import org.jutils.io.IOUtils;
import org.jutils.io.options.IOptionsCreator;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.app.FrameRunner;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JHexMain
{
    /**  */
    public static final File USER_OPTIONS_FILE = IOUtils.getUsersFile(
        ".jutils", "jhex", "options.xml" );

    /**  */
    private static OptionsSerializer<JHexOptions> OPTIONS;

    /***************************************************************************
     * @param args Program arguments.
     **************************************************************************/
    public static void main( String [] args )
    {
        File file = null;

        if( args.length > 0 )
        {
            file = new File( args[0] );
            if( !file.isFile() )
            {
                System.err.println(
                    "File does not exist: " + file.getAbsolutePath() );
                System.exit( -1 );
            }
        }

        JHexApp hexApp = new JHexApp( file );
        FrameRunner.invokeLater( hexApp );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<JHexOptions> getOptions()
    {
        if( OPTIONS == null )
        {
            OPTIONS = OptionsSerializer.getOptions( new JHexOptionsCreator(),
                USER_OPTIONS_FILE );
        }
        return OPTIONS;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class JHexOptionsCreator
        implements IOptionsCreator<JHexOptions>
    {

        @Override
        public JHexOptions createDefaultOptions()
        {
            return new JHexOptions();
        }

        @Override
        public JHexOptions initialize( JHexOptions options )
        {
            options = new JHexOptions( options );

            options.removeNonExistentRecents();

            return options;
        }

        @Override
        public void warn( String message )
        {
            // TODO Auto-generated method stub

        }
    }
}
