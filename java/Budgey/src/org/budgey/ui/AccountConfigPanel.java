package org.budgey.ui;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.budgey.data.Account;
import org.jutils.ValidationException;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.fields.ValidationTextField;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validators.ITextValidator;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AccountConfigPanel
{
    /**  */
    private final ValidationTextField nameField;
    /**  */
    private final MoneyTextField startingBalanceField;
    /**  */
    private final ItemActionList<String> nameListeners;
    /**  */
    private final ItemView itemView;

    /**  */
    private Account account;

    /***************************************************************************
     * 
     **************************************************************************/
    public AccountConfigPanel()
    {
        nameField = new ValidationTextField();
        startingBalanceField = new MoneyTextField();
        itemView = new ItemView( createFieldsPanel() );
        nameListeners = new ItemActionList<String>();

        itemView.setCancelVisible( false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Component getPanel()
    {
        return itemView.getPanel();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createFieldsPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        nameField.addValidityChanged( new NameValidityChanged( this ) );
        nameField.setValidator( new NameValidator( this ) );
        nameField.setColumns( 20 );

        panel.add( new JLabel( "Name:" ),
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 4, 2, 2 ), 0, 0 ) );
        panel.add( nameField.getView(),
            new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 4, 4, 4 ), 0, 0 ) );

        panel.add( new JLabel( "Starting Balance:" ),
            new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 4, 2, 2 ), 0, 0 ) );
        panel.add( startingBalanceField.getView(),
            new GridBagConstraints( 0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 4, 4, 4 ), 0, 0 ) );

        return panel;
    }

    /***************************************************************************
     * @param account
     **************************************************************************/
    public void setData( Account account )
    {
        this.account = account;
        nameField.setText( account.getName() );
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
    public void addNameChangedListeners( ItemActionListener<String> l )
    {
        nameListeners.addListener( l );
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
    private static class NameValidityChanged implements IValidityChangedListener
    {
        private final AccountConfigPanel panel;

        public NameValidityChanged( AccountConfigPanel panel )
        {
            this.panel = panel;
        }

        @Override
        public void signalValid()
        {
            if( panel.itemView != null )
            {
                panel.itemView.setOkEnabled( true );
            }
        }

        @Override
        public void signalInvalid( String reason )
        {
            if( panel.itemView != null )
            {
                panel.itemView.setOkEnabled( false );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class NameValidator implements ITextValidator
    {
        private final AccountConfigPanel panel;

        public NameValidator( AccountConfigPanel panel )
        {
            this.panel = panel;
        }

        @Override
        public void validateText( String text ) throws ValidationException
        {
            boolean valid = text.length() > 0;

            if( valid )
            {
                panel.account.setName( text );
                panel.nameListeners.fireListeners( panel, text );
            }
            else
            {
                throw new ValidationException( "Field may not be empty" );
            }
        }
    }
}
