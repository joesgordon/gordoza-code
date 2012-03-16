package nmrc.model;

import java.util.List;

import nmrc.data.PeakMatch;

public interface IPeakChooser
{
    public PeakMatch choosePeak( IPeak peak, List<PeakMatch> peaks );

    public boolean isCancelled();
}
