package org.eglsht.data;

import java.util.ArrayList;
import java.util.List;

import org.jutils.ui.sheet.ISpreadSheet;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EagleSheet implements ISpreadSheet
{
    /**  */
    public final List<SheetEntry> entries;
    /**  */
    public String cornerName;

    /***************************************************************************
     * 
     **************************************************************************/
    public EagleSheet()
    {
        this( 10, 10 );
    }

    /***************************************************************************
     * @param rows
     * @param cols
     **************************************************************************/
    public EagleSheet( int rows, int cols )
    {
        this.entries = new ArrayList<SheetEntry>();

        for( int i = 0; i < rows; i++ )
        {
            entries.add( new SheetEntry( cols ) );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getColumnCount()
    {
        int count = 0;

        if( !entries.isEmpty() )
        {
            count = entries.get( 0 ).fields.size();
        }

        return count;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getRowCount()
    {
        return entries.size();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Object getValueAt( int row, int col )
    {
        SheetEntry entry = entries.get( row );

        return entry.fields.get( col );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getColumnHeader( int col )
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getRowHeader( int row )
    {
        return "" + row;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValueAt( Object value, int row, int col )
    {
        SheetEntry entry = entries.get( row );

        entry.fields.set( col, value.toString() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getCornerName()
    {
        return cornerName;
    }
}
