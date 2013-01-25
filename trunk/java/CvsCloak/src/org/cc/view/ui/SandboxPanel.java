package org.cc.view.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.cc.model.ICloak;
import org.cc.model.IFileResource;
import org.cc.view.CvIconLoader;
import org.cc.view.ui.model.FileResourceTableModel;
import org.jutils.IconConstants;
import org.jutils.ui.model.ItemTableModel;

/**
 * 
 */
public class SandboxPanel extends JPanel
{
    /**  */
    private ItemTableModel<IFileResource> listModel;
    /**  */
    private JTable table;
    /**  */
    private JButton refreshButton;
    /**  */
    private JButton addButton;

    /**  */
    private File sandbox;
    /**  */
    private ICloak cloak;

    public SandboxPanel( ICloak cloak )
    {
        super( new GridBagLayout() );

        this.cloak = cloak;

        add( createToolBar(), new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 ) );
        add( createMainPanel(), new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 ) );
    }

    private JToolBar createToolBar()
    {
        JToolBar toolbar = new JToolBar();

        refreshButton = new JButton( CvIconLoader.getRefreshIcon() );
        refreshButton.addActionListener( new RefreshListener() );
        refreshButton.setFocusable( false );
        refreshButton.setToolTipText( "Refresh status" );
        toolbar.add( refreshButton );

        toolbar.addSeparator();

        addButton = new JButton( CvIconLoader.getAddIcon() );
        addButton.setFocusable( false );
        addButton.setToolTipText( "Add the selected file(s)" );
        toolbar.add( addButton );

        JButton removeButton = new JButton( CvIconLoader.getRemoveIcon() );
        removeButton.setFocusable( false );
        removeButton.setToolTipText( "Remove the selected file(s)" );
        toolbar.add( removeButton );

        JButton revertButton = new JButton( CvIconLoader.getRevertIcon() );
        revertButton.setFocusable( false );
        revertButton.setToolTipText( "Revert the selected file(s)" );
        toolbar.add( revertButton );

        toolbar.addSeparator();

        JButton updateButton = new JButton( CvIconLoader.getUpdateIcon() );
        updateButton.setFocusable( false );
        updateButton.setToolTipText( "Update your sandbox" );
        toolbar.add( updateButton );

        JButton commitButton = new JButton( CvIconLoader.getCommitIcon() );
        commitButton.setFocusable( false );
        commitButton.setToolTipText( "Commit the selected files" );
        toolbar.add( commitButton );

        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        return toolbar;
    }

    private JComponent createMainPanel()
    {
        listModel = new FileResourceTableModel();
        table = new JTable();
        JScrollPane listScrollPane = new JScrollPane( table );

        table.setModel( listModel );
        table.setShowGrid( false );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
        table.setRowHeight( 19 );

        TableColumn col;

        col = table.getColumnModel().getColumn( 0 );
        col.setWidth( 55 );
        col.setPreferredWidth( 55 );
        col.setMaxWidth( 55 );
        col.setMinWidth( 55 );

        col = table.getColumnModel().getColumn( 1 );
        col.setCellRenderer( new FileResourceCellRenderer() );

        return listScrollPane;
    }

    public void setSandbox( File sb )
    {
        sandbox = sb;
        listModel.setItems( cloak.getStatus( sb ) );
    }

    private class RefreshListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            listModel.setItems( cloak.getStatus( sandbox ) );
        }
    }
}

class FileResourceCellRenderer extends DefaultTableCellRenderer
{
    private FileIconLoader iconLoader;

    public FileResourceCellRenderer()
    {
        iconLoader = new FileIconLoader();
    }

    public Component getTableCellRendererComponent( JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column )
    {
        IFileResource fileResource = ( IFileResource )value;
        File file = fileResource.getFile();

        setIcon( iconLoader.getSystemIcon( file ) );
        setText( fileResource.getRepositoryPath() );

        return super.getTableCellRendererComponent( table, value, isSelected,
            hasFocus, row, column );
    }
}

class FileIconLoader
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
