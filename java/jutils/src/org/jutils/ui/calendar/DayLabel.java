package org.jutils.ui.calendar;

import java.awt.Color;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.UIManager;

import org.jutils.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
class DayLabel implements IView<JLabel>
{
    /**  */
    private static final Color dayBG = new Color( 0xFF, 0xFF, 0xFF );
    /**  */
    private static final Color dayFG = new Color( 0x00, 0x00, 0x00 );
    /**  */
    private static final Color daySelectedBG = UIManager.getColor(
        "TextArea.selectionBackground" );
    /**  */
    private static final Color daySelectedFG = new Color( 0xFF, 0xFF, 0xFF );
    /**  */
    private static final Color nonDayFG = Color.gray;

    /**  */
    private final JLabel label;

    /**  */
    private int year = 0;
    /**  */
    private int month = 0;
    /**  */
    private boolean isSelected = false;
    /**  */
    private boolean isNonDay = false;

    /***************************************************************************
     * 
     **************************************************************************/
    public DayLabel()
    {
        this.label = new JLabel();

        label.setForeground( dayFG );
        label.setBackground( dayBG );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JLabel getView()
    {
        return label;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getDay()
    {
        return Integer.parseInt( label.getText() );
    }

    /***************************************************************************
     * @param year
     **************************************************************************/
    public void setYear( int year )
    {
        this.year = year;
    }

    /***************************************************************************
     * @param month
     **************************************************************************/
    public void setMonth( int month )
    {
        this.month = month;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public GregorianCalendar getDate()
    {
        return new GregorianCalendar( year, month, getDay() );
    }

    /***************************************************************************
     * @param selected
     **************************************************************************/
    public void setSelected( boolean selected )
    {
        isSelected = selected;

        label.setFocusable( isSelected );
        if( isSelected )
        {
            label.setForeground( daySelectedFG );
            label.setBackground( daySelectedBG );
        }
        else
        {
            if( isNonDay )
            {
                label.setForeground( nonDayFG );
            }
            else
            {
                label.setForeground( dayFG );
            }

            label.setBackground( dayBG );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isSelected()
    {
        return isSelected;
    }

    /***************************************************************************
     * @param nonDay
     **************************************************************************/
    public void setNonDay( boolean nonDay )
    {
        isNonDay = nonDay;

        if( !this.isSelected() )
        {
            if( isNonDay )
            {
                label.setForeground( nonDayFG );
            }
            else
            {
                label.setForeground( dayFG );
            }
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isNonDay()
    {
        return isNonDay;
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setText( String text )
    {
        label.setText( text );
    }
}
