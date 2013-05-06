package org.eglsht.data;

import java.util.ArrayList;
import java.util.List;

public class SheetEntry
{
    public final List<String> fields;

    public SheetEntry()
    {
        this( 10 );
    }

    public SheetEntry( int cols )
    {
        this.fields = new ArrayList<String>();

        for( int i = 0; i < cols; i++ )
        {
            fields.add( "" );
        }
    }
}
