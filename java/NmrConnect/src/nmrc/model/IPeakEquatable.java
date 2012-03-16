package nmrc.model;

public interface IPeakEquatable extends IMatcher<IPeak>
{
    public IPeakMatch getMatchCriteria();

    public boolean matches( IPeak thatPeak );
}
