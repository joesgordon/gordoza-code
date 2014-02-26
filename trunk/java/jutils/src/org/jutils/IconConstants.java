package org.jutils;

import java.awt.Image;
import java.util.List;

/*******************************************************************************
 * Defines the constants needed to access the icons in this library.
 ******************************************************************************/
public final class IconConstants
{
    public final static String ATOMIC_32 = "atomic32.png";

    public final static String BACK_16 = "back16.png";

    public final static String BACK_24 = "back24.png";

    public final static String BINARY_32 = "binary32.png";

    public final static String CALENDAR_16 = "calendar16.png";

    public final static String CALENDAR_32 = "calendar32.png";

    public final static String CHAT_16 = "chat16.png";

    public final static String CHAT_32 = "chat32.png";

    public final static String CHAT_48 = "chat48.png";

    public final static String CHAT_64 = "chat64.png";

    public final static String CLOCK_24 = "clock24.png";

    public final static String CLOSE_16 = "close16.png";

    public final static String CHECK_16 = "check16.png";

    public final static String CONFIG_16 = "config16.png";

    public final static String CONFIG_24 = "config24.png";

    public final static String EDIT_ADD_16 = "edit-add16.png";

    public final static String EDIT_DELETE_16 = "edit-delete16.png";

    public final static String EDIT_16 = "edit16.png";

    public final static String FIND_16 = "find16.png";

    public final static String FIND_32 = "find32.png";

    public final static String FONT_24 = "font24.png";

    public final static String FORWARD_16 = "forward16.png";

    public final static String FORWARD_24 = "forward24.png";

    public final static String IM_USER_16 = "im-user16.png";

    public final static String IM_USER_OFFLINE_16 = "im-invisible-user16.png";

    public final static String IM_USER_32 = "im-user32.png";

    public final static String IM_USER_OFFLINE_32 = "im-invisible-user32.png";

    public final static String IMPORT_16 = "document-import16.png";

    public final static String EXPORT_16 = "document-export16.png";

    public final static String LAUNCH_16 = "launch16.png";

    public final static String NEW_FILE_16 = "newFile16.png";

    public final static String OPEN_FILE_16 = "open16.png";

    public final static String OPEN_FOLDER_16 = "folder16.png";

    public final static String OPEN_FOLDER_32 = "folder32.png";

    public final static String PAGEMAG_16 = "document-preview16.png";

    public final static String PAGEMAG_24 = "document-preview24.png";

    public final static String PAGEMAG_32 = "document-preview32.png";

    public final static String PAGEMAG_64 = "document-preview64.png";

    public final static String PAGEMAG_128 = "document-preview128.png";

    public final static String REFRESH_16 = "refresh16.png";

    public final static String REFRESH_24 = "refresh24.png";

    public final static String SAVE_16 = "save16.png";

    public final static String SAVE_AS_16 = "saveAs16.png";

    public final static String SORT_DOWN_16 = "sortDown16.png";

    public final static String STOP_16 = "stop16.png";

    public final static String USER_ADD_24 = "userAdd24.png";

    public final static String UNDO_16 = "undo16.png";

    public final static String REDO_16 = "redo16.png";

    public final static String LEFT_16 = "arrow-left16.png";

    public final static String RIGHT_16 = "arrow-right16.png";

    public final static String DOWN_16 = "down16.png";

    public final static String UP_16 = "arrow-up16.png";

    public final static String UP_24 = "up24.png";

    /** The icon loader to be used to access icons in this project. */
    public final static IconLoader loader = new IconLoader(
        IconConstants.class, "icons" );

    /***************************************************************************
     * Private constructor to prevent instantiation.
     **************************************************************************/
    private IconConstants()
    {
    }

    public static List<Image> getPageMagImages()
    {
        return loader.getImages( PAGEMAG_16, PAGEMAG_24, PAGEMAG_32,
            PAGEMAG_64, PAGEMAG_128 );
    }
}
