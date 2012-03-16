package org.budgey.ui.model;

import java.util.Arrays;
import java.util.Date;

import org.budgey.data.Money;
import org.budgey.data.Transaction;
import org.jutils.ui.model.ItemTableModel;

public class TransactionTableModel extends ItemTableModel<Transaction>
{
    private static final String[] COLUMN_NAMES = { "Date", "Location",
        "Amount", "Balance", "Tags" };
    private static final Class<?>[] COLUMN_CLASSES = { Date.class,
        String.class, Money.class, Money.class, String.class };

    public TransactionTableModel()
    {
        setColumnClasses( Arrays.asList( COLUMN_CLASSES ) );
        setColumnNames( Arrays.asList( COLUMN_NAMES ) );
    }

    @Override
    public Object getValueAt( int row, int col )
    {
        Transaction trans = getRow( row );

        switch( col )
        {
            case 0:
                return trans.getDate();
            case 1:
                return trans.getSecondParty();
            case 2:
                return trans.getAmount();
            case 3:
                return trans.getBalance();
            case 4:
                return trans.getTag();
        }

        throw new IllegalArgumentException( "No information for column " + col );
    }
}
