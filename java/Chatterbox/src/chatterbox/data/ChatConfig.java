package chatterbox.data;

import chatterbox.ChatterboxConstants;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatConfig
{
    /**  */
    public String displayName;
    /**  */
    public String address;
    /**  */
    public int port;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatConfig()
    {
        this.displayName = ChatterboxConstants.DEFAULT_USERNAME;
        this.address = "238.192.69.69";
        this.port = 6969;
    }

    /***************************************************************************
     * @param config
     **************************************************************************/
    public ChatConfig( ChatConfig config )
    {
        this.displayName = config.displayName;
        this.address = config.address;
        this.port = config.port;
    }

    /***************************************************************************
     * @param config
     **************************************************************************/
    public void set( ChatConfig config )
    {
        this.displayName = config.displayName;
        this.address = config.address;
        this.port = config.port;
    }
}
