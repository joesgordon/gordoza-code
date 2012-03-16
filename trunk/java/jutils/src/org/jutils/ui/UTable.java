package org.jutils.ui;

import java.lang.reflect.Method;

import javax.swing.JTable;
import javax.swing.SizeSequence;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UTable extends JTable
{
    /***************************************************************************
	 * 
	 **************************************************************************/
    public UTable()
    {
        super();
    }

    /***************************************************************************
	 * 
	 **************************************************************************/
    public void setRowHeight( int row, int rowHeight )
    {
        try
        {
            Class<?> thisClass = this.getClass();
            Class<?> jTableClass = thisClass.getSuperclass();

            Method getRowModelMethod = jTableClass.getDeclaredMethod(
                "getRowModel", ( Class<?> )null );

            getRowModelMethod.setAccessible( true );

            SizeSequence rowModel = ( SizeSequence )getRowModelMethod.invoke(
                this, ( Class<?> )null );

            if( rowHeight < 0 )
            {
                throw new IllegalArgumentException(
                    "New row height less than 0" );
            }

            rowModel.setSize( row, rowHeight );
            resizeAndRepaint();
        }
        catch( Exception ex )
        {
            // ex.printStackTrace();
            super.setRowHeight( row, rowHeight );
        }
    }
}
