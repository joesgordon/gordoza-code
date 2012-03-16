package nmrc.data;

import java.util.ArrayList;
import java.util.List;

import nmrc.model.*;

public class PeakFile implements IPeakFile
{
    private String[] headings;

    private List<IPeakRecord> records;
    private List<IPeak> peaks;

    public PeakFile( String[] headings, List<IPeakRecord> records )
    {
        this.headings = headings;
        this.records = records;

        peaks = new ArrayList<IPeak>();
        for( IPeakRecord record : records )
        {
            peaks.add( new Peak( record ) );
        }
    }

    @Override
    public String[] getHeadings()
    {
        return headings;
    }

    @Override
    public List<IPeakRecord> getRecords()
    {
        return records;
    }

    @Override
    public List<IPeak> getPeaks()
    {
        return peaks;
    }
}
