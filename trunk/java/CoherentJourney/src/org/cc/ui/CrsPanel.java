package org.cc.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.*;

import org.cc.model.IChangeRequest;
import org.cc.ui.tableModels.CrTableModel;
import org.jutils.IconConstants;

/***************************************************************************
 * 
 ******************************************************************************/
public class CrsPanel extends JPanel
{
    /**  */
    private CrTableModel crTableModel;
    /**  */
    private JTable crTable;

    /***************************************************************************
     * 
     **************************************************************************/
    public CrsPanel()
    {
        super( new BorderLayout() );

        crTableModel = new CrTableModel();

        add( createToolbar(), BorderLayout.NORTH );
        add( createMainPanel(), BorderLayout.CENTER );
    }

    /***************************************************************************
     * @param crs
     **************************************************************************/
    public void setData( List<IChangeRequest> crs )
    {
        crTableModel.setItems( crs );

        if( crs != null && !crs.isEmpty() )
        {
            crTable.getSelectionModel().setSelectionInterval( 0, 0 );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createMainPanel()
    {
        JPanel mainPanel = new JPanel( new BorderLayout() );
        crTable = new JTable( crTableModel );
        JScrollPane crsScrollPane = new JScrollPane( crTable );

        crTable.setAutoResizeMode( JTable.AUTO_RESIZE_LAST_COLUMN );
        crTable.getColumnModel().getColumn( 0 ).setPreferredWidth( 50 );
        crTable.getColumnModel().getColumn( 1 ).setPreferredWidth( 500 );
        crTable.getColumnModel().getColumn( 2 ).setPreferredWidth( 100 );

        mainPanel.add( crsScrollPane, BorderLayout.CENTER );

        return mainPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        JButton addButton = new JButton();

        addButton.setIcon( IconConstants.getIcon( IconConstants.EDIT_ADD_16 ) );
        addButton.setToolTipText( "Add a CR" );
        addButton.setFocusable( false );

        toolbar.add( addButton );

        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        return toolbar;
    }
}
