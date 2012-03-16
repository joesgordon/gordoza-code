package org.jutils.ui;

import java.awt.Color;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/*******************************************************************************
 *
 ******************************************************************************/
public class UComboBox extends JComboBox
{
    /**  */
    private Color background = null;

    /**  */
    private Color disabledBackground = new Color( 0xD4D0C8 );

    /***************************************************************************
	 * 
	 **************************************************************************/
    public UComboBox()
    {
        super();
        init();
    }

    /***************************************************************************
     * @param items
     **************************************************************************/
    public UComboBox( Object[] items )
    {
        super( items );
        init();
    }

    /***************************************************************************
     * @param items
     **************************************************************************/
    public UComboBox( Vector<?> items )
    {
        super( items );
        init();
    }

    /***************************************************************************
     * @param aModel
     **************************************************************************/
    public UComboBox( ComboBoxModel aModel )
    {
        super( aModel );
        init();
    }

    /***************************************************************************
	 * 
	 **************************************************************************/
    private void init()
    {
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
                this.getEditor().getEditorComponent().setBackground( background );
            }
            else
            {
                background = this.getEditor().getEditorComponent().getBackground();
                this.getEditor().getEditorComponent().setBackground(
                    disabledBackground );
            }
        }
        super.setEnabled( enabled );
    }
}
