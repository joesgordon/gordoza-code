package org.budgey.io;

import java.io.*;
import java.util.ArrayList;

import org.budgey.data.*;
import org.jutils.io.ISerializer;
import org.jutils.io.XStreamUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BudgetSerializer implements ISerializer<Budget>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Budget read( InputStream stream ) throws IOException
    {
        Budget budget = ( Budget )XStreamUtils.readObjectXStream( stream );

        init( budget );

        return budget;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( Budget item, OutputStream stream ) throws IOException
    {
        XStreamUtils.writeObjectXStream( item, stream );
    }

    /***************************************************************************
     * @param budget
     **************************************************************************/
    private void init( Budget budget )
    {
        if( budget.getAccounts() == null )
        {
            Account a = new Account();
            budget.addAccount( a );
            budget.setDefaultAccount( a );
        }

        for( Account a : budget.getAccounts() )
        {
            init( a );
        }
    }

    /***************************************************************************
     * @param a
     **************************************************************************/
    private void init( Account a )
    {
        if( a.getName() == null )
        {
            a.setName( "Account" );
        }

        if( a.getStartingBalance() == null )
        {
            a.setStartingBalance( new Money( 0 ) );
        }

        if( a.getLedgers() == null )
        {
            a.setLedgers( new ArrayList<LedgerMonth>() );
        }

        for( LedgerMonth month : a.getLedgers() )
        {
            init( month );
        }
    }

    /***************************************************************************
     * @param month
     **************************************************************************/
    private void init( LedgerMonth month )
    {
        if( month.getTransactions() == null )
        {
            month.setTransactions( new ArrayList<Transaction>() );
            month.calculateEndingBalance();
        }
    }
}
