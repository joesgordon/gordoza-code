package chatterbox.ui;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.ui.model.LabelListCellRenderer.IListCellLabelDecorator;

import chatterbox.model.IUser;

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
        IUser user = ( IUser )value;

        label.setText( user.getDisplayName() );

        if( user.isAvailable() )
        {
            label.setIcon( userAvailableIcon );
        }
        else
        {
            label.setIcon( userUnavailableIcon );
        }
    }
}
