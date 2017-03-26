package org.budgey.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.*;

import org.budgey.data.Transaction;
import org.budgey.ui.model.TransactionTableModel;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.IView;
import org.jutils.ui.model.ItemsTableModel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TransactionListView implements IView<Component>
{
    /**  */
    private final JPanel transactionPanel;
    /**  */
    private final ItemsTableModel<Transaction> transactionModel;
    /**  */
    private final ItemActionList<Transaction> editTransListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public TransactionListView()
    {
        editTransListeners = new ItemActionList<Transaction>();
        transactionPanel = new JPanel( new BorderLayout() );
        transactionModel = new ItemsTableModel<Transaction>(
            new TransactionTableModel() );
        JTable table = new JTable( transactionModel );
        JScrollPane pane = new JScrollPane( table );

        table.addMouseListener( new TableMouseListener( this,
            editTransListeners, transactionModel ) );

        pane.setBorder( BorderFactory.createEmptyBorder() );

        transactionPanel.add( pane, BorderLayout.CENTER );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addEditTransactionListener( ItemActionListener<Transaction> l )
    {
        editTransListeners.addListener( l );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    @Override
    public Component getView()
    {
        return transactionPanel;
    }

    /***************************************************************************
     * @param transaction
     **************************************************************************/
    public void addTransaction( Transaction transaction )
    {
        transactionModel.addItem( transaction );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<Transaction> getTransactions()
    {
        return transactionModel.getItems();
    }

    /***************************************************************************
     * @param transactions
     **************************************************************************/
    public void setTransactions( List<Transaction> transactions )
    {
        transactionModel.setItems( transactions );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class TableMouseListener extends MouseAdapter
    {
        private final ItemActionList<Transaction> editTransListeners;
        private final ItemsTableModel<Transaction> model;
        private final Object source;

        public TableMouseListener( Object source,
            ItemActionList<Transaction> editTransListeners,
            ItemsTableModel<Transaction> model )
        {
            this.source = source;
            this.editTransListeners = editTransListeners;
            this.model = model;
        }

        @Override
        public void mouseClicked( MouseEvent e )
        {
            if( SwingUtilities.isLeftMouseButton( e ) &&
                e.getClickCount() == 2 )
            {
                JTable table = ( JTable )e.getSource();
                int row = table.getSelectedRow();
                if( row > -1 )
                {
                    Transaction trans = model.getItem( row );

                    editTransListeners.fireListeners( source, trans );
                }
            }
        }
    }
}
