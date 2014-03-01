package org.jutils.apps.jhex;

import java.io.File;

import org.jutils.io.IOUtils;
import org.jutils.io.UserOptionsSerializer;
import org.jutils.ui.app.FrameApplication;

//TODO comments

/*******************************************************************************
 * 
 ******************************************************************************/
public class JHexMain
{
    /**  */
    public static final File USER_OPTIONS_FILE = IOUtils.getUsersFile(
        ".jutils", "jhex", "options.xml" );

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

        JHexApp hexApp = new JHexApp( getUserIO(), file );
        FrameApplication.invokeLater( hexApp );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static UserOptionsSerializer<JHexOptions> getUserIO()
    {
        return UserOptionsSerializer.getUserIO( JHexOptions.class,
            USER_OPTIONS_FILE );
    }
}
