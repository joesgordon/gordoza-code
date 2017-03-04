package chatterbox.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class ConversationInfo
{
    /**  */
    public final String id;
    /**  */
    public String name;
    /**  */
    public final List<String> users;

    public ConversationInfo( String id, String name )
    {
        this.id = id;
        this.name = name;
        this.users = new ArrayList<>();
    }

    public ConversationInfo( ConversationInfo ci )
    {
        this.id = ci.id;
        this.name = ci.name;
        this.users = new ArrayList<>( ci.users );
    }
}
