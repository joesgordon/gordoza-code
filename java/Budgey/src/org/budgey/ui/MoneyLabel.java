package org.budgey.ui;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.budgey.data.Money;

public class MoneyLabel extends JLabel
{
    private Color negativeColor;
    private Money money;

    public MoneyLabel()
    {
        super();

        // setEditable( false );

        negativeColor = Color.red;

        // setBorder( BorderFactory.createLineBorder( Color.gray ) );
        setOpaque( true );
        setHorizontalAlignment( SwingConstants.RIGHT );
    }

    public void setMoney( Money amount )
    {
        money = amount;

        setText( createAmountFieldString( amount ) );
        invalidate();
    }

    public Money getMoney()
    {
        return money;
    }

    @Override
    public Color getBackground()
    {
        if( money != null && money.isNegative() )
        {
            return negativeColor;
        }

        return super.getBackground();
    }

    private static String createAmountFieldString( Money amount )
    {
        return "<html><b>" + amount.toString() + "</b></html>";
    }
}
