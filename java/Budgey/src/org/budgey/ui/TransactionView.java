package org.budgey.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.budgey.data.Transaction;
import org.jutils.ui.calendar.CalendarField;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.event.updater.ReflectiveUpdater;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IDataView;

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
    private final CalendarField dateField;
    /**  */
    private final ItemView itemView;

    /**  */
    private Transaction transaction;

    /***************************************************************************
     * 
     **************************************************************************/
    public TransactionView()
    {
        secondPartyField = new StringFormField( "Seondary Party", 20, 3, null );
        tagField = new StringFormField( "Tags", 20, 0, null );
        amountField = new MoneyFormField( "Amount" );
        dateField = new CalendarField();

        itemView = new ItemView( createFieldsPanel() );

        setData( new Transaction() );

        secondPartyField.setUpdater(
            new ReflectiveUpdater<>( this, "transaction.secondParty" ) );
        tagField.setUpdater(
            new ReflectiveUpdater<>( this, "transaction.tags" ) );
        amountField.setUpdater(
            new ReflectiveUpdater<>( this, "transaction.amount" ) );
        dateField.addDataUpdater( new DateUpdater(
            new ReflectiveUpdater<>( this, "transaction.date" ) ) );
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
        panel.add( secondPartyField.getField(),
            new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 4, 4, 4 ), 0, 0 ) );

        panel.add( new JLabel( "Tag:" ),
            new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 4, 2, 2 ), 0, 0 ) );
        panel.add( tagField.getField(),
            new GridBagConstraints( 0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 4, 4, 4 ), 0, 0 ) );

        panel.add( new JLabel( "Amount:" ),
            new GridBagConstraints( 0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 4, 2, 2 ), 0, 0 ) );
        panel.add( amountField.getField(),
            new GridBagConstraints( 0, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 4, 4, 4 ), 0, 0 ) );

        panel.add( new JLabel( "Date:" ),
            new GridBagConstraints( 0, 6, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 4, 2, 2 ), 0, 0 ) );
        panel.add( dateField.getView(),
            new GridBagConstraints( 0, 7, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 4, 4, 4 ), 0, 0 ) );

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

        Calendar cal = new GregorianCalendar();
        cal.setTime( transaction.getDate() );

        secondPartyField.setValue( transaction.getSecondParty() );
        tagField.setValue( transaction.getTag() );
        amountField.setValue( transaction.getAmount() );
        dateField.setDate( new GregorianCalendar() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void requestFocus()
    {
        secondPartyField.getField().requestFocus();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DateUpdater implements IUpdater<Calendar>
    {
        private final IUpdater<Long> updater;

        public DateUpdater( IUpdater<Long> updater )
        {
            this.updater = updater;
        }

        @Override
        public void update( Calendar data )
        {
            updater.update( data.getTimeInMillis() );
        }
    }
}
