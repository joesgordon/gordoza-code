package org.cojo.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.*;

import org.cojo.model.IChangeRequest;
import org.cojo.ui.tableModels.CrTableModel;
import org.jutils.IconConstants;
import org.jutils.ui.model.IView;
import org.jutils.ui.model.ItemsTableModel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CrsPanel implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final ItemsTableModel<IChangeRequest> crTableModel;
    /**  */
    private final JTable crTable;

    /***************************************************************************
     * 
     **************************************************************************/
    public CrsPanel()
    {
        this.view = new JPanel( new BorderLayout() );
        this.crTableModel = new ItemsTableModel<>( new CrTableModel() );
        this.crTable = new JTable( crTableModel );

        view.add( createToolbar(), BorderLayout.NORTH );
        view.add( createMainPanel(), BorderLayout.CENTER );
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

        addButton.setIcon(
            IconConstants.loader.getIcon( IconConstants.EDIT_ADD_16 ) );
        addButton.setToolTipText( "Add a CR" );
        addButton.setFocusable( false );

        toolbar.add( addButton );

        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        return toolbar;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }
}
