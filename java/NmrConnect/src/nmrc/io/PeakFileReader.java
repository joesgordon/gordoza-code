package nmrc.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import nmrc.data.PeakFile;
import nmrc.model.IPeakFile;
import nmrc.model.IPeakRecord;

import org.jutils.io.IReader;

public class PeakFileReader implements IReader<IPeakFile>
{
    private PeakReader peakReader;
    private BufferedReader reader;

    public PeakFileReader( Reader reader )
    {
        this.reader = new BufferedReader( reader );
        peakReader = new PeakReader( this.reader );
    }

    @Override
    public Reader getStream()
    {
        return peakReader.getStream();
    }

    @Override
    public IPeakFile read() throws IOException
    {
        String line = reader.readLine();
        PeakFile peakFile = null;

        if( line != null )
        {
            String[] headings = line.split( "\\s*,\\s*" );
            IPeakRecord peak;
            List<IPeakRecord> peaks = new ArrayList<IPeakRecord>();

            int i = 0;
            for( String rec : headings )
            {
                headings[i] = rec.replace( "\"", "" );
                i++;
            }

            while( ( peak = peakReader.read() ) != null )
            {
                peaks.add( peak );
            }

            peakFile = new PeakFile( headings, peaks );
        }
        return peakFile;
    }

}
