package org.budgey.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        LocalDate date = trans.getDate();

        for( LedgerMonth ledger : ledgers )
        {
            if( ledger.containsDate( date ) == 0 )
            {
                ledger.addTransaction( trans );
                added = true;
                break;
            }
        }

        if( !added )
        {
            LedgerMonth month = new LedgerMonth();
            month.setDate( date );
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

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<Transaction> getCurrentMonthsTransactions()
    {
        LocalDate start = LocalDate.now();
        LocalDate end;

        start = LocalDate.of( start.getYear(), start.getMonth(),
            start.getDayOfMonth() );
        end = start.plusMonths( 1 );

        return getTransactions( start, end );
    }

    /***************************************************************************
     * @param beginning
     * @param end
     * @return
     **************************************************************************/
    public List<Transaction> getTransactions( LocalDate beginning,
        LocalDate end )
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
