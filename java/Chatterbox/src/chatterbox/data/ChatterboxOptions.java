package chatterbox.data;

import java.util.ArrayList;
import java.util.List;

import org.jutils.net.MulticastInputs;

import chatterbox.ChatterboxConstants;

/***************************************************************************
 * 
 **************************************************************************/
public class ChatterboxOptions
{
    /**  */
    public String displayName;
    /**  */
    public boolean showPopups;
    /**  */
    public boolean isAway;
    /**  */
    public final List<ConversationInfo> activeConversations;
    /**  */
    public final List<UserOption> userOptions;
    /**  */
    public final MulticastInputs chatCfg;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatterboxOptions()
    {
        this.displayName = ChatterboxConstants.DEFAULT_USERNAME;
        this.showPopups = true;
        this.isAway = false;
        this.activeConversations = new ArrayList<>();
        this.userOptions = new ArrayList<>();
        this.chatCfg = new MulticastInputs();
    }

    /***************************************************************************
     * @param cfg
     **************************************************************************/
    public ChatterboxOptions( ChatterboxOptions cfg )
    {
        this.displayName = cfg.displayName;
        this.showPopups = cfg.showPopups;
        this.isAway = cfg.isAway;
        this.activeConversations = new ArrayList<>();
        this.userOptions = new ArrayList<>();
        this.chatCfg = new MulticastInputs( cfg.chatCfg );

        if( cfg.activeConversations != null )
        {
            for( ConversationInfo ci : cfg.activeConversations )
            {
                this.activeConversations.add( new ConversationInfo( ci ) );
            }
        }
        if( cfg.userOptions != null )
        {
            for( UserOption uo : cfg.userOptions )
            {
                this.userOptions.add( new UserOption( uo ) );
            }
        }
    }
}
