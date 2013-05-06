package org.eglsht.data;

public class SheetSize
{
    public int rows;
    public int cols;

    public SheetSize()
    {
        this( 10, 10 );
    }

    public SheetSize( int rows, int cols )
    {
        this.rows = rows;
        this.cols = cols;
    }
}
