package nmrc.model;

public interface IAssignmentOption
{
    public int getPeakStart();

    public void setPeakStart( int index );

    public int getPeakEnd();

    public int getAminoAcidStart();

    public void setAminoAcidStart( int index );

    public int getAminoAcidEnd();

    public int getLength();

    public void setLength( int length );
}
