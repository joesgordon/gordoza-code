package org.jutils.gitit.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 *
 ******************************************************************************/
public class GititConfig
{
    /**  */
    public final List<File> directories;

    /**  */
    public final List<GititCommand> commands;

    /***************************************************************************
     * 
     **************************************************************************/
    public GititConfig()
    {
        this.directories = new ArrayList<>();
        this.commands = new ArrayList<>();

        this.commands.add( createCmd( "Modified", "repostatus" ) );
        this.commands.add( createCmd( "Sync", "sync" ) );
        this.commands.add( createCmd( "Log", "log" ) );
        this.commands.add( createCmd( "Pull", "pull" ) );
        this.commands.add( createCmd( "Push", "push" ) );
    }

    /***************************************************************************
     * @param name
     * @param cmd
     * @return
     **************************************************************************/
    private static GititCommand createCmd( String name, String cmd )
    {
        String tgp = "\"C:\\Program Files\\TortoiseGit\\bin\\TortoiseGitProc.exe\"";
        return new GititCommand( name, tgp + " /command:" + cmd );
    }

    /***************************************************************************
     *
     **************************************************************************/
    public static class GititCommand
    {
        /**  */
        public String name;
        /**  */
        public String command;

        /**
         * 
         */
        public GititCommand()
        {
            this( "Windows Eplorer", "explorer.exe" );
        }

        /**
         * @param name
         * @param command
         */
        public GititCommand( String name, String command )
        {
            this.name = name;
            this.command = command;
        }
    }
}
