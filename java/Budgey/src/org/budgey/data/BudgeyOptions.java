package org.budgey.data;

import java.io.File;

import org.jutils.utils.UniqueMaxStack;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BudgeyOptions
{
    /**  */
    public final UniqueMaxStack<File> lastBudgets;

    /***************************************************************************
     * 
     **************************************************************************/
    public BudgeyOptions()
    {
        this.lastBudgets = new UniqueMaxStack<>( 20 );
    }

    /***************************************************************************
     * @param options
     **************************************************************************/
    public BudgeyOptions( BudgeyOptions options )
    {
        this();

        lastBudgets.pushAll( options.lastBudgets );
    }
}
