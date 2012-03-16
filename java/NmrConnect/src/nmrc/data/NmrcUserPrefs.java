package nmrc.data;

import org.jutils.io.ConfigFile;

public class NmrcUserPrefs
{
    /**  */
    private ConfigFile<String, String> configData;
    /**  */
    public static final String DEFAULT_FOLDER_KEY = "DEFAULT_FOLDER";

    public NmrcUserPrefs( ConfigFile<String, String> configFile )
    {
        configData = configFile;
    }

    public String getDefaultFolder()
    {
        return configData.get( DEFAULT_FOLDER_KEY );
    }

    public void setDefaultFolder( String folder )
    {
        configData.put( DEFAULT_FOLDER_KEY, folder );
    }
}
