package org.cc.cvs.cmd;

import java.io.*;

// TODO Make into an interface and move written functions into a class

public abstract class AbstractCommand
{
    public final int runCommand( IStatusUpdater updater ) throws IOException
    {
        String cmd = getCommand();
        int returnValue = -1;
        Process cvsProcess = Runtime.getRuntime().exec( cmd );

        parseStreams( cvsProcess, updater );

        try
        {
            cvsProcess.waitFor();
        }
        catch( InterruptedException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return returnValue;
    }

    private void parseStreams( Process proc, IStatusUpdater updater )
        throws IOException
    {
        parseStream( proc.getErrorStream(), updater );

        parseStream( proc.getInputStream(), updater );
    }

    private void parseStream( InputStream in, IStatusUpdater updater )
        throws IOException
    {
        try
        {
            BufferedReader bin = new BufferedReader( new InputStreamReader( in ) );

            String line;
            while( ( line = bin.readLine() ) != null )
            {
                parseLine( line, updater );
            }
        }
        finally
        {
            in.close();
        }
    }

    public abstract String getDescription();

    protected abstract String parseLine( String line, IStatusUpdater updater );

    protected abstract String getCommand();
}
