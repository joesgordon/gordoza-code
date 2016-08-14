package org.jutils.apps.summer;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.jutils.ValidationException;
import org.jutils.apps.summer.data.ChecksumResult;
import org.jutils.apps.summer.io.ChecksumFileSerializer;
import org.jutils.apps.summer.ui.CreateView;
import org.jutils.io.IOUtils;
import org.jutils.io.LogUtils;
import org.jutils.ui.app.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SummerMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        if( args.length == 0 )
        {
            IFrameApp app = new SummerApp();
            FrameApplication.invokeLater( app );
        }
        else
        {
            List<String> argList = new ArrayList<>( Arrays.asList( args ) );

            try
            {
                runMain( argList );
            }
            catch( ValidationException ex )
            {
                LogUtils.printError( ex.getMessage() );
            }
        }
    }

    public static int runMain( List<String> args ) throws ValidationException
    {
        if( args.isEmpty() )
        {
            throw new ValidationException(
                "Incorrect # of arguments for create command; Expected 3-5, found 0" );
        }

        String cmd = args.remove( 0 ).toLowerCase();

        if( cmd.equals( "create" ) )
        {
            return runCreate( args );
        }
        else if( cmd.equals( "verify" ) )
        {
            // TODO verify file.
            ;
        }
        else
        {
            throw new ValidationException(
                "Unknown summer command \"" + cmd + "\"" );
        }

        return 0;
    }

    /***************************************************************************
     * @param args
     * @return
     * @throws ValidationException
     **************************************************************************/
    private static int runCreate( List<String> args ) throws ValidationException
    {
        int argCount = 0;

        for( String arg : args )
        {
            if( !arg.startsWith( "-" ) )
            {
                argCount++;
            }
        }

        if( argCount < 3 )
        {
            throw new ValidationException(
                "Incorrect # of arguments for create command; Expected 3, found " +
                    argCount );
        }
        else
        {
            boolean append = false;
            boolean replace = false;
            File baseDir;
            File outputFile;
            List<File> relPaths = new ArrayList<>();

            for( int i = 0; i < 2; i++ )
            {
                if( args.get( 0 ).startsWith( "-" ) )
                {
                    String arg = args.remove( 0 );
                    if( arg.toLowerCase().equals( "-a" ) )
                    {
                        append = true;
                    }
                    else if( arg.toLowerCase().equals( "-r" ) )
                    {
                        replace = true;
                    }
                }
            }

            baseDir = new File( args.remove( 0 ) );
            outputFile = new File( args.remove( 0 ) );

            for( String path : args )
            {
                File relFile = new File( baseDir, path );
                relPaths.add( relFile );
            }

            IOUtils.validateDirInput( baseDir, "Base Directory" );
            IOUtils.validateFileOuput( outputFile, "Checksum" );

            for( File relFile : relPaths )
            {
                if( !relFile.exists() )
                {
                    throw new ValidationException(
                        "Relative Path does not exist: " +
                            relFile.getAbsolutePath() );
                }
            }

            ChecksumResult input = createInputs( baseDir, relPaths );

            ChecksumCreator sumCreator = new ChecksumCreator( input );

            AppRunner.invokeAndWait( sumCreator );

            if( !sumCreator.isSuccessful() )
            {
                return -2;
            }

            ChecksumFileSerializer cfs = new ChecksumFileSerializer();

            try
            {
                cfs.write( input, outputFile, append, replace );
            }
            catch( IOException ex )
            {
                ex.printStackTrace();
                return -1;
            }
        }

        return 0;
    }

    /***************************************************************************
     * @param baseDir
     * @param relFile
     * @param file
     * @return
     **************************************************************************/
    private static ChecksumResult createInputs( File baseDir,
        List<File> relPaths )
    {
        ChecksumResult sums = new ChecksumResult();
        List<File> paths = new ArrayList<>();

        for( File relFile : relPaths )
        {
            if( relFile.isFile() )
            {
                paths.add( relFile );
            }
            else
            {
                paths.addAll( IOUtils.getAllFiles( relFile ) );
            }
        }

        sums.setFiles( paths );
        sums.setPaths( baseDir );

        return sums;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ChecksumCreator implements IApplication
    {
        private final ChecksumResult input;

        private boolean succeeded = false;

        public ChecksumCreator( ChecksumResult input )
        {
            this.input = input;
        }

        @Override
        public String getLookAndFeelName()
        {
            return null;
        }

        @Override
        public void createAndShowUi()
        {
            succeeded = false;
            succeeded = CreateView.runCreate( null, input ) != null;
        }

        public boolean isSuccessful()
        {
            return succeeded;
        }
    }
}
