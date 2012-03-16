package nmrc.ui.tables.models;

import java.util.*;

import nmrc.model.IPeak;
import nmrc.model.IPeakRecord;

import org.jutils.ui.model.ItemTableModel;

/*******************************************************************************
 * @param <IPeakRecord>
 ******************************************************************************/
public class PeakRecordTableModel extends ItemTableModel<IPeak>
{
    /**  */
    private Class<?>[] COL_CLASSES = new Class<?>[] { String.class,
        Double.class, Double.class, String.class, String.class, String.class,
        String.class, String.class };

    /**  */
    public static final String[] COL_NAMES = new String[] { "Pk", "Hn", "N15",
        "Ca[i]", "Cb[i]", "Ca[i-1]", "Cb[i-1]", "CB" };

    /**  */
    private StringBuilder stringBuilder;

    /***************************************************************************
     * 
     **************************************************************************/
    public PeakRecordTableModel()
    {
        super();

        stringBuilder = new StringBuilder();

        super.setColumnClasses( Arrays.asList( COL_CLASSES ) );
        super.setColumnNames( Arrays.asList( COL_NAMES ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Object getValueAt( int row, int col )
    {
        IPeakRecord record = getRow( row ).getRecord();

        switch( col )
        {
            case 0:
                return record.getPeakName();
            case 1:
                return new Double( record.getHn() );
            case 2:
                return new Double( record.getN15() );
            case 3:
                return join( record.getCarbonAlpha(), "," );
            case 4:
                return join( record.getCarbonBeta(), "," );
            case 5:
                return join( record.getPreviousCarbonAlpha(), "," );
            case 6:
                return join( record.getPreviousCarbonBeta(), "," );
            case 7:
                return join( record.getAlternateCarbonBeta(), "," );
        }

        throw new IllegalArgumentException( "Column Index does not exist: " +
            col );
    }

    /***************************************************************************
     * @param list
     * @param delimiter
     * @return
     **************************************************************************/
    public String join( List<? extends Object> list, String delimiter )
    {
        String str = null;

        if( list != null )
        {
            stringBuilder = new StringBuilder();
            Iterator<?> iter = list.iterator();
            while( iter.hasNext() )
            {
                stringBuilder.append( iter.next() );
                if( iter.hasNext() )
                {
                    stringBuilder.append( delimiter );
                }
            }
            str = stringBuilder.toString();
        }

        return str;
    }
}
