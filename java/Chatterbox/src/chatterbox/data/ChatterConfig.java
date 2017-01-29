package chatterbox.data;

import org.mc.io.MulticastInputs;

import chatterbox.ChatterboxConstants;

/***************************************************************************
 * 
 **************************************************************************/
public class ChatterConfig
{
    /**  */
    public final MulticastInputs chatCfg;
    /**  */
    public String displayName;
    /**  */
    public boolean showPopups;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatterConfig()
    {
        this.chatCfg = new MulticastInputs();
        this.displayName = ChatterboxConstants.DEFAULT_USERNAME;
        this.showPopups = true;
    }

    /***************************************************************************
     * @param cfg
     **************************************************************************/
    public ChatterConfig( ChatterConfig cfg )
    {
        this.chatCfg = new MulticastInputs( cfg.chatCfg );
        this.displayName = cfg.displayName;
        this.showPopups = cfg.showPopups;
    }
}
