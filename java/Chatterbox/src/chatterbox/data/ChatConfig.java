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

    /***************************************************************************
     * @param config
     **************************************************************************/
    public ChatConfig( ChatConfig config )
    {
        this.username = config.username;
        this.address = config.address;
        this.port = config.port;
    }

    public void set( ChatConfig config )
    {
        this.username = config.username;
        this.address = config.address;
        this.port = config.port;
    }
}
