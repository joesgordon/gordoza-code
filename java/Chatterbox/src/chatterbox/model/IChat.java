package chatterbox.model;

import java.io.IOException;
import java.util.List;

import org.jutils.ui.event.ItemActionListener;

import chatterbox.data.ChatConfig;
import chatterbox.data.ChatUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IChat
{
    /***************************************************************************
     * @param config
     * @throws IOException
     **************************************************************************/
    public void connect( ChatConfig config ) throws IOException;

    /***************************************************************************
     * 
     **************************************************************************/
    public void disconnect();

    /***************************************************************************
     * @return
     **************************************************************************/
    public ChatConfig getConfig();

    /***************************************************************************
     * @param conversation
     **************************************************************************/
    public void removeConversation( IConversation conversation );

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addConversationCreatedListener(
        ItemActionListener<IConversation> l );

    /***************************************************************************
     * @return
     **************************************************************************/
    public IConversation getDefaultConversation();

    /***************************************************************************
     * @param users
     * @return
     **************************************************************************/
    public IConversation createConversation( List<ChatUser> users );

    /***************************************************************************
     * @return
     **************************************************************************/
    public ChatUser getLocalUser();
}
