package org.budgey.ui;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.budgey.data.Account;
import org.jutils.ui.*;
import org.jutils.ui.UValidationTextField.TextValidator;
import org.jutils.ui.UValidationTextField.ValidityChangedListener;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AccountConfigPanel
{
    /**  */
    private final UValidationTextField nameField;
    /**  */
    private final MoneyTextField startingBalanceField;
    /**  */
    private final ItemActionList<String> nameListeners;
    /**  */
    private Account account;
    /**  */
    private final ItemView itemView;

    /***************************************************************************
     * 
     **************************************************************************/
    public AccountConfigPanel()
    {
        nameField = new UValidationTextField();
        startingBalanceField = new MoneyTextField();
        itemView = new ItemView( createFieldsPanel() );
        nameListeners = new ItemActionList<String>();

        itemView.setCancelVisible( false );
    }

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

        nameField.addValidityChanged( new NameValidityChanged() );
        nameField.setValidator( new NameValidator() );
        nameField.setColumns( 20 );

        panel.add( new JLabel( "Name:" ), new GridBagConstraints( 0, 0, 1, 1,
            0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 4, 4, 2, 2 ), 0, 0 ) );
        panel.add( nameField.getView(), new GridBagConstraints( 0, 1, 1, 1,
            0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 2, 4, 4, 4 ), 0, 0 ) );

        panel.add( new JLabel( "Starting Balance:" ), new GridBagConstraints(
            0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
            GridBagConstraints.NONE, new Insets( 4, 4, 2, 2 ), 0, 0 ) );
        panel.add( startingBalanceField.getView(), new GridBagConstraints( 0,
            3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL, new Insets( 2, 4, 4, 4 ), 0, 0 ) );

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

    public void addOkListener( ActionListener l )
    {
        itemView.addOkListener( l );

        nameField.addActionListener( l );
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
    private class NameValidityChanged implements ValidityChangedListener
    {
        @Override
        public void validityChanged( boolean newValidity )
        {
            itemView.setOkEnabled( newValidity );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class NameValidator implements TextValidator
    {
        @Override
        public boolean validateText( String text )
        {
            boolean valid = text.length() > 0;

            if( valid )
            {
                account.setName( text );
                nameListeners.fireListeners( AccountConfigPanel.this, text );
            }

            return valid;
        }
    }
}
