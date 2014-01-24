package org.jutils.ui.calendar;

import java.awt.Color;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.UIManager;

//TODO comments

/*******************************************************************************
 *
 ******************************************************************************/
class DayLabel extends JLabel
{
    /**  */
    private final Color dayBG = new Color( 0xFF, 0xFF, 0xFF );
    /**  */
    private final Color dayFG = new Color( 0x00, 0x00, 0x00 );
    /**  */
    private final Color daySelectedBG = UIManager.getColor( "TextArea.selectionBackground" );
    /**  */
    private final Color daySelectedFG = new Color( 0xFF, 0xFF, 0xFF );
    /**  */
    private final Color nonDayFG = Color.gray;

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
        super();

        this.setForeground( dayFG );
        this.setBackground( dayBG );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getDay()
    {
        return Integer.parseInt( this.getText() );
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

        this.setFocusable( isSelected );
        if( isSelected )
        {
            this.setForeground( daySelectedFG );
            this.setBackground( daySelectedBG );
        }
        else
        {
            if( isNonDay )
            {
                this.setForeground( nonDayFG );
            }
            else
            {
                this.setForeground( dayFG );
            }

            this.setBackground( dayBG );
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
                this.setForeground( nonDayFG );
            }
            else
            {
                this.setForeground( dayFG );
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
}
