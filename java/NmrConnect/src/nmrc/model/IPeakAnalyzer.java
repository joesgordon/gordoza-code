package nmrc.model;

import java.util.List;

/***************************************************************************
 * 
 **************************************************************************/
public interface IPeakAnalyzer
{
    /***************************************************************************
     * Returns the analyzed list of peaks.
     * @return
     **************************************************************************/
    public List<IPeak> analyze( List<IPeak> peaks );
}