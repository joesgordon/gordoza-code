package chatterbox.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.*;

import chatterbox.data.ChatUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UsersView implements IDataView<List<ChatUser>>
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
    public UsersView()
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
            new LabelListCellRenderer( new UserListCellDecorator() ) );
        userList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        userList.addMouseListener( new UsersMouseListener( this ) );

        view.add( userScrollPane, BorderLayout.CENTER );
        view.setBorder( BorderFactory.createEtchedBorder() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public List<ChatUser> getData()
    {
        return users;
    }

    /***************************************************************************
     * 
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
        private final UsersView view;

        public UsersMouseListener( UsersView view )
        {
            this.view = view;
        }

        @Override
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
                    ArrayList<ChatUser> users = new ArrayList<ChatUser>();
                    ListModel<ChatUser> dlm = view.userList.getModel();
                    Object item = dlm.getElementAt( index );

                    users.add( ( ChatUser )item );
                    view.conversationStartedListeners.fireListeners( view,
                        users );
                    view.userList.ensureIndexIsVisible( index );
                }
            }
        }
    }
}
