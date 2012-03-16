package org.budgey.data;

import java.io.File;
import java.io.IOException;

import org.jutils.io.XStreamUtils;

public class BudgeyOptions
{
    public File lastOpenDir;
    public File lastSaveDir;

    private transient File optionsFile;

    public static BudgeyOptions read( File file )
    {
        BudgeyOptions options;

        try
        {
            options = ( BudgeyOptions )XStreamUtils.readObjectXStream( file );
        }
        catch( IOException e )
        {
            options = new BudgeyOptions();
        }
        options.optionsFile = file;

        return options;
    }

    public void write()
    {
        try
        {
            XStreamUtils.writeObjectXStream( this, optionsFile );
        }
        catch( IOException e )
        {
            // IOException? Meh.
        }
    }
}
