package chatterbox.ui;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;

import org.jutils.core.IconConstants;
import org.jutils.core.ui.model.LabelListCellRenderer.IListCellLabelDecorator;

import chatterbox.data.ChatUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UserListCellDecorator implements IListCellLabelDecorator<ChatUser>
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
    public void decorate( JLabel label, JList<? extends ChatUser> list,
        ChatUser user, int index, boolean isSelected, boolean cellHasFocus )
    {
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
