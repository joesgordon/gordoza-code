package nmrc.ui.tables.models;

import java.util.Arrays;

import nmrc.model.IShiftxRecord;

import org.jutils.ui.model.ItemTableModel;

public class ShiftxTableModel extends ItemTableModel<IShiftxRecord>
{
    /**  */
    private static final Class<?>[] COL_CLASSES = new Class<?>[] {
        String.class, String.class, Double.class, Double.class, Double.class,
        Double.class };
    /**  */
    public static final String[] COL_NAMES = new String[] { "AA #", "AA", "HN",
        "N15", "Ca", "Cb" };

    /***************************************************************************
     * 
     **************************************************************************/
    public ShiftxTableModel()
    {
        super();

        super.setColumnClasses( Arrays.asList( COL_CLASSES ) );
        super.setColumnNames( Arrays.asList( COL_NAMES ) );
    }

    @Override
    public Object getValueAt( int row, int col )
    {
        IShiftxRecord record = getRow( row );

        switch( col )
        {
            case 0:
                return record.getAminoAcidNumber();
            case 1:
                return record.getAminoAcidName();
            case 2:
                return record.getHydrogen();
            case 3:
                return record.getNitrogen();
            case 4:
                return record.getCarbonAlpha();
            case 5:
                return record.getCarbonBeta();
        }

        throw new IllegalArgumentException( "Column Index does not exist: " +
            col );
    }
}
