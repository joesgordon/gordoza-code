package org.budgey.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.budgey.data.Account;
import org.budgey.data.Budget;
import org.budgey.data.LedgerMonth;
import org.budgey.data.Money;
import org.budgey.data.Transaction;
import org.jutils.ValidationException;
import org.jutils.io.ISerializer;
import org.jutils.io.XStreamUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BudgetSerializer
    implements ISerializer<Budget, InputStream, OutputStream>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Budget read( InputStream stream )
        throws IOException, ValidationException
    {
        Budget budget = ( Budget )XStreamUtils.readObjectXStream( stream );

        init( budget );

        return budget;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( Budget item, OutputStream stream ) throws IOException
    {
        try
        {
            XStreamUtils.writeObjectXStream( item, stream );
        }
        catch( ValidationException ex )
        {
            throw new IOException( ex );
        }
    }

    /***************************************************************************
     * @param budget
     **************************************************************************/
    private static void init( Budget budget )
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
    private static void init( Account a )
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
    private static void init( LedgerMonth month )
    {
        if( month.getTransactions() == null )
        {
            month.setTransactions( new ArrayList<Transaction>() );
            month.calculateEndingBalance();
        }
    }
}
