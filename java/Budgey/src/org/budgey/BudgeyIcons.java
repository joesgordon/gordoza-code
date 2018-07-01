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
    /**  */
    private static final IconLoader loader = new IconLoader( BudgeyIcons.class,
        "icons" );

    /**  */
    public static final String WALLET_24 = "wallet24.png";

    /***************************************************************************
     * Private default constructor to prevent instantiation of this class.
     **************************************************************************/
    private BudgeyIcons()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Icon getCalendarAddIcon()
    {
        return getIcon( "calendar_add32.png" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Icon getBookAddIcon()
    {
        return getIcon( "book_add16.png" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Icon getCoinsAddIcon()
    {
        return getIcon( "coins_add32.png" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Icon getCogIcon()
    {
        return getIcon( "cog32.png" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Icon getBookIcon()
    {
        return getIcon( "book16.png" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<Image> getWalletIcons()
    {
        return loader.getImages( "wallet16.png", "wallet24.png", "wallet32.png",
            "wallet64.png", "wallet128.png", "wallet256.png" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Icon getCheckIcon()
    {
        return getIcon( "check24.png" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Icon getWallet32Icon()
    {
        return getIcon( "wallet32.png" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Icon getCancelIcon()
    {
        return getIcon( "cancel24.png" );
    }

    /***************************************************************************
     * @param name
     * @return
     **************************************************************************/
    public static Icon getIcon( String name )
    {
        return loader.getIcon( name );
    }
}
