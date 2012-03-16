package nmrc.model;

import java.util.List;

public interface IPeakAssignerQuery
{
    public void displayError( String message );

    public IAssignmentOption confirmOption( List<IAminoAcid> aAcids,
        List<IPeak> chain, IAssignmentOption option );

    public IAssignmentOption chooseOption( List<IAminoAcid> aAcids,
        List<IPeak> chain, List<IAssignmentOption> options );
}
