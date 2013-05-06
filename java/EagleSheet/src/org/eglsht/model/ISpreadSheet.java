package org.eglsht.model;

public interface ISpreadSheet
{
    public int getColumnCount();

    public int getRowCount();

    public String getValueAt( int row, int col );

    public String getColumnHeader( int col );

    public String getRowHeader( int row );

    public String getCornerName();

    public void setValueAt( String string, int row, int col );
}
