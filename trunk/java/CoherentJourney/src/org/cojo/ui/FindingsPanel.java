package org.cojo.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.*;

import org.cojo.model.IFinding;
import org.cojo.ui.tableModels.FindingTableModel;
import org.jutils.IconConstants;
import org.jutils.Utils;

public class FindingsPanel extends JPanel
{
    /**  */
    private FindingTableModel findingTableModel;
    /**  */
    private JTable findingTable;

    public FindingsPanel()
    {
        super( new GridBagLayout() );
        JButton addButton = new JButton();
        JButton editButton = new JButton();
        JButton deleteButton = new JButton();
        findingTableModel = new FindingTableModel();
        findingTable = new JTable( findingTableModel );
        JScrollPane findingScrollPane = new JScrollPane( findingTable );

        addButton.setIcon( IconConstants.loader.getIcon( IconConstants.EDIT_ADD_16 ) );
        editButton.setIcon( IconConstants.loader.getIcon( IconConstants.EDIT_16 ) );
        deleteButton.setIcon( IconConstants.loader.getIcon( IconConstants.EDIT_DELETE_16 ) );

        findingTable.addMouseListener( new MouseAdapter()
        {
            public void mouseClicked( MouseEvent e )
            {
                if( e.getClickCount() == 2 )
                {
                    showFindingDialog();
                }
            }
        } );

        add( addButton, new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 4, 0,
                2, 2 ), 0, 0 ) );
        add( editButton, new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 4, 0,
                2, 2 ), 0, 0 ) );
        add( deleteButton, new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 4, 0,
                2, 2 ), 0, 0 ) );

        add( findingScrollPane, new GridBagConstraints( 0, 1, 4, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 4, 0,
                2, 2 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param findings
     **************************************************************************/
    public void setData( List<IFinding> findings )
    {
        findingTableModel.setItems( findings );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showFindingDialog()
    {
        Frame frame = Utils.getComponentsFrame( this );
        FindingPanel findingPanel = new FindingPanel();
        JEditDialog dialog = new JEditDialog( frame, findingPanel );

        int row = findingTable.getSelectedRow();
        row = findingTable.convertRowIndexToModel( row );
        if( row > -1 )
        {
            IFinding finding = findingTableModel.getRow( row );

            findingPanel.setData( finding );

            dialog.setSize( 400, 400 );
            dialog.validate();
            dialog.setLocationRelativeTo( frame );
            dialog.setVisible( true );
        }
    }
}
