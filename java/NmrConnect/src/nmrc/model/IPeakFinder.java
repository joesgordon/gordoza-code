package nmrc.model;

import java.util.List;

public interface IPeakFinder
{
    public List<IPeak> findPeaks( IPeak peak, List<IPeak> peaks );
}
