package org.budgey;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import org.jutils.IconLoader;

public final class BudgeyIconConstants
{
    private static final IconLoader loader = new IconLoader(
        BudgeyIconConstants.class, "icons" );

    public static Icon getCalendarAddIcon()
    {
        return loader.getIcon( "calendar_add32.png" );
    }

    public static Icon getBookAddIcon()
    {
        return loader.getIcon( "book_add16.png" );
    }

    public static Icon getCoinsAddIcon()
    {
        return loader.getIcon( "coins_add32.png" );
    }

    public static Icon getCogIcon()
    {
        return loader.getIcon( "cog32.png" );
    }

    public static Icon getBookIcon()
    {
        return loader.getIcon( "book16.png" );
    }

    public static List<? extends Image> getWalletIcons()
    {
        return loader.getImages( "wallet16.png", "wallet24.png",
            "wallet32.png", "wallet64.png", "wallet128.png", "wallet256.png" );
    }

    public static Icon getCheckIcon()
    {
        return loader.getIcon( "check24.png" );
    }

    public static Icon getCancelIcon()
    {
        return loader.getIcon( "cancel24.png" );
    }
}
