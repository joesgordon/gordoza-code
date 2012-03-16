package org.jutils.ui;

import java.awt.Color;
import java.text.Format;

import javax.swing.JFormattedTextField;

/**
 * 
 */
public class UFormattedTextField extends JFormattedTextField
{
    private Color background = null;

    private Color disabledBackground = new Color( 0xD4D0C8 );

    public UFormattedTextField()
    {
        super();
        init();
    }

    public UFormattedTextField( Object value )
    {
        super( value );
        init();
    }

    public UFormattedTextField( Format format )
    {
        super( format );
        init();
    }

    public UFormattedTextField( AbstractFormatter formatter )
    {
        super( formatter );
        init();
    }

    public UFormattedTextField( AbstractFormatterFactory factory )
    {
        super( factory );
        init();
    }

    public UFormattedTextField( AbstractFormatterFactory factory,
        Object currentValue )
    {
        super( factory, currentValue );
        init();
    }

    private void init()
    {
        background = this.getBackground();
    }

    public void setEnabled( boolean enabled )
    {
        if( enabled != this.isEnabled() )
        {
            if( enabled )
            {
                this.setBackground( background );
            }
            else
            {
                background = this.getBackground();
                this.setBackground( disabledBackground );
            }
        }
        super.setEnabled( enabled );
    }

}
