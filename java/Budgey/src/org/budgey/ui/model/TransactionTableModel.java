package org.budgey.ui.model;

import java.util.Date;

import org.budgey.data.Money;
import org.budgey.data.Transaction;
import org.jutils.ui.model.ITableItemsConfig;

public class TransactionTableModel implements ITableItemsConfig<Transaction>
{
    private static final String [] COLUMN_NAMES = { "Date", "Location",
        "Amount", "Balance", "Tags" };
    private static final Class<?> [] COLUMN_CLASSES = { Date.class,
        String.class, Money.class, Money.class, String.class };

    @Override
    public String [] getColumnNames()
    {
        return COLUMN_NAMES;
    }

    @Override
    public Class<?> [] getColumnClasses()
    {
        return COLUMN_CLASSES;
    }

    @Override
    public Object getItemData( Transaction trans, int col )
    {
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

        throw new IllegalArgumentException(
            "No information for column " + col );
    }

    @Override
    public void setItemData( Transaction item, int col, Object data )
    {
    }

    @Override
    public boolean isCellEditable( Transaction item, int col )
    {
        return false;
    }
}
