package chatterbox.ui;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.TitleView;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.model.IDataView;

import chatterbox.data.ChatInfo;
import chatterbox.messenger.ChatterboxHandler;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatsView implements IDataView<ChatterboxHandler>
{
    /**  */
    private final JPanel view;
    /**  */
    private DefaultListModel<ChatInfo> chatsModel;
    /**  */
    private JList<ChatInfo> chatsList;

    /**  */
    private ChatterboxHandler chat;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatsView()
    {
        this.chatsModel = new DefaultListModel<>();
        this.chatsList = new JList<>( chatsModel );
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        TitleView titleview = new TitleView( "Conversations", createPanel() );

        return titleview.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createPanel()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        JScrollPane scrollpane = new JScrollPane( chatsList );

        chatsList.setCellRenderer( new ChatInfoCellRenderer() );

        scrollpane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        scrollpane.getVerticalScrollBar().setUnitIncrement( 12 );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( scrollpane, BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createToolbar()
    {
        JToolBar toolbar = new JToolBar();
        ActionListener listener;
        Icon icon;
        Action action;

        SwingUtils.setToolbarDefaults( toolbar );

        listener = ( e ) -> createNewChat();
        icon = IconConstants.getIcon( IconConstants.EDIT_ADD_16 );
        action = new ActionAdapter( listener, "Create Conversation", icon );
        SwingUtils.addActionToToolbar( toolbar, action );

        return toolbar;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void createNewChat()
    {
        // TODO Auto-generated method stub
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
    public ChatterboxHandler getData()
    {
        return chat;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( ChatterboxHandler data )
    {
        this.chat = data;

        chatsModel.clear();

        for( ChatInfo info : chat.getConversationInfos() )
        {
            chatsModel.addElement( info );
        }
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ChatInfoCellRenderer
        implements ListCellRenderer<ChatInfo>
    {
        private final ChatInfoView view;
        private final Color bgColor;
        private final Color sbgColor;
        private final Color fgColor;
        private final Color sfgColor;

        public ChatInfoCellRenderer()
        {
            this.view = new ChatInfoView();
            this.bgColor = UIManager.getColor( "List.background" );
            this.sbgColor = UIManager.getColor( "List.selectionBackground" );
            this.fgColor = UIManager.getColor( "List.foreground" );
            this.sfgColor = UIManager.getColor( "List.selectionForeground" );
        }

        @Override
        public Component getListCellRendererComponent(
            JList<? extends ChatInfo> list, ChatInfo value, int index,
            boolean isSelected, boolean cellHasFocus )
        {
            Color bg = bgColor;
            Color fg = fgColor;

            if( isSelected )
            {
                bg = sbgColor;
                fg = sfgColor;
            }

            view.setColors( bg, fg );
            view.setData( value );
            return view.getView();
        }
    }
}
