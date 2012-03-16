package org.jutils.ui.explorer;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

/*******************************************************************************
 *
 ******************************************************************************/
public class ExplorerTable extends JTable
{
    /**  */
    private DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();

    /**  */
    private ExplorerTableModel model = null;

    /***************************************************************************
     *
     **************************************************************************/
    public ExplorerTable()
    {
        super( new ExplorerTableModel() );
        model = ( ExplorerTableModel )this.getModel();

        rightRenderer.setHorizontalAlignment( SwingConstants.RIGHT );

        this.setShowGrid( false );
        this.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        this.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        // this.setAutoCreateRowSorter( true );
        this.addFocusListener( new ExplorerTable_FocusLostAdapter( this ) );

        this.getTableHeader().setReorderingAllowed( false );

        clearTable();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public ExplorerTableModel getExplorerTableModel()
    {
        return model;
    }

    /***************************************************************************
     *
     **************************************************************************/
    public void clearTable()
    {
        TableColumnModel columnModel = this.getColumnModel();
        columnModel.getColumn( 0 ).setCellRenderer( new FilenameRenderer() );
        columnModel.getColumn( 0 ).setPreferredWidth( 200 );
        columnModel.getColumn( 1 ).setPreferredWidth( 200 );
        columnModel.getColumn( 2 ).setPreferredWidth( 50 );
        columnModel.getColumn( 2 ).setCellRenderer( rightRenderer );
        columnModel.getColumn( 3 ).setPreferredWidth( 150 );
        columnModel.getColumn( 4 ).setPreferredWidth( 150 );
        columnModel.getColumn( 4 ).setCellRenderer( rightRenderer );

        model.clearModel();
    }

    /***************************************************************************
     * @param file File
     **************************************************************************/
    public void addFile( File file )
    {
        model.addFile( file );
    }

    /***************************************************************************
     * @param files File[]
     **************************************************************************/
    public void addFiles( List<? extends ExplorerItem> files )
    {
        model.addFiles( files );
    }

    /***************************************************************************
     * @return File
     **************************************************************************/
    public File getSelectedFile()
    {
        File file = null;
        int row = this.getSelectedRow();

        if( row > -1 && row < model.getRowCount() )
        {
            row = convertRowIndexToModel( row );
            ExplorerItem item = model.getExplorerItem( row );
            file = item.getFile();
        }

        return file;
    }

    /***************************************************************************
     * @return ExplorerItem
     **************************************************************************/
    public ExplorerItem getSelectedItem()
    {
        ExplorerItem item = null;
        int row = this.getSelectedRow();

        if( row > -1 && row < model.getRowCount() )
        {
            row = this.convertRowIndexToModel( row );
            item = model.getExplorerItem( row );
        }

        return item;
    }
}

class ExplorerTable_FocusLostAdapter implements FocusListener
{
    private ExplorerTable table = null;

    public ExplorerTable_FocusLostAdapter( ExplorerTable table )
    {
        this.table = table;
    }

    public void focusGained( FocusEvent e )
    {
        ;
    }

    public void focusLost( FocusEvent e )
    {
        table.getSelectionModel().clearSelection();
    }
}

class FilenameRenderer extends DefaultTableCellRenderer
{
    /**  */
    private static final FileSystemView view = FileSystemView.getFileSystemView();

    public FilenameRenderer()
    {
        super();
    }

    public void setValue( Object value )
    {
        super.setValue( value );

        if( value instanceof ExplorerItem )
        {
            File file = ( ( ExplorerItem )value ).getFile();
            if( file != null && file.exists() )
            {
                Icon icon = view.getSystemIcon( file );
                setIcon( icon );
            }
            else
            {
                setIcon( null );
            }
        }
        else
        {
            setIcon( null );
        }
    }
}
