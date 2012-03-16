package org.budgey.data;

import java.util.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LedgerMonth
{
    /**  */
    private short year;
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
        startingBalance = new Money( 0 );
        transactions = new ArrayList<Transaction>();
    }

    /***************************************************************************
     * Returns -1 if this month was strictly before the given date, 0 if the
     * date occurs within this month, or 1 if this month is after the date.
     * @param date
     * @return
     **************************************************************************/
    public int containsDate( long date )
    {
        long diff;
        Calendar c = getCalendar();

        diff = c.getTime().getTime() - date;

        if( diff < 0 )
        {
            return -1;
        }
        else
        {
            c.add( Calendar.MONTH, 1 );
            diff = c.getTime().getTime() - date;

            if( diff > 0 )
            {
                return 1;
            }
        }

        return 0;
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
    public Calendar getCalendar()
    {
        return new GregorianCalendar( year, month.toCalendarMonth(), 1 );
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
    public void setDate( long time )
    {
        Date d = new Date( time );
        Calendar c = new GregorianCalendar();
        c.setTime( d );

        month = Month.fromCalendarMonth( c.get( Calendar.MONTH ) );
        year = ( short )c.get( Calendar.YEAR );
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
