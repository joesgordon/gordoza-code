package chatterbox.data;

import java.util.ArrayList;
import java.util.List;

/***************************************************************************
 * 
 **************************************************************************/
public class ChatInfo
{
    /**  */
    public final String id;
    /**  */
    public String name;
    /**  */
    public final List<String> users;

    /***************************************************************************
     * @param id
     * @param name
     **************************************************************************/
    public ChatInfo( String id, String name )
    {
        this.id = id;
        this.name = name;
        this.users = new ArrayList<>();
    }

    /***************************************************************************
     * @param ci
     **************************************************************************/
    public ChatInfo( ChatInfo ci )
    {
        this.id = ci.id;
        this.name = ci.name;
        this.users = new ArrayList<>( ci.users );
    }
}
