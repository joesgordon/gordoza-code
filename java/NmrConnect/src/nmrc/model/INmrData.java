package nmrc.model;

import java.util.List;

public interface INmrData
{
    public List<IPeak> getPeaks();

    public List<IShiftxRecord> getShiftX();

    public List<IAminoAcid> getAminoAcids();
}
