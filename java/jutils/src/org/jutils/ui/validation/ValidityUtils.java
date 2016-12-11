package org.jutils.ui.validation;

import javax.swing.Action;
import javax.swing.JButton;

import org.jutils.SwingUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class ValidityUtils
{
    /***************************************************************************
     * 
     **************************************************************************/
    private ValidityUtils()
    {
    }

    /***************************************************************************
     * @param a
     * @param v
     * @param validTip
     **************************************************************************/
    public static void setActionValidity( Action a, Validity v,
        String validTip )
    {
        String tip = v.choose( validTip, v.reason );

        a.setEnabled( v.isValid );
        SwingUtils.setActionToolTip( a, tip );
    }

    /***************************************************************************
     * @param a
     * @param v
     * @param validTip
     **************************************************************************/
    public static void setButtonValidity( JButton b, Validity v,
        String validTip )
    {
        String tip = v.choose( validTip, v.reason );

        b.setEnabled( v.isValid );
        b.setToolTipText( tip );
    }
}
