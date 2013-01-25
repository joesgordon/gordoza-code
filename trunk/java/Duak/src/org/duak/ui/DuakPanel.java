package org.duak.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.*;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import org.duak.data.FileInfo;
import org.duak.utils.FileSize;
import org.jutils.IconConstants;
import org.jutils.ui.ResizingTable;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DuakPanel extends JPanel
{
    /**  */
    private JLabel locationLabel;
    /**  */
    private JLabel totalLabel;
    /**  */
    private FileResultsTableModel tableModel;
    /**  */
    private ResizingTable<FileResultsTableModel> table;
    /**  */
    private ItemActionList<FileInfo> folderOpenedListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public DuakPanel()
    {
        super( new GridBagLayout() );

        folderOpenedListeners = new ItemActionList<FileInfo>();

        locationLabel = new JLabel();
        totalLabel = new JLabel();
        tableModel = new FileResultsTableModel();

        table = new ResizingTable<FileResultsTableModel>( tableModel );
        JScrollPane tableScrollPane = new JScrollPane( table );

        tableScrollPane.getViewport().setBackground( Color.white );

        TableRowSorter<FileResultsTableModel> sorter;

        sorter = new TableRowSorter<FileResultsTableModel>( tableModel );
        // sorter.setComparator( 1, new FileResultsComparer() );
        RowSorter.SortKey[] keys = new RowSorter.SortKey[] { new RowSorter.SortKey(
            1, SortOrder.DESCENDING ) };
        sorter.setSortKeys( Arrays.asList( keys ) );
        // sorter.toggleSortOrder( 1 );

        table.setRowSorter( sorter );
        table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        table.setShowGrid( false );
        // table.setBackground( getBackground() );
        table.getTableHeader().setReorderingAllowed( false );
        table.setDefaultRenderer( FileInfo.class,
            new FileResultTableCellRenderer() );
        table.addMouseListener( new FolderOpenedListener() );

        add( locationLabel, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                4, 4, 2, 4 ), 0, 0 ) );

        add( totalLabel, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                4, 4, 2, 4 ), 0, 0 ) );

        add( tableScrollPane, new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 2, 4,
                4, 4 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param results
     **************************************************************************/
    public void setResults( FileInfo results )
    {
        locationLabel.setText( results.getFile().getAbsolutePath() );

        totalLabel.setText( new FileSize( results.getSize() ).toString() );

        tableModel.setItems( results.getChildren() );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addFolderOpenedListener( ItemActionListener<FileInfo> l )
    {
        folderOpenedListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class FolderOpenedListener extends MouseAdapter
    {
        @Override
        public void mouseClicked( MouseEvent e )
        {
            if( e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2 )
            {
                int row = table.getSelectedRow();
                if( row > -1 )
                {
                    row = table.convertRowIndexToModel( row );
                    folderOpenedListeners.fireListeners( DuakPanel.this,
                        tableModel.getRow( row ) );
                }
            }
        }
    }

    private static class FileResultTableCellRenderer extends
        DefaultTableCellRenderer
    {
        private FileIconLoader iconLoader;

        public FileResultTableCellRenderer()
        {
            iconLoader = new FileIconLoader();
        }

        public Component getTableCellRendererComponent( JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int column )
        {
            Component c = super.getTableCellRendererComponent( table, value,
                isSelected, hasFocus, row, column );

            if( !( value instanceof FileInfo ) )
            {
                setText( value.toString() );
            }
            else
            {
                FileInfo fileResource = ( FileInfo )value;
                File file = fileResource.getFile();

                setIcon( iconLoader.getSystemIcon( file ) );
                setText( iconLoader.getSystemName( file ) );
            }

            return c;
        }
    }

    private static class FileIconLoader
    {
        private FileSystemView fsv;
        private Map<File, Icon> iconMap;
        private Icon defaultIcon;

        public FileIconLoader()
        {
            fsv = FileSystemView.getFileSystemView();
            iconMap = new HashMap<File, Icon>();
            defaultIcon = IconConstants.loader.getIcon( IconConstants.OPEN_FILE_16 );
        }

        public String getSystemName( File file )
        {
            return fsv.getSystemDisplayName( file );
        }

        public Icon getSystemIcon( File file )
        {
            Icon icon = iconMap.get( file );

            if( icon == null )
            {
                icon = fsv.getSystemIcon( file );

                if( icon == null )
                {
                    icon = defaultIcon;
                }
                iconMap.put( file, icon );
            }

            return icon;
        }
    }
}
