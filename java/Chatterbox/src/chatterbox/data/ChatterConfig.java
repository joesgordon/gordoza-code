package chatterbox.data;

/***************************************************************************
 * 
 **************************************************************************/
public class ChatterConfig
{
    /**  */
    public final ChatConfig chatCfg;
    /**  */
    public boolean showPopups;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatterConfig()
    {
        this.chatCfg = new ChatConfig();
        this.showPopups = true;
    }

    /***************************************************************************
     * @param cfg
     **************************************************************************/
    public ChatterConfig( ChatterConfig cfg )
    {
        this.chatCfg = new ChatConfig( cfg.chatCfg );
        this.showPopups = cfg.showPopups;
    }
}
