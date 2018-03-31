package chatterbox.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.ui.OkDialogView;
import org.jutils.ui.event.*;
import org.jutils.ui.model.*;

import chatterbox.data.ChatUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UserListView implements IDataView<List<ChatUser>>
{
    /**  */
    private final JPanel view;
    /**  */
    private final CollectionListModel<ChatUser> userModel;
    /**  */
    private final JList<ChatUser> userList;

    /**  */
    private final ItemActionList<List<ChatUser>> conversationStartedListeners;

    /**  */
    private List<ChatUser> users;

    /***************************************************************************
     * 
     **************************************************************************/
    public UserListView()
    {
        this.view = new JPanel( new BorderLayout() );
        this.userModel = new CollectionListModel<>();
        this.userList = new JList<>( userModel );

        this.conversationStartedListeners = new ItemActionList<>();

        JScrollPane userScrollPane = new JScrollPane( userList );

        userScrollPane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        userScrollPane.setPreferredSize( new Dimension( 175, 100 ) );
        userScrollPane.setMinimumSize( new Dimension( 100, 100 ) );

        userList.setCellRenderer(
            new LabelListCellRenderer<>( new UserListCellDecorator() ) );
        userList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        userList.addMouseListener( new UsersMouseListener( this ) );

        view.add( userScrollPane, BorderLayout.CENTER );
        view.setBorder( BorderFactory.createEtchedBorder() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<ChatUser> getData()
    {
        return users;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( List<ChatUser> users )
    {
        this.users = users;

        // for( ChatUser u : users )
        // {
        // LogUtils.printDebug(
        // String.format( "%s: %s", u.getUserId(), u.getDisplayName() ) );
        // }

        userModel.setData( users );
    }

    /***************************************************************************
     * @param user
     **************************************************************************/
    private void showUserInfo( ChatUser user )
    {
        UserInfoView userView = new UserInfoView();
        OkDialogView dialogView = new OkDialogView( getView(),
            userView.getView() );

        userView.setData( user );

        dialogView.show();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void addConversationStartedListener(
        ItemActionListener<List<ChatUser>> l )
    {
        conversationStartedListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class UsersMouseListener extends MouseAdapter
    {
        private final UserListView view;

        public UsersMouseListener( UserListView view )
        {
            this.view = view;
        }

        private JPopupMenu createPopup( ChatUser user )
        {
            JPopupMenu popup = new JPopupMenu();

            popup.add( createShowPropertiesAction( user ) );

            return popup;
        }

        private Action createShowPropertiesAction( ChatUser user )
        {
            ActionListener l = ( e ) -> view.showUserInfo( user );

            return new ActionAdapter( l, "Information", null );
        }

        @Override
        public void mouseReleased( MouseEvent e )
        {
            if( SwingUtilities.isLeftMouseButton( e ) &&
                e.getClickCount() == 2 )
            {
                int index = view.userList.locationToIndex( e.getPoint() );

                JOptionPane.showMessageDialog( view.getView(),
                    "This functionality is not yet supported. Good try, though.",
                    "Not Supported", JOptionPane.ERROR_MESSAGE );

                if( index > -1 )
                {
                    ArrayList<ChatUser> users = new ArrayList<ChatUser>();
                    ListModel<ChatUser> dlm = view.userList.getModel();
                    Object item = dlm.getElementAt( index );

                    users.add( ( ChatUser )item );
                    view.conversationStartedListeners.fireListeners( view,
                        users );
                    view.userList.ensureIndexIsVisible( index );
                }
            }
            else if( SwingUtilities.isRightMouseButton( e ) &&
                e.getClickCount() == 1 )
            {
                int index = view.userList.locationToIndex( e.getPoint() );

                if( index > -1 )
                {
                    view.userList.setSelectedIndex( index );
                    ChatUser user = view.userModel.get( index );
                    JPopupMenu popup = createPopup( user );
                    popup.show( e.getComponent(), e.getX(), e.getY() );
                }
            }
        }
    }
}
