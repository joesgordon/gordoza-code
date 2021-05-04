package chatterbox.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import org.jutils.core.IconConstants;
import org.jutils.core.SwingUtils;
import org.jutils.core.data.UIProperty;
import org.jutils.core.ui.TitleView;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.model.IDataView;

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
            this.bgColor = UIProperty.LIST_BACKGROUND.getColor();
            this.sbgColor = UIProperty.LIST_SELECTIONBACKGROUND.getColor();
            this.fgColor = UIProperty.LIST_FOREGROUND.getColor();
            this.sfgColor = UIProperty.LIST_SELECTIONFOREGROUND.getColor();
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
