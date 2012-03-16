package org.cc.view.ui.model;

import java.util.Arrays;

import org.cc.model.IFileResource;
import org.jutils.ui.model.ItemTableModel;

public class FileResourceTableModel extends ItemTableModel<IFileResource>
{
    private final String[] COL_NAMES = { "Status", "File" };

    public FileResourceTableModel()
    {
        setColumnNames( Arrays.asList( COL_NAMES ) );
    }

    @Override
    public Object getValueAt( int row, int col )
    {
        IFileResource file = getRow( row );

        switch( col )
        {
            case 0:
                return file.getStatus().toChar();

            case 1:
                return file;
        }

        throw new RuntimeException( "No info for column " + col );
    }
}
