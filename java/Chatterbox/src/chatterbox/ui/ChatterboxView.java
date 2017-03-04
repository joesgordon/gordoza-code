package chatterbox.ui;

import java.awt.*;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jutils.ui.model.IDataView;

import chatterbox.messenger.Chat;
import chatterbox.messenger.Conversation;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterboxView implements IDataView<Chat>
{
    /**  */
    private final JPanel view;
    /**  */
    private final ChatView defaultConversationView;
    /**  */
    private final ChatsView conversationsView;

    /**  */
    private Chat chat;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatterboxView()
    {
        this.defaultConversationView = new ChatView();
        this.conversationsView = new ChatsView();
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        conversationsView.getView().setMinimumSize( new Dimension( 250, 250 ) );
        conversationsView.getView().setPreferredSize(
            new Dimension( 200, 250 ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 10, 10, 10, 10 ), 0, 0 );
        panel.add( conversationsView.getView(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 10, 0, 10, 10 ), 0, 0 );
        panel.add( defaultConversationView.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatView createConversationView( Conversation conversation )
    {
        ChatView cv = new ChatView();

        cv.setData( conversation );

        return cv;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
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
    public void setData( Chat chat )
    {
        this.chat = chat;

        defaultConversationView.setData( chat.getDefaultConversation() );
    }
}
