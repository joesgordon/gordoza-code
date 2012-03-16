package nmrc.ui.tables.models;

import java.util.*;

import nmrc.model.IPeak;

import org.jutils.ui.model.ItemTableModel;

/*******************************************************************************
 * @param <IPeakRecord>
 ******************************************************************************/
public class PeakTableModel extends ItemTableModel<IPeak>
{
    /**  */
    private static final Class<?>[] COL_CLASSES = new Class<?>[] {
        String.class, String.class, Double.class, Double.class, Double.class,
        Double.class };
    /**  */
    public static final String[] COL_NAMES = new String[] { "Name",
        "Previous Name", "Ca", "Cb", "Ca[i-1]", "Cb[i-1]" };
    private StringBuilder stringBuilder;

    /***************************************************************************
     * 
     **************************************************************************/
    public PeakTableModel()
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
        IPeak peak = getRow( row );

        switch( col )
        {
            case 0:
                return peak.getRecord().getPeakName();
            case 1:
                return peak.getPrevious() != null ? peak.getPrevious().getRecord().getPeakName()
                    : null;
            case 2:
                return peak.getAlpha();
            case 3:
                return peak.getBeta();
            case 4:
                return peak.getPreviousAlpha();
            case 5:
                return peak.getPreviousBeta();
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
