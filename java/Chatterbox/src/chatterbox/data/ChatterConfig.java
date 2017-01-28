package chatterbox.data;

import chatterbox.ChatterboxConstants;

/***************************************************************************
 * 
 **************************************************************************/
public class ChatterConfig
{
    /**  */
    public final ChatConfig chatCfg;
    /**  */
    public String displayName;
    /**  */
    public boolean showPopups;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatterConfig()
    {
        this.chatCfg = new ChatConfig();
        this.displayName = ChatterboxConstants.DEFAULT_USERNAME;
        this.showPopups = true;
    }

    /***************************************************************************
     * @param cfg
     **************************************************************************/
    public ChatterConfig( ChatterConfig cfg )
    {
        this.chatCfg = new ChatConfig( cfg.chatCfg );
        this.displayName = cfg.displayName;
        this.showPopups = cfg.showPopups;
    }
}
