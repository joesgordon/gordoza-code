package org.budgey.ui;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.budgey.BudgeyIconConstants;
import org.jutils.Utils;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class ItemView
{
    /**  */
    private final JPanel panel;
    /**  */
    private final JButton cancelButton;
    /**  */
    private final JButton okButton;

    /***************************************************************************
     * 
     **************************************************************************/
    public ItemView( Component fieldsPanel )
    {
        panel = new JPanel( new GridBagLayout() );

        okButton = new JButton( "OK", BudgeyIconConstants.getCheckIcon() );
        cancelButton = new JButton( "Cancel",
            BudgeyIconConstants.getCancelIcon() );

        panel.add( fieldsPanel,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        panel.add( createButtonPanel(),
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Component getPanel()
    {
        return panel;
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addOkListener( ActionListener l )
    {
        okButton.addActionListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addCancelListener( ActionListener l )
    {
        cancelButton.addActionListener( l );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createButtonPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        // panel.setBorder( BorderFactory.createLineBorder( Color.red ) );

        panel.add( Box.createVerticalStrut( 0 ),
            new GridBagConstraints( 0, 0, 2, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        panel.add( cancelButton,
            new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 4, 4, 20, 4 ), 30, 10 ) );

        panel.add( okButton,
            new GridBagConstraints( 2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 4, 4, 20, 40 ), 30, 10 ) );

        Dimension dim = Utils.getMaxComponentSize( cancelButton, okButton );

        cancelButton.setPreferredSize( dim );
        okButton.setPreferredSize( dim );

        return panel;
    }

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    public void setOkEnabled( boolean enabled )
    {
        okButton.setEnabled( enabled );
    }

    /***************************************************************************
     * @param visible
     **************************************************************************/
    public void setCancelVisible( boolean visible )
    {
        cancelButton.setVisible( visible );
    }
}
