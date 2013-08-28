package chatterbox.ui;

import java.awt.Component;

import javax.swing.*;

import org.jutils.IconConstants;

import chatterbox.model.IUser;

public class UserListCellRenderer extends DefaultListCellRenderer
{
    private Icon userAvailableIcon;

    private Icon userUnavailableIcon;

    public UserListCellRenderer()
    {
        userAvailableIcon = IconConstants.loader.getIcon( IconConstants.IM_USER_32 );
        userUnavailableIcon = IconConstants.loader.getIcon( IconConstants.IM_USER_OFFLINE_32 );
    }

    @Override
    public Component getListCellRendererComponent( JList<?> list, Object value,
        int index, boolean isSelected, boolean cellHasFocus )
    {
        super.getListCellRendererComponent( list, value, index, isSelected,
            cellHasFocus );

        IUser user = ( IUser )value;

        setText( user.getDisplayName() );

        if( user.isAvailable() )
        {
            setIcon( userAvailableIcon );
        }
        else
        {
            setIcon( userUnavailableIcon );
        }

        return this;
    }
}
