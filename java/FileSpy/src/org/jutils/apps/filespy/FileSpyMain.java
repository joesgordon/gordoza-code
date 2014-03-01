package org.jutils.apps.filespy;

import java.io.File;

import org.jutils.apps.filespy.data.FileSpyData;
import org.jutils.io.*;
import org.jutils.io.UserOptionsSerializer.IUserOptionsCreator;
import org.jutils.ui.app.FrameApplication;

/*******************************************************************************
 *
 ******************************************************************************/
public class FileSpyMain
{
    /**  */
    public static final File USER_OPTIONS_FILE = IOUtils.getUsersFile(
        ".jutils", "filespy", "options.xml" );

    /***************************************************************************
     * Application entry point.
     * @param args String[]
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameApplication.invokeLater( new FileSpyApp( createUserIO() ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OptionsCreator implements
        IUserOptionsCreator<FileSpyData>
    {
        @Override
        public FileSpyData createDefaultOptions()
        {
            FileSpyData data = new FileSpyData();

            return data;
        }

        @Override
        public FileSpyData initialize( FileSpyData item_read )
        {
            return new FileSpyData( item_read );
        }
    }

    public static UserOptionsSerializer<FileSpyData> createUserIO()
    {
        UserOptionsSerializer<FileSpyData> userio;
        userio = UserOptionsSerializer.getUserIO( new OptionsCreator(),
            USER_OPTIONS_FILE );

        return userio;
    }
}
