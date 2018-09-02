package org.budgey;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import org.jutils.io.IconLoader;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class BudgeyIcons
{
    /** The icon loader to be used to access icons in this project. */
    private static final IconLoader loader = new IconLoader( BudgeyIcons.class,
        "icons" );

    /** The prefix of the application icons. */
    public static final String APP_PREFIX = "wallet";

    /** The name of the 24 pixel wallet icon. */
    public static final String WALLET_24 = APP_PREFIX + "24.png";

    /** The name of the 32 pixel add calendar icon. */
    public static final String CALLENDAR_ADD_32 = "calendar_add32.png";

    /** The name of the 32 pixel add book icon. */
    public static final String BOOK_ADD_32 = "book_add16.png";

    /** The name of the 32 pixel add coins icon. */
    public static final String COINS_ADD_32 = "coins_add32.png";

    /** The name of the 32 pixel cog icon. */
    public static final String COG_32 = "cog32.png";

    /** The name of the 16 pixel book icon. */
    public static final String BOOK_16 = "book16.png";

    /** The name of the 16 pixel book icon. */
    public static final String CHECK_24 = "check24.png";

    /** The name of the 16 pixel book icon. */
    public static final String CANCEL_24 = "cancel24.png";

    /***************************************************************************
     * Private default constructor to prevent instantiation of this class.
     **************************************************************************/
    private BudgeyIcons()
    {
    }

    /***************************************************************************
     * Loads and returns a list of images to be used for this application.
     * @return the list of application images.
     **************************************************************************/
    public static List<Image> getAppImages()
    {
        return loader.getImages( IconLoader.buildNameList( APP_PREFIX ) );
    }

    /***************************************************************************
     * Returns an icon for the provided name.
     * @param name the name of the icon to be loaded.
     * @return the common icon.
     * @see IconLoader#getIcon(String)
     **************************************************************************/
    public static Icon getIcon( String name )
    {
        return loader.getIcon( name );
    }
}
