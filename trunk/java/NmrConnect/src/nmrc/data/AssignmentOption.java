package nmrc.data;

import nmrc.model.IAssignmentOption;

public class AssignmentOption implements IAssignmentOption
{
    private int peakIndex;
    private int aaIndex;
    private int length;

    public AssignmentOption()
    {
        this( 0, 0, 0 );
    }

    public AssignmentOption( int pIndex, int aIndex, int len )
    {
        peakIndex = pIndex;
        aaIndex = aIndex;
        length = len;
    }

    @Override
    public int getAminoAcidEnd()
    {
        return aaIndex + length;
    }

    @Override
    public int getPeakEnd()
    {
        return peakIndex + length;
    }

    @Override
    public int getAminoAcidStart()
    {
        return aaIndex;
    }

    @Override
    public int getLength()
    {
        return length;
    }

    @Override
    public int getPeakStart()
    {
        return peakIndex;
    }

    @Override
    public void setAminoAcidStart( int index )
    {
        aaIndex = index;
    }

    @Override
    public void setLength( int length )
    {
        this.length = length;
    }

    @Override
    public void setPeakStart( int index )
    {
        peakIndex = index;
    }
}
