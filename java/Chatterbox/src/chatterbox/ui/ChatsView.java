package chatterbox.ui;

import java.awt.Component;

import javax.swing.JPanel;

import org.jutils.ui.TitleView;
import org.jutils.ui.model.IDataView;

import chatterbox.messenger.Chat;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatsView implements IDataView<Chat>
{
    /**  */
    private final JPanel view;

    /**  */
    private Chat chat;

    public ChatsView()
    {
        this.view = createView();
    }

    private JPanel createView()
    {
        JPanel panel = new JPanel();
        TitleView titleview = new TitleView( "Conversations", panel );
        // TODO Auto-generated method stub
        return titleview.getView();
    }

    @Override
    public Component getView()
    {
        return view;
    }

    @Override
    public Chat getData()
    {
        return chat;
    }

    @Override
    public void setData( Chat data )
    {
        this.chat = data;

        // TODO Auto-generated method stub
    }
}
