package org.jutils.ui;

import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.text.Document;

/*******************************************************************************
 *
 ******************************************************************************/
public class UTextField extends JTextField
{
    /**  */
    private Color background = null;

    private Color disabledBackground = new Color( 0xD4D0C8 );

    /***************************************************************************
     * 
     **************************************************************************/
    public UTextField()
    {
        super();
        init();
    }

    /***************************************************************************
     * @param columns
     **************************************************************************/
    public UTextField( int columns )
    {
        super( columns );
        init();
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public UTextField( String text )
    {
        super( text );
        init();
    }

    /***************************************************************************
     * @param text
     * @param columns
     **************************************************************************/
    public UTextField( String text, int columns )
    {
        super( text, columns );
        init();
    }

    /***************************************************************************
     * @param doc
     * @param text
     * @param columns
     **************************************************************************/
    public UTextField( Document doc, String text, int columns )
    {
        super( doc, text, columns );
        init();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void init()
    {
        background = this.getBackground();
    }

    /***************************************************************************
     * 
     **************************************************************************/
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
