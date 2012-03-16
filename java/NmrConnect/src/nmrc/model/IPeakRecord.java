package nmrc.model;

import java.util.List;

public interface IPeakRecord
{
    public int getIndex();

    public String getPeakName();

    public double getHn();

    public double getN15();

    public List<Double> getCarbonAlpha();

    public List<Double> getCarbonBeta();

    public List<Double> getPreviousCarbonAlpha();

    public List<Double> getPreviousCarbonBeta();

    public List<Double> getAlternateCarbonBeta();

    public boolean hasPrevious();

    public void addDuplicate( IPeakRecord record );

    public List<IPeakRecord> getDuplicates();

    public void removeDuplicate( IPeakRecord record );
}
