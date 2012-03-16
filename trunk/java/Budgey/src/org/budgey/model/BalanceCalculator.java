package org.budgey.model;

import org.budgey.data.Account;
import org.budgey.data.LedgerMonth;
import org.budgey.data.Money;
import org.budgey.data.Transaction;

public class BalanceCalculator
{
    public Money calculateBalance( Account account )
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
    public Money calculateBalance( LedgerMonth m, Money startingBalance )
    {
        Money balance = startingBalance;

        for( Transaction t : m.getTransactions() )
        {
            balance = balance.add( t.getAmount() );
        }

        return balance;
    }
}
