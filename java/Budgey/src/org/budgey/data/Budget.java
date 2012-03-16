package org.budgey.data;

import java.util.ArrayList;
import java.util.List;

public class Budget
{
    private List<Account> accounts;
    private String defaultAccount;

    public Budget()
    {
        accounts = new ArrayList<Account>();
        Account account = new Account();

        accounts.add( account );
        defaultAccount = account.getName();
    }

    public List<Account> getAccounts()
    {
        return accounts;
    }

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

    public void setDefaultAccount( Account account )
    {
        defaultAccount = account.getName();
    }

    public void addAccount( Account a )
    {
        accounts.add( a );
        if( accounts.size() == 1 )
        {
            setDefaultAccount( a );
        }
    }
}
