package org.cojo.ui.tableModels;

import java.util.Arrays;

import org.cojo.model.CrState;
import org.cojo.model.IChangeRequest;
import org.jutils.ui.model.ItemTableModel;

public class CrTableModel extends ItemTableModel<IChangeRequest>
{
    public static final String [] COLUMN_HEADING = { "#", "Title", "State" };
    public static final Class<?> [] COLUMN_CLASSES = { Integer.class,
        String.class, CrState.class };

    public CrTableModel()
    {
        setColumnNames( Arrays.asList( COLUMN_HEADING ) );
        setColumnClasses( Arrays.asList( COLUMN_CLASSES ) );
    }

    @Override
    public Object getValueAt( int row, int col )
    {
        IChangeRequest cr = getRow( row );

        switch( col )
        {
            case 0:
                return cr.getNumber();
            case 1:
                return cr.getTitle();
            case 2:
                return cr.getState();
        }

        throw new IllegalArgumentException( "Unknown column: " + col );
    }
}
