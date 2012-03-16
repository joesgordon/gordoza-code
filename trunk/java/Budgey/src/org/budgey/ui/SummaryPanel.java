package org.budgey.ui;

import java.awt.*;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.budgey.data.Money;

public class SummaryPanel extends JPanel
{
    private MoneyLabel balanceField;
    private MoneyLabel availableField;

    public SummaryPanel()
    {
        super( new GridBagLayout() );

        JLabel balanceLabel = new JLabel( "Your account balance is " );
        balanceField = new MoneyLabel();

        JLabel availableLabel = new JLabel( "Your available funds are " );
        availableField = new MoneyLabel();

        add( balanceLabel, new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 0, 0,
                0, 0 ), 0, 0 ) );
        add( balanceField, new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(
                0, 0, 0, 0 ), 0, 0 ) );

        add( availableLabel, new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 4, 0,
                0, 0 ), 0, 0 ) );
        add( availableField, new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(
                0, 0, 0, 0 ), 0, 0 ) );
    }

    public void setBalance( Money amount )
    {
        balanceField.setMoney( amount );
    }

    public void setAvailable( Money amount )
    {
        availableField.setMoney( amount );
    }
}
