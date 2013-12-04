package org.jutils.apps.jhex;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jutils.io.*;
import org.jutils.io.UserOptionsSerializer.IUserOptionsCreator;
import org.jutils.ui.FrameRunner;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JHexMain extends FrameRunner
{
    /**  */
    private static final File USER_OPTIONS_DIR = new File( IOUtils.USERS_DIR,
        ".jhex" );
    /**  */
    private static final File USER_OPTIONS_FILE = new File( USER_OPTIONS_DIR,
        "options.txt" );

    private final File file;
    private final UserOptionsSerializer<JHexOptions> userDataIO;

    /***************************************************************************
     * 
     **************************************************************************/
    public JHexMain()
    {
        this( null );
    }

    /***************************************************************************
     * @param file
     * @param userIO
     **************************************************************************/
    public JHexMain( File file )
    {
        this.file = file;
        this.userDataIO = getUserIO();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JFrame createFrame()
    {
        JHexFrame view = new JHexFrame( userDataIO );
        JFrame frame = view.getView();

        frame.setSize( new Dimension( 800, 600 ) );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        if( file != null )
        {
            view.openFile( file );
        }

        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected boolean validate()
    {
        return true;
    }

    /***************************************************************************
     * @param args Program arguments.
     **************************************************************************/
    public static void main( String[] args )
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

        SwingUtilities.invokeLater( new JHexMain( file ) );
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
