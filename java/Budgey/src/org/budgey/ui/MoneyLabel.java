package org.budgey.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

import org.budgey.data.Money;
import org.jutils.core.data.UIProperty;
import org.jutils.core.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MoneyLabel implements IDataView<Money>
{
    /**  */
    private final JTextField moneyField;

    /**  */
    private final Color negativeColor;

    private final Color positiveColor;

    /**  */
    private Money money;

    /***************************************************************************
     * 
     **************************************************************************/
    public MoneyLabel()
    {
        this.moneyField = new JTextField();
        this.negativeColor = Color.red;
        this.positiveColor = UIProperty.PANEL_BACKGROUND.getColor();

        moneyField.setOpaque( true );
        moneyField.setHorizontalAlignment( SwingConstants.RIGHT );
        moneyField.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        moneyField.setEditable( false );
        moneyField.setFont( moneyField.getFont().deriveFont( Font.BOLD ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JTextComponent getView()
    {
        return moneyField;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Money getData()
    {
        return money;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Money money )
    {
        this.money = money;

        if( money.isNegative() )
        {
            moneyField.setBackground( negativeColor );
        }
        else
        {
            moneyField.setBackground( positiveColor );
        }

        moneyField.setText( money.toString() );
        moneyField.validate();
        moneyField.setMinimumSize( moneyField.getPreferredSize() );
    }
}
