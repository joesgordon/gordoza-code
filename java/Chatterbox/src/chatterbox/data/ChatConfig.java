package chatterbox.data;

import chatterbox.ChatterboxConstants;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatConfig
{
    /**  */
    public String username;
    /**  */
    public String address;
    /**  */
    public int port;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatConfig()
    {
        this.username = ChatterboxConstants.DEFAULT_USERNAME;
        this.address = "238.192.69.69";
        this.port = 6969;
    }
}
