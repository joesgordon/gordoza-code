package nmrc.model;

import java.util.List;

public interface IPeakFile
{
    public String[] getHeadings();

    public List<IPeakRecord> getRecords();

    public List<IPeak> getPeaks();
}
