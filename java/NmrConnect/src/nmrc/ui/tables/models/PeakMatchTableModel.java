package nmrc.ui.tables.models;

import java.util.Arrays;

import nmrc.data.DoubleMatch;
import nmrc.data.PeakMatch;

import org.jutils.ui.model.ItemTableModel;

/*******************************************************************************
 * @param <IPeakRecord>
 ******************************************************************************/
public class PeakMatchTableModel extends ItemTableModel<PeakMatch>
{
    /**  */
    private static final Class<?>[] COL_CLASSES = new Class<?>[] {
        String.class, Double.class, Double.class, Double.class, Double.class,
        Double.class, Double.class, Double.class };

    /**  */
    public static final String[] COL_NAMES = new String[] { "Peak Name",
        "Overall Diff", "Ca", "Ca[i-1]", "Ca diff", "Cb", "Cb[i-1]", "Cb diff" };

    /***************************************************************************
     * 
     **************************************************************************/
    public PeakMatchTableModel()
    {
        super();

        super.setColumnClasses( Arrays.asList( COL_CLASSES ) );
        super.setColumnNames( Arrays.asList( COL_NAMES ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Object getValueAt( int row, int col )
    {
        PeakMatch match = getRow( row );
        DoubleMatch alphaMatch = match.getAlphaMatch();
        DoubleMatch betaMatch = match.getBetaMatch();

        switch( col )
        {
            case 0:
                return match.getPeak().getRecord().getPeakName();
            case 1:
                return match.getOverallDiff();
            case 2:
                return alphaMatch == null ? null : alphaMatch.getBase();
            case 3:
                return alphaMatch == null ? null : alphaMatch.getOther();
            case 4:
                return alphaMatch == null ? null : alphaMatch.getDelta();
            case 5:
                return betaMatch == null ? null : betaMatch.getBase();
            case 6:
                return betaMatch == null ? null : betaMatch.getOther();
            case 7:
                return betaMatch == null ? null : betaMatch.getDelta();
        }

        throw new IllegalArgumentException( "Column Index does not exist: " +
            col );
    }
}
