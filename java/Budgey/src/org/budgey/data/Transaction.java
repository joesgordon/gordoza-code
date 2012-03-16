package org.budgey.data;

import java.util.Date;

public class Transaction
{
    private long date;
    private String secondParty;
    private String tag;
    private Money amount;
    private transient Money runningBalance;

    public Transaction()
    {
        date = new Date().getTime();
        secondParty = "";
        tag = "";
        amount = new Money( 0 );
        runningBalance = new Money( 0 );
    }

    public Date getDate()
    {
        return new Date( date );
    }

    public String getSecondParty()
    {
        return secondParty;
    }

    public void setSecondParty( String party )
    {
        secondParty = party;
    }

    public Money getAmount()
    {
        return amount;
    }

    public Money getBalance()
    {
        return runningBalance;
    }

    public Money calcBalance( Money prevBalance )
    {
        runningBalance = prevBalance.add( amount );
        return runningBalance;
    }

    public String getTag()
    {
        return tag;
    }

    public void setAmount( Money m )
    {
        m.hashCode();
        amount = m;
    }
}
