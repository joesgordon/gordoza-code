package org.budgey.data;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LedgerMonth
{
    /**  */
    private int year;
    /**  */
    private Month month;
    /**  */
    private Money startingBalance;
    /**  */
    private List<Transaction> transactions;
    /**  */
    private Money endingBalance;

    /***************************************************************************
     * 
     **************************************************************************/
    public LedgerMonth()
    {
        this.startingBalance = new Money( 0 );
        this.transactions = new ArrayList<Transaction>();
    }

    /***************************************************************************
     * Returns -1 if this month was strictly before the given date, 0 if the
     * date occurs within this month, or 1 if this month is after the date.
     * @param date
     * @return
     **************************************************************************/
    public int containsDate( LocalDate d )
    {
        LocalDate date = getDate();

        if( d.getMonth() == date.getMonth() && d.getYear() == date.getYear() )
        {
            return 0;
        }

        return date.compareTo( d );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Money getStartingBalance()
    {
        return startingBalance;
    }

    /***************************************************************************
     * @param balance
     **************************************************************************/
    public void setStartingBalance( Money balance )
    {
        startingBalance = balance;

        endingBalance = calculateEndingBalance();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Money getEndingBalance()
    {
        if( endingBalance == null )
        {
            endingBalance = calculateEndingBalance();
        }

        return endingBalance;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Money calculateEndingBalance()
    {
        endingBalance = startingBalance;

        for( Transaction t : transactions )
        {
            endingBalance = t.calcBalance( endingBalance );
        }

        return endingBalance;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public LocalDate getDate()
    {
        return LocalDate.of( year, month, 1 );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<Transaction> getTransactions()
    {
        return transactions;
    }

    /***************************************************************************
     * @param trans
     **************************************************************************/
    public void addTransaction( Transaction trans )
    {
        transactions.add( trans );

        Collections.sort( transactions, new TransactionComparable() );

        endingBalance = calculateEndingBalance();
    }

    /***************************************************************************
     * @param list
     **************************************************************************/
    public void setTransactions( ArrayList<Transaction> list )
    {
        transactions = list;
    }

    /***************************************************************************
     * @param time
     **************************************************************************/
    public void setDate( LocalDate date )
    {
        month = date.getMonth();
        year = date.getYear();
    }

    private class TransactionComparable implements Comparator<Transaction>
    {
        @Override
        public int compare( Transaction thisTrans, Transaction thatTrans )
        {
            return thisTrans.getDate().compareTo( thatTrans.getDate() );
        }
    }
}
