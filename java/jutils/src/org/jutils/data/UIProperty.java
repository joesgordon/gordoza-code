package org.jutils.data;

import java.awt.Color;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.border.Border;

/*******************************************************************************
 * Defines constants of common properties.
 ******************************************************************************/
public enum UIProperty
{
    BUTTON_BACKGROUND( "Button.background" ),
    CHECKBOX_ICON( "CheckBox.icon" ),
    CONTROL( "control" ),
    CONTROLSHADOW( "controlShadow" ),
    LABEL_FONT( "Label.font" ),
    LABEL_BACKGROUND( "Label.background" ),
    LIST_BACKGROUND( "List.background" ),
    LIST_SELECTIONBACKGROUND( "List.selectionBackground" ),
    LIST_FOREGROUND( "List.foreground" ),
    LIST_SELECTIONFOREGROUND( "List.selectionForeground" ),
    OPTIONPANE_ERRORICON( "OptionPane.errorIcon" ),
    PANEL_BACKGROUND( "Panel.background" ),
    PROGRESSBAR_FOREGROUND( "ProgressBar.foreground" ),
    INTERNALFRAME_ACTIVETITLEBACKGROUND(
        "InternalFrame.activeTitleBackground" ),
    SCROLLBAR_THUMB( "ScrollBar.thumb" ),
    SCROLLBAR_THUMBSHADOW( "ScrollBar.thumbShadow" ),
    TABLEHEADER_FONT( "TableHeader.font" ),
    TEXTAREA_SELECTIONBACKGROUND( "TextArea.selectionBackground" ),
    TEXTFIELD_BORDER( "TextField.border" ),
    TEXTFIELD_INACTIVEBACKGROUND( "TextField.inactiveBackground" );

    public final String key;

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private UIProperty( String key )
    {
        this.key = key;
    }

    /***************************************************************************
     * Returns the color for the background of a panel.
     **************************************************************************/
    public final Color getColor()
    {
        return UIManager.getColor( key );
    }

    /***************************************************************************
     * Returns the color for the background of a panel.
     **************************************************************************/
    public final Icon getIcon()
    {
        return UIManager.getIcon( key );
    }

    /***************************************************************************
     * Returns the color for the background of a panel.
     **************************************************************************/
    public final Border getBorder()
    {
        return UIManager.getBorder( key );
    }

    /***************************************************************************
     * Returns the color for the background of a panel.
     **************************************************************************/
    public final Font getFont()
    {
        return UIManager.getFont( key );
    }
}
