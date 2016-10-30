package org.budgey.model;

import org.budgey.data.*;

public class BalanceCalculator
{
    public static Money calculateBalance( Account account )
    {
        Money balance = account.getStartingBalance();

        for( LedgerMonth m : account.getLedgers() )
        {
            m.setStartingBalance( balance );
            balance = calculateBalance( m, balance );
        }

        return balance;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Money calculateBalance( LedgerMonth m, Money startingBalance )
    {
        Money balance = startingBalance;

        for( Transaction t : m.getTransactions() )
        {
            balance = balance.add( t.getAmount() );
        }

        return balance;
    }
}
