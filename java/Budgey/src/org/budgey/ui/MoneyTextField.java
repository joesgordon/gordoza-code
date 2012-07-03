package org.budgey.ui;

import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.text.DefaultFormatterFactory;

import org.budgey.data.Money;
import org.jutils.ui.UValidationTextField;
import org.jutils.ui.UValidationTextField.ITextValidator;
import org.jutils.ui.model.IJcompView;

public class MoneyTextField implements IJcompView
{
    private UValidationTextField field;
    private MoneyFormatter formatter;
    private static final Pattern p;

    static
    {
        p = Pattern.compile( "\\$?(\\d+)\\.?(\\d{0,2})" );
    }

    public MoneyTextField()
    {
        super();
        formatter = new MoneyFormatter();
        field = new UValidationTextField( new DefaultFormatterFactory(
            formatter ) );
    }

    public MoneyFormatter getFormatter()
    {
        return formatter;
    }

    public JComponent getView()
    {
        return field.getView();
    }

    public static class MoneyFormatter extends AbstractFormatter
    {
        @Override
        public Money stringToValue( String text ) throws ParseException
        {
            Money m = null;
            long cents = 0;

            Matcher matcher = p.matcher( text );
            if( matcher.matches() )
            {
                String str;
                long num;

                str = matcher.group( 1 );
                if( str != null && str.length() > 0 )
                {
                    num = Long.parseLong( str );
                    cents += num * 100;
                }

                str = matcher.group( 2 );
                if( str != null && str.length() > 0 )
                {
                    num = Long.parseLong( str );
                    if( str.length() < 2 )
                    {
                        num *= 10;
                    }
                    cents += num;
                }

                m = new Money( cents );
            }
            else
            {
                throw new ParseException( "Not a money value: " + text, 0 );
            }

            return m;
        }

        @Override
        public String valueToString( Object value ) throws ParseException
        {
            return value == null ? new Money( 0 ).toString() : value.toString();
        }
    }

    public void setData( Money amount )
    {
        field.setText( amount.toString() );
    }

    public void setValidator( ITextValidator tv )
    {
        field.setValidator( tv );
    }

    public void addActionListener( ActionListener l )
    {
        field.addActionListener( l );
    }
}
