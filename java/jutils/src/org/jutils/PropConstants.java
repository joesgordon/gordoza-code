package org.jutils;

import java.awt.Color;

import javax.swing.UIManager;

/*******************************************************************************
 * Defines constants of common properties.
 ******************************************************************************/
public class PropConstants
{
    /** The key for background color of a panel. */
    public static final String UI_PANEL_COLOR = "Panel.background";
    /** The key for the system specific line separator. */
    public static final String SYS_LINE_SEP = "line.separator";
    /** The key for the user's home directory. */
    public static final String SYS_USER_DIR = "user.home";
    /** The key for the inactive background for text fields. */
    public static final String INACTIVE_TEXT_BACKGROUND = "TextField.inactiveBackground";

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private PropConstants()
    {
    }

    /***************************************************************************
     * Returns the color for the background of a panel.
     **************************************************************************/
    public static final Color getPanelBackground()
    {
        return UIManager.getColor( UI_PANEL_COLOR );
    }

    /***************************************************************************
     * Returns the color for the background of an inactive text field.
     **************************************************************************/
    public static final Color getInactiveTextBackground()
    {
        return UIManager.getColor( INACTIVE_TEXT_BACKGROUND );
    }
}
