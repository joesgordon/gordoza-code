package chatterbox.ui;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.ui.model.LabelListCellRenderer.IListCellLabelDecorator;

import chatterbox.data.ChatUser;

/***************************************************************************
 * 
 **************************************************************************/
public class UserListCellDecorator implements IListCellLabelDecorator
{
    /**  */
    private final Icon userAvailableIcon;
    /**  */
    private final Icon userUnavailableIcon;

    /***************************************************************************
     * 
     **************************************************************************/
    public UserListCellDecorator()
    {
        userAvailableIcon = IconConstants.getIcon( IconConstants.IM_USER_32 );
        userUnavailableIcon = IconConstants.getIcon(
            IconConstants.IM_USER_OFFLINE_32 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void decorate( JLabel label, JList<?> list, Object value, int index,
        boolean isSelected, boolean cellHasFocus )
    {
        ChatUser user = ( ChatUser )value;

        label.setText( user.nickName );

        if( user.available )
        {
            label.setIcon( userAvailableIcon );
        }
        else
        {
            label.setIcon( userUnavailableIcon );
        }
    }
}
