package org.cc.cvs.cmd;

import java.io.*;

public abstract class AbstractCommand
{
    public abstract String getDescription();

    public final void runCommand( IStatusUpdater updater ) throws IOException
    {
        String cmd = getCommand();
        Process cvsProcess = Runtime.getRuntime().exec( cmd );

        InputStream in = cvsProcess.getErrorStream();
        parseStream( in, updater );

        in = cvsProcess.getInputStream();
        parseStream( in, updater );

        try
        {
            int returnValue = cvsProcess.waitFor();
            if( returnValue != 0 )
            {
                throw new IOException( "Command returned " + returnValue +
                    ": " + cmd );
            }
        }
        catch( InterruptedException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void parseStream( InputStream in, IStatusUpdater updater )
        throws IOException
    {
        BufferedReader bin = new BufferedReader( new InputStreamReader( in ) );

        String line;
        while( ( line = bin.readLine() ) != null )
        {
            parseLine( line, updater );
        }
    }

    protected abstract String parseLine( String line, IStatusUpdater updater );

    protected abstract String getCommand();
}
