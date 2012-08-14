package org.cojo.ui.tableModels;

import java.util.Arrays;
import java.util.Date;

import org.cojo.model.IFinding;
import org.jutils.ui.model.ItemTableModel;

public class FindingTableModel extends ItemTableModel<IFinding>
{
    public static final String[] COLUMN_HEADING = { "#", "User", "Date",
        "Accepted" };
    public static final Class<?>[] COLUMN_CLASSES = { Integer.class,
        String.class, Date.class, Boolean.class };

    public FindingTableModel()
    {
        setColumnNames( Arrays.asList( COLUMN_HEADING ) );
        setColumnClasses( Arrays.asList( COLUMN_CLASSES ) );
    }

    @Override
    public Object getValueAt( int row, int col )
    {
        IFinding finding = getRow( row );

        switch( col )
        {
            case 0:
                return finding.getNumber();
            case 1:
                return finding.getUser().getName();
            case 2:
                return new Date( finding.getDate() );
            case 3:
                return finding.isAccepted();
        }

        throw new IllegalArgumentException( "Unknown column: " + col );
    }
}
