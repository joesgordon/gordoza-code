package nmrc.ui.tables.models;

import java.util.Arrays;

import nmrc.model.IAminoAcid;
import nmrc.model.IPeak;

import org.jutils.ui.model.ItemTableModel;

public class AminoAcidTableModel extends ItemTableModel<IAminoAcid>
{
    /**  */
    private static final Class<?>[] COL_CLASSES = new Class<?>[] {
        String.class, String.class, String.class, Double.class, Double.class,
        Double.class, Double.class, Double.class, Double.class };
    /**  */
    public static final String[] COL_NAMES = new String[] { "AA #", "AA",
        "Peak Name", "HN", "N15", "Ca", "Cb", "Ca[i-1]", "Cb[i-1]" };

    /***************************************************************************
     * 
     **************************************************************************/
    public AminoAcidTableModel()
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
        IAminoAcid aa = getRow( row );
        IPeak pk = aa.getPeak();

        switch( col )
        {
            case 0:
                return aa.getShiftX().getAminoAcidNumber();
            case 1:
                return aa.getShiftX().getAminoAcidName();
            case 2:
                return pk == null ? null : pk.getRecord().getPeakName();
            case 3:
                return pk == null ? null : pk.getRecord().getHn();
            case 4:
                return pk == null ? null : pk.getRecord().getN15();
            case 5:
                return pk == null ? null : pk.getAlpha();
            case 6:
                return pk == null ? null : pk.getBeta();
            case 7:
                return pk == null ? null : pk.getPreviousAlpha();
            case 8:
                return pk == null ? null : pk.getPreviousBeta();
        }

        throw new IllegalArgumentException( "Column Index does not exist: " +
            col );
    }
}
