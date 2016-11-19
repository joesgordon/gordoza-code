package org.jutils.ui.explorer;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.*;

import org.jutils.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class ExplorerTable implements IView<JTable>
{
    /**  */
    private final DefaultTableCellRenderer rightRenderer;
    /**  */
    private final JTable table;
    /**  */
    private final ExplorerTableModel model;

    /***************************************************************************
     *
     **************************************************************************/
    public ExplorerTable()
    {
        this.model = new ExplorerTableModel();
        this.table = new JTable( model );
        this.rightRenderer = new DefaultTableCellRenderer();

        rightRenderer.setHorizontalAlignment( SwingConstants.RIGHT );

        table.setShowGrid( false );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        // this.setAutoCreateRowSorter( true );
        table.addFocusListener( new ExplorerTable_FocusLostAdapter( this ) );

        table.getTableHeader().setReorderingAllowed( false );

        clearTable();
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public JTable getView()
    {
        return table;
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
        TableColumnModel columnModel = table.getColumnModel();
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
        int row = table.getSelectedRow();

        if( row > -1 && row < model.getRowCount() )
        {
            row = table.convertRowIndexToModel( row );
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
        int row = table.getSelectedRow();

        if( row > -1 && row < model.getRowCount() )
        {
            row = table.convertRowIndexToModel( row );
            item = model.getExplorerItem( row );
        }

        return item;
    }

    private static final class ExplorerTable_FocusLostAdapter
        implements FocusListener
    {
        private ExplorerTable table = null;

        public ExplorerTable_FocusLostAdapter( ExplorerTable table )
        {
            this.table = table;
        }

        @Override
        public void focusGained( FocusEvent e )
        {
            ;
        }

        @Override
        public void focusLost( FocusEvent e )
        {
            table.table.getSelectionModel().clearSelection();
        }
    }

    private static final class FilenameRenderer implements TableCellRenderer
    {
        /**  */
        private static final FileSystemView view = FileSystemView.getFileSystemView();
        /**  */
        private final DefaultTableCellRenderer renderer;

        public FilenameRenderer()
        {
            this.renderer = new DefaultTableCellRenderer();
        }

        @Override
        public Component getTableCellRendererComponent( JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int column )
        {
            renderer.getTableCellRendererComponent( table, value, isSelected,
                hasFocus, row, column );

            if( value instanceof ExplorerItem )
            {
                File file = ( ( ExplorerItem )value ).getFile();
                if( file != null && file.exists() )
                {
                    Icon icon = view.getSystemIcon( file );
                    renderer.setIcon( icon );
                }
                else
                {
                    renderer.setIcon( null );
                }
            }
            else
            {
                renderer.setIcon( null );
            }

            return renderer;
        }
    }
}
