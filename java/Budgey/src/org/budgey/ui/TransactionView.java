package org.budgey.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.budgey.data.Transaction;
import org.jutils.core.ui.fields.StringFormField;
import org.jutils.core.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TransactionView implements IDataView<Transaction>
{
    /**  */
    private final StringFormField secondPartyField;
    /**  */
    private final StringFormField tagField;
    /**  */
    private final MoneyFormField amountField;
    /**  */
    // private final DateField dateField;
    /**  */
    private final ItemView itemView;

    /**  */
    private Transaction transaction;

    /***************************************************************************
     * 
     **************************************************************************/
    public TransactionView()
    {
        secondPartyField = new StringFormField( "Seondary Party", 3, null );
        tagField = new StringFormField( "Tags", 0, null );
        amountField = new MoneyFormField( "Amount" );
        // dateField = new DateField( "Date" );

        itemView = new ItemView( createFieldsPanel() );

        setData( new Transaction() );

        secondPartyField.setUpdater( ( d ) -> transaction.setSecondParty( d ) );
        tagField.setUpdater( ( d ) -> transaction.setTags( d ) );
        amountField.setUpdater( ( d ) -> transaction.setAmount( d ) );
        // dateField.setUpdater( ( d ) -> transaction.setDate( d ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createFieldsPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        panel.add( new JLabel( "Second Party:" ),
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 4, 2, 2 ), 0, 0 ) );
        panel.add( secondPartyField.getView(),
            new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 4, 4, 4 ), 0, 0 ) );

        panel.add( new JLabel( "Tag:" ),
            new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 4, 2, 2 ), 0, 0 ) );
        panel.add( tagField.getView(),
            new GridBagConstraints( 0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 4, 4, 4 ), 0, 0 ) );

        panel.add( new JLabel( "Amount:" ),
            new GridBagConstraints( 0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 4, 2, 2 ), 0, 0 ) );
        panel.add( amountField.getView(),
            new GridBagConstraints( 0, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 4, 4, 4 ), 0, 0 ) );

        panel.add( new JLabel( "Date:" ),
            new GridBagConstraints( 0, 6, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 4, 2, 2 ), 0, 0 ) );
        // panel.add( dateField.getView(),
        // new GridBagConstraints( 0, 7, 1, 1, 0.0, 0.0,
        // GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
        // new Insets( 2, 4, 4, 4 ), 0, 0 ) );

        return panel;
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addOkListener( ActionListener l )
    {
        itemView.addOkListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addCancelListener( ActionListener l )
    {
        itemView.addCancelListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return itemView.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Transaction getData()
    {
        return transaction;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Transaction transaction )
    {
        this.transaction = transaction;

        secondPartyField.setValue( transaction.getSecondParty() );
        tagField.setValue( transaction.getTag() );
        amountField.setValue( transaction.getAmount() );
        // dateField.setValue( transaction.getDate() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void requestFocus()
    {
        secondPartyField.getView().requestFocus();
    }
}
