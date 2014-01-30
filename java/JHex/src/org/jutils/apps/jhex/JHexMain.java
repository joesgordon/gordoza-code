package org.jutils.apps.jhex;

import java.io.File;

import org.jutils.io.*;
import org.jutils.io.UserOptionsSerializer.IUserOptionsCreator;
import org.jutils.ui.app.FrameApplication;

//TODO comments

/*******************************************************************************
 * 
 ******************************************************************************/
public class JHexMain
{
    /**  */
    private static final File USER_OPTIONS_DIR = new File( IOUtils.USERS_DIR,
        ".jhex" );
    /**  */
    private static final File USER_OPTIONS_FILE = new File( USER_OPTIONS_DIR,
        "options.txt" );

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
                System.err.println( "File does not exist: " +
                    file.getAbsolutePath() );
                System.exit( -1 );
            }
        }

        JHexApp app = new JHexApp( getUserIO(), file );
        FrameApplication.invokeLater( app );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static UserOptionsSerializer<JHexOptions> getUserIO()
    {
        return UserOptionsSerializer.getUserIO( new JHexOptionsDataCreator(),
            USER_OPTIONS_FILE );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class JHexOptionsDataCreator implements
        IUserOptionsCreator<JHexOptions>
    {
        @Override
        public JHexOptions createDefaultOptions()
        {
            JHexOptions options = new JHexOptions();
            return options;
        }
    }
}
