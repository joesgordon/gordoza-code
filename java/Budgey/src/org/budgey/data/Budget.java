package org.budgey.data;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Budget
{
    /**  */
    private final List<Account> accounts;
    // TODO consider removing
    /**  */
    private String defaultAccount;

    /***************************************************************************
     * 
     **************************************************************************/
    public Budget()
    {
        this.accounts = new ArrayList<Account>();
        Account account;

        account = new Account();
        account.setName( "Checking" );
        accounts.add( account );
        this.defaultAccount = account.getName();

        account = new Account();
        account.setName( "Savings" );
        accounts.add( account );

        account = new Account();
        account.setName( "Credit" );
        accounts.add( account );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Money getTotal()
    {
        Money total = new Money( 0 );

        for( Account a : accounts )
        {
            Money balance = a.getBalance();

            total = total.add( balance );
        }

        return total;
    }

    /***********************************************************************
     * @return
     **********************************************************************/
    public List<Account> getAccounts()
    {
        return accounts;
    }

    /***********************************************************************
     * @return
     **********************************************************************/
    public Account getDefaultAccount()
    {
        if( defaultAccount != null )
        {
            for( Account account : accounts )
            {
                if( account.getName().equals( defaultAccount ) )
                {
                    return account;
                }
            }
        }

        return null;
    }

    /***********************************************************************
     * @param account
     **********************************************************************/
    public void setDefaultAccount( Account account )
    {
        defaultAccount = account.getName();
    }

    /***********************************************************************
     * @param a
     **********************************************************************/
    public void addAccount( Account a )
    {
        accounts.add( a );
        if( accounts.size() == 1 )
        {
            setDefaultAccount( a );
        }
    }
}
