package chatterbox.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.ui.TitleView;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.*;

import chatterbox.model.IUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UserView implements IDataView<List<IUser>>
{
    /**  */
    private final TitleView view;
    /**  */
    private final CollectionListModel<IUser> userModel;
    /**  */
    private final JList<IUser> userList;

    /**  */
    private final ItemActionList<List<IUser>> conversationStartedListeners;

    /**  */
    private List<IUser> users;

    /***************************************************************************
     * 
     **************************************************************************/
    public UserView()
    {
        this.view = new TitleView();
        this.userModel = new CollectionListModel<>();
        this.userList = new JList<>( userModel );

        this.conversationStartedListeners = new ItemActionList<>();

        JScrollPane userScrollPane = new JScrollPane( userList );

        userScrollPane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        userScrollPane.setPreferredSize( new Dimension( 175, 100 ) );
        userScrollPane.setMinimumSize( new Dimension( 100, 100 ) );

        userList.setCellRenderer(
            new LabelListCellRenderer( new UserListCellDecorator() ) );
        userList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        userList.addMouseListener( new UsersMouseListener( this ) );

        view.setTitle( "Users" );
        view.setComponent( userScrollPane );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public List<IUser> getData()
    {
        return users;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( List<IUser> users )
    {
        this.users = users;

        // for( IUser u : users )
        // {
        // LogUtils.printDebug(
        // String.format( "%s: %s", u.getUserId(), u.getDisplayName() ) );
        // }

        userModel.setData( users );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void addConversationStartedListener(
        ItemActionListener<List<IUser>> l )
    {
        conversationStartedListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class UsersMouseListener extends MouseAdapter
    {
        private final UserView view;

        public UsersMouseListener( UserView view )
        {
            this.view = view;
        }

        public void mouseClicked( MouseEvent e )
        {
            if( !"".isEmpty() && e.getClickCount() == 2 )
            {
                int index = view.userList.locationToIndex( e.getPoint() );

                JOptionPane.showMessageDialog( view.getView(),
                    "This functionality is not yet supported. Good try, though.",
                    "Not Supported", JOptionPane.ERROR_MESSAGE );

                if( index > -1 )
                {
                    ArrayList<IUser> users = new ArrayList<IUser>();
                    ListModel<IUser> dlm = view.userList.getModel();
                    Object item = dlm.getElementAt( index );

                    users.add( ( IUser )item );
                    view.conversationStartedListeners.fireListeners( view,
                        users );
                    view.userList.ensureIndexIsVisible( index );
                }
            }
        }
    }
}
