package org.budgey.ui.model;

import java.time.LocalDate;

import org.budgey.data.Money;
import org.budgey.data.Transaction;
import org.jutils.ui.model.ITableItemsConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TransactionTableModel implements ITableItemsConfig<Transaction>
{
    /**  */
    private static final String [] COLUMN_NAMES = { "Date", "Location",
        "Amount", "Balance", "Tags" };
    /**  */
    private static final Class<?> [] COLUMN_CLASSES = { LocalDate.class,
        String.class, Money.class, Money.class, String.class };

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String [] getColumnNames()
    {
        return COLUMN_NAMES;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Class<?> [] getColumnClasses()
    {
        return COLUMN_CLASSES;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Object getItemData( Transaction trans, int col )
    {
        switch( col )
        {
            case 0:
                return trans.getDate().toString();
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

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setItemData( Transaction item, int col, Object data )
    {
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean isCellEditable( Transaction item, int col )
    {
        return true;
    }
}
