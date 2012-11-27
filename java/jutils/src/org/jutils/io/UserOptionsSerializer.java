package org.jutils.io;

import java.io.*;

import com.thoughtworks.xstream.XStreamException;

// TODO add a way for the errors to become warnings.

/*******************************************************************************
 * Default serializer for user options.
 * @param <T> The type of options to be serialized.
 ******************************************************************************/
public class UserOptionsSerializer<T>
{
    /**
     * The class to use to create default options when the options cannot be
     * read from file.
     */
    private final UserOptionsCreator<T> creator;
    /** The file from which the options will be read and written. */
    private final File file;

    /** The last options read/written. */
    private T options;

    /***************************************************************************
     * Creates a serializer that will read/write options of type {@code T} to
     * the given file, using the creator when a default set of options are
     * required.
     * @param creator The class to use to create default options when the
     * options cannot be read from file.
     * @param file The file from which the options will be read and written.
     **************************************************************************/
    public UserOptionsSerializer( UserOptionsCreator<T> creator, File file )
    {
        this.creator = creator;
        this.file = file;
    }

    /***************************************************************************
     * Creates the directories for this file if necessary and returns
     * {@code true} if they exist as of the return of this function.
     **************************************************************************/
    public boolean ensureDirectoryExists()
    {
        File dir = file.getParentFile();

        if( !dir.isDirectory() )
        {
            return dir.mkdirs();
        }

        return true;
    }

    /***************************************************************************
     * Returns the last options read/written.
     **************************************************************************/
    public T getOptions()
    {
        if( options == null )
        {
            options = read();
        }

        return options;
    }

    /***************************************************************************
     * Reads the options from file, caching the options before returns them.
     **************************************************************************/
    public T read()
    {
        options = null;

        try
        {
            InputStream stream = null;

            try
            {
                stream = new FileInputStream( file );

                @SuppressWarnings( "unchecked")
                T t = ( T )XStreamUtils.readObjectXStream( stream );
                options = t;
            }
            finally
            {
                if( stream != null )
                {
                    stream.close();
                }
            }
        }
        catch( FileNotFoundException ex )
        {
            options = creator.createDefaultOptions();
            System.out.println( "WARNING: User options file does not exist: " +
                file.getAbsolutePath() );
        }
        catch( IOException ex )
        {
            options = creator.createDefaultOptions();
            ex.printStackTrace();
        }
        catch( XStreamException ex )
        {

            file.renameTo( new File( file.getAbsoluteFile().getParentFile(),
                file.getName() + ".broken" ) );
            options = creator.createDefaultOptions();
            System.out.println( "WARNING: User options file is out of date: " +
                file.getAbsolutePath() );
        }

        return options;
    }

    /***************************************************************************
     * Writes the last read/written options to file.
     **************************************************************************/
    public void write()
    {
        if( options != null )
        {
            write( options );
        }
    }

    /***************************************************************************
     * Caches the given options and writes to file.
     * @param data
     **************************************************************************/
    public void write( T data )
    {
        this.options = data;

        try
        {
            XStreamUtils.writeObjectXStream( data, file );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
        }
        catch( XStreamException ex )
        {
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     * Interface used to create a default set of options when an error results
     * during a read from file.
     * @param <T> The type of options to be created.
     **************************************************************************/
    public static interface UserOptionsCreator<T>
    {
        /** Creates a default set of options. */
        public T createDefaultOptions();
    }

}
