package org.budgey.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

import javax.swing.*;

import org.budgey.data.Money;
import org.budgey.data.Transaction;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.*;

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
        DefaultTableItemsConfig<Transaction> itemsTableCfg = new DefaultTableItemsConfig<>();

        itemsTableCfg.addCol( "Date", LocalDate.class, ( t ) -> t.getDate(),
            ( t, f ) -> t.setDate( f ) );
        itemsTableCfg.addCol( "Location", String.class,
            ( t ) -> t.getSecondParty(), ( t, f ) -> t.setSecondParty( f ) );
        itemsTableCfg.addCol( "Amount", Money.class, ( t ) -> t.getAmount(),
            ( t, f ) -> t.setAmount( f ) );
        itemsTableCfg.addCol( "Balance", Money.class, ( t ) -> t.getBalance() );
        itemsTableCfg.addCol( "Tags", String.class, ( t ) -> t.getTag(),
            ( t, f ) -> t.setTags( f ) );

        editTransListeners = new ItemActionList<Transaction>();
        transactionPanel = new JPanel( new BorderLayout() );
        transactionModel = new ItemsTableModel<>( itemsTableCfg );
        JTable table = new JTable( transactionModel );
        JScrollPane pane = new JScrollPane( table );

        table.addMouseListener( new TableMouseListener( this,
            editTransListeners, transactionModel ) );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

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
        /**  */
        private final ItemActionList<Transaction> editTransListeners;
        /**  */
        private final ItemsTableModel<Transaction> model;
        /**  */
        private final Object source;

        /**
         * @param source
         * @param editTransListeners
         * @param model
         */
        public TableMouseListener( Object source,
            ItemActionList<Transaction> editTransListeners,
            ItemsTableModel<Transaction> model )
        {
            this.source = source;
            this.editTransListeners = editTransListeners;
            this.model = model;
        }

        /**
         * {@inheritDoc}
         */
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
