package org.jutils.ui;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JLabel;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class LedLabel
{
    /** */
    public static final Color DEFAULT_OFF_COLOR = Color.lightGray;
    /** */
    public static final Color DEFAULT_ON_COLOR = Color.green;

    /** */
    private final JLabel label;
    /** */
    private final LedIcon icon;

    /**  */
    private final Color onColor;
    /**  */
    private final Color offColor;

    /***************************************************************************
     * 
     **************************************************************************/
    public LedLabel()
    {
        this( 16 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public LedLabel( String text )
    {
        this( DEFAULT_ON_COLOR, DEFAULT_OFF_COLOR, 16, text );
    }

    /***************************************************************************
     * @param onColor
     **************************************************************************/
    public LedLabel( Color onColor )
    {
        this( onColor, 16 );
    }

    /***************************************************************************
     * @param size
     **************************************************************************/
    public LedLabel( int size )
    {
        this( DEFAULT_ON_COLOR, size );
    }

    /***************************************************************************
     * @param onColor
     * @param size
     **************************************************************************/
    public LedLabel( Color onColor, int size )
    {
        this( onColor, DEFAULT_OFF_COLOR, size );
    }

    /***************************************************************************
     * @param offColor
     * @param onColor
     * @param size
     **************************************************************************/
    public LedLabel( Color onColor, Color offColor, int size )
    {
        this( onColor, offColor, size, "" );
    }

    /***************************************************************************
     * @param onColor
     * @param offColor
     * @param size
     * @param text
     **************************************************************************/
    public LedLabel( Color onColor, Color offColor, int size, String text )
    {
        this.onColor = onColor;
        this.offColor = offColor;
        this.icon = new LedIcon( offColor, size );
        this.label = new JLabel( icon );

        this.label.setText( text );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JComponent getView()
    {
        return label;
    }

    /***************************************************************************
     * @param isOn
     **************************************************************************/
    public void setLit( boolean isOn )
    {
        setColor( isOn ? onColor : offColor );
    }

    /***************************************************************************
     * @param color
     **************************************************************************/
    public void setColor( Color color )
    {
        if( !icon.getColor().equals( color ) )
        {
            icon.setColor( color );
            label.revalidate();
            label.repaint();
        }
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setText( String text )
    {
        label.setText( text );
    }

    /***************************************************************************
     * @param isOn
     * @param text
     **************************************************************************/
    public void setStatus( boolean isOn, String text )
    {
        setLit( isOn );
        setText( text );
    }
}
