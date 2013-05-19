package org.budgey.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.*;

import org.budgey.data.Money;
import org.budgey.data.Transaction;
import org.jutils.ui.calendar.CalendarField;
import org.jutils.ui.validation.*;
import org.jutils.ui.validators.ITextValidator;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TransactionView
{
    /**  */
    private final ValidationTextField secondPartyField;
    /**  */
    private final JTextField tagField;
    /**  */
    private final MoneyTextField amountField;
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
        secondPartyField = new ValidationTextField();
        tagField = new JTextField();
        amountField = new MoneyTextField();
        dateField = new CalendarField();

        itemView = new ItemView( createFieldsPanel() );
    }

    public Component getPanel()
    {
        return itemView.getPanel();
    }

    /**
     * @return
     */
    private Component createFieldsPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        secondPartyField.setColumns( 20 );
        secondPartyField.setValidator( new SecondPartyValidtor() );

        amountField.setValidator( new AmountValidtor() );

        panel.add( new JLabel( "Second Party:" ), new GridBagConstraints( 0, 0,
            1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 4, 4, 2, 2 ), 0, 0 ) );
        panel.add( secondPartyField.getView(), new GridBagConstraints( 0, 1, 1,
            1, 0.0, 0.0, GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL, new Insets( 2, 4, 4, 4 ), 0, 0 ) );

        panel.add( new JLabel( "Tag:" ), new GridBagConstraints( 0, 2, 1, 1,
            0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 4, 4, 2, 2 ), 0, 0 ) );
        panel.add( tagField, new GridBagConstraints( 0, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                2, 4, 4, 4 ), 0, 0 ) );

        panel.add( new JLabel( "Amount:" ), new GridBagConstraints( 0, 4, 1, 1,
            0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 4, 4, 2, 2 ), 0, 0 ) );
        panel.add( amountField.getView(), new GridBagConstraints( 0, 5, 1, 1,
            0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 2, 4, 4, 4 ), 0, 0 ) );

        panel.add( new JLabel( "Date:" ), new GridBagConstraints( 0, 6, 1, 1,
            0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 4, 4, 2, 2 ), 0, 0 ) );
        panel.add( dateField, new GridBagConstraints( 0, 7, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                2, 4, 4, 4 ), 0, 0 ) );

        return panel;
    }

    /**
     * @param l
     */
    public void addOkListener( ActionListener l )
    {
        itemView.addOkListener( l );

        secondPartyField.addActionListener( l );
        tagField.addActionListener( l );
        amountField.addActionListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addCancelListener( ActionListener l )
    {
        itemView.addCancelListener( l );
    }

    /***************************************************************************
     * @param transaction
     **************************************************************************/
    public void setTransaction( Transaction transaction )
    {
        this.transaction = transaction;

        Calendar cal = new GregorianCalendar();
        cal.setTime( transaction.getDate() );

        secondPartyField.setText( transaction.getSecondParty() );
        tagField.setText( transaction.getTag() );
        amountField.setData( transaction.getAmount() );
        dateField.setDate( new GregorianCalendar() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Transaction getTransaction()
    {
        return transaction;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void requestFocus()
    {
        secondPartyField.getView().requestFocus();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class SecondPartyValidtor implements ITextValidator
    {
        @Override
        public void validateText( String text ) throws ValidationException
        {
            boolean valid = text.length() > 0;

            if( valid )
            {
                transaction.setSecondParty( text );
            }
            else
            {
                throw new ValidationException( "Field may not be empty" );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class AmountValidtor implements ITextValidator
    {
        @Override
        public void validateText( String text ) throws ValidationException
        {
            boolean valid = false;
            Money m = null;

            // if( transaction == null )
            // {
            // return false;
            // }

            try
            {
                m = amountField.getFormatter().stringToValue( text );
            }
            catch( ParseException ex )
            {
                throw new ValidationException( ex.getMessage() );
            }

            if( valid )
            {
                transaction.setAmount( m );
            }
        }
    }
}
