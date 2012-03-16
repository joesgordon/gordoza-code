package org.budgey.data;

import java.util.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Account
{
    /**  */
    private String name;
    /**  */
    private Money startingBalance;
    /**  */
    private List<LedgerMonth> ledgers;

    // private List<Transaction> forecasts;

    /***************************************************************************
     * 
     **************************************************************************/
    public Account()
    {
        name = "New Account";
        startingBalance = new Money( 0 );
        ledgers = new ArrayList<LedgerMonth>();
    }

    /***************************************************************************
     * @param trans
     **************************************************************************/
    public void addTransaction( Transaction trans )
    {
        boolean added = false;
        long time = trans.getDate().getTime();

        for( LedgerMonth ledger : ledgers )
        {
            if( ledger.containsDate( time ) == 0 )
            {
                ledger.addTransaction( trans );
                added = true;
                break;
            }
        }

        if( !added )
        {
            LedgerMonth month = new LedgerMonth();
            month.setDate( time );
            month.addTransaction( trans );
            ledgers.add( month );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<LedgerMonth> getLedgers()
    {
        return ledgers;
    }

    public List<Transaction> getCurrentMonthsTransactions()
    {
        Date start;
        Date end;

        Calendar c = new GregorianCalendar();

        c.set( Calendar.DAY_OF_MONTH, 1 );
        c.set( Calendar.HOUR_OF_DAY, 0 );
        c.set( Calendar.MINUTE, 0 );
        c.set( Calendar.SECOND, 0 );
        c.set( Calendar.MILLISECOND, 0 );

        start = c.getTime();

        c.add( Calendar.MONTH, 1 );

        end = c.getTime();

        return getTransactions( start.getTime(), end.getTime() );
    }

    /***************************************************************************
     * @param beginning
     * @param end
     * @return
     **************************************************************************/
    public List<Transaction> getTransactions( long beginning, long end )
    {
        List<Transaction> transactions = new ArrayList<Transaction>();

        for( LedgerMonth ledger : ledgers )
        {
            if( ledger.containsDate( beginning ) >= 0 &&
                ledger.containsDate( end ) <= 0 )
            {
                transactions.addAll( ledger.getTransactions() );
            }
            else
            {
                break;
            }
        }

        return transactions;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Money getBalance()
    {
        Money balance = startingBalance;

        for( LedgerMonth m : ledgers )
        {
            m.setStartingBalance( balance );
            balance = m.getEndingBalance();
        }

        return balance;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Money getAvailable()
    {
        // TODO calculate available.
        return new Money( 0 );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getName()
    {
        return name;
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
    }

    /***************************************************************************
     * @param name
     **************************************************************************/
    public void setName( String name )
    {
        this.name = name;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return getName();
    }

    /***************************************************************************
     * @param list
     **************************************************************************/
    public void setLedgers( ArrayList<LedgerMonth> list )
    {
        ledgers = list;
    }
}
