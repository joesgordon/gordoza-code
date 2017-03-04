package chatterbox.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.ui.TitleView;
import org.jutils.ui.model.IDataView;

import chatterbox.data.ChatInfo;
import chatterbox.messenger.Chat;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatsView implements IDataView<Chat>
{
    /**  */
    private final JPanel view;
    /**  */
    private DefaultListModel<ChatInfo> chatsModel;
    /**  */
    private JList<ChatInfo> chatsList;

    /**  */
    private Chat chat;

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
        JPanel panel = new JPanel( new BorderLayout() );
        TitleView titleview = new TitleView( "Conversations", panel );
        JScrollPane scrollpane = new JScrollPane( chatsList );

        scrollpane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        scrollpane.getVerticalScrollBar().setUnitIncrement( 12 );

        panel.add( scrollpane );

        return titleview.getView();
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
    public Chat getData()
    {
        return chat;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Chat data )
    {
        this.chat = data;

        // TODO Auto-generated method stub
    }
}
