package org.jutils.ui.hex;

import javax.swing.table.AbstractTableModel;

public class HexTableModel extends AbstractTableModel
{
    private IByteBuffer buffer;
    private char[] asciiBuffer;

    public HexTableModel()
    {
        asciiBuffer = new char[16];
    }

    public int getBufferSize()
    {
        return buffer != null ? buffer.size() : 0;
    }

    public Class<?> getColumnClass( int col )
    {
        return String.class;
    }

    @Override
    public boolean isCellEditable( int row, int col )
    {
        return true;
    }

    @Override
    public String getColumnName( int col )
    {
        if( col < 16 )
        {
            return HexUtils.BYTE_STRINGS[col];
        }

        return "Ascii";
    }

    public void setBuffer( IByteBuffer buf )
    {
        buffer = buf;

        int lastRow = getRowCount();
        if( lastRow > -1 )
        {
            super.fireTableRowsInserted( 0, lastRow );
        }
    }

    private int getOffset( int row, int col )
    {
        if( col != 16 )
        {
            return row * 16 + col;
        }

        return row * 16;
    }

    @Override
    public void setValueAt( Object val, int row, int col )
    {
        int i = Integer.parseInt( val.toString(), 16 );

        if( i < 0 || i > 255 )
        {
            throw new NumberFormatException( "Number outside range of byte: " +
                Integer.toHexString( i ) );
        }

        buffer.set( getOffset( row, col ), ( byte )i );
    }

    @Override
    public Object getValueAt( int row, int col )
    {
        String str = null;

        if( buffer == null )
        {
            return null;
        }

        int index = getOffset( row, col );

        if( col < 16 )
        {
            if( index < buffer.size() )
            {
                str = HexUtils.BYTE_STRINGS[HexUtils.toUnsigned( buffer.get( index ) )];
            }
        }
        else
        {
            int end = index + 16;
            int count;

            end = end > buffer.size() ? buffer.size() : end;
            count = end - index;

            for( int i = 0; i < count; i++ )
            {
                char ch = ( char )buffer.get( index + i );
                if( ch < 0x20 || ch > 0x7e )
                {
                    ch = ' ';
                    // ch = ( char )( -1 );
                }
                asciiBuffer[i] = ch;
            }

            str = new String( asciiBuffer, 0, count );
        }

        return str;
    }

    @Override
    public int getColumnCount()
    {
        return 17;
    }

    @Override
    public int getRowCount()
    {
        if( buffer == null )
        {
            return 0;
        }

        int len = buffer.size();
        int extra = len % 16 == 0 ? 0 : 1;
        return len > 0 ? len / 16 + extra : 0;
    }

    public IByteBuffer getBuffer()
    {
        return buffer;
    }
}
