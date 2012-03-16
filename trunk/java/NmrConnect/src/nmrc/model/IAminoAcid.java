package nmrc.model;

public interface IAminoAcid
{
    public IPeak getPeak();

    public void setPeak( IPeak peak );

    public IShiftxRecord getShiftX();
}
