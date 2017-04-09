package org.budgey.data;

import java.time.LocalDate;

public class Transaction
{
    private LocalDate date;
    private String secondParty;
    private String tags;
    private Money amount;
    private transient Money runningBalance;

    public Transaction()
    {
        date = LocalDate.now();
        secondParty = "";
        tags = "";
        amount = new Money( 0 );
        runningBalance = new Money( 0 );
    }

    public LocalDate getDate()
    {
        return date;
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
        return tags;
    }

    public void setAmount( Money m )
    {
        m.hashCode();
        amount = m;
    }

    public void setTags( String tags )
    {
        this.tags = tags;
    }

    public void setDate( LocalDate date )
    {
        this.date = date;
    }
}
