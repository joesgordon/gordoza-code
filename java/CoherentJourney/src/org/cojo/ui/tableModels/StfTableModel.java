package org.cojo.ui.tableModels;

import java.util.Arrays;

import org.cojo.model.ISoftwareTask;
import org.jutils.ui.model.ItemTableModel;

public class StfTableModel extends ItemTableModel<ISoftwareTask>
{
    public static final String[] COLUMN_HEADING = { "#", "Title", "Lead",
        "Est Hours", "Act Hours" };
    public static final Class<?>[] COLUMN_CLASSES = { Integer.class,
        String.class, String.class, Integer.class, Integer.class };

    public StfTableModel()
    {
        super();

        setColumnNames( Arrays.asList( COLUMN_HEADING ) );
        setColumnClasses( Arrays.asList( COLUMN_CLASSES ) );
    }

    @Override
    public Object getValueAt( int row, int col )
    {
        ISoftwareTask cr = getRow( row );

        switch( col )
        {
            case 0:
                return cr.getNumber();
            case 1:
                return cr.getTitle();
            case 2:
                return cr.getLead().getName();
            case 3:
                return cr.getEstimatedHours();
            case 4:
                return cr.getActualHours();
        }

        throw new IllegalArgumentException( "Unknown column: " + col );
    }
}
