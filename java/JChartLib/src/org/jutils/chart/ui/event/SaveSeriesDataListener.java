package org.jutils.chart.ui.event;

import java.io.*;

import javax.swing.JOptionPane;

import org.jutils.Utils;
import org.jutils.chart.app.JChartAppConstants;
import org.jutils.chart.app.UserData;
import org.jutils.chart.io.FilteredWriter;
import org.jutils.chart.model.Series;
import org.jutils.io.IOUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.event.IFileSelectionListener;
import org.jutils.ui.model.IDataView;

/***************************************************************************
 * 
 **************************************************************************/
public class SaveSeriesDataListener implements IFileSelectionListener
{
    /**  */
    private final IDataView<Series> view;

    /***************************************************************************
     * @param view
     **************************************************************************/
    public SaveSeriesDataListener( IDataView<Series> view )
    {
        this.view = view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public File getDefaultFile()
    {
        Series s = view.getData();

        return getDefaultFile( s );
    }

    /***************************************************************************
     * @param s
     * @return
     **************************************************************************/
    public static File getDefaultFile( Series s )
    {
        File file = s.getResourceFile();

        if( file != null )
        {
            String ext = IOUtils.getFileExtension( file );
            String name = IOUtils.removeFilenameExtension( file );

            file = new File( file.getParentFile(), name + "_filtered." + ext );
        }
        else
        {
            OptionsSerializer<UserData> options = JChartAppConstants.getOptions();

            file = options.getOptions().lastDataFile;
        }

        return file;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void filesChosen( File [] files )
    {
        Series s = view.getData();
        File toFile = files[0];
        File fromFile = s.getResourceFile();

        try
        {
            saveFile( s, fromFile, toFile );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( view.getView(),
                "Unable to save file: " + Utils.NEW_LINE +
                    toFile.getAbsolutePath() + Utils.NEW_LINE + Utils.NEW_LINE +
                    ex.getMessage(),
                "I/O Error", JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * @param s
     * @param toFile
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    public static void saveFile( Series s, File toFile )
        throws FileNotFoundException, IOException
    {
        saveFile( s, null, toFile );
    }

    /***************************************************************************
     * @param s
     * @param fromFile
     * @param toFile
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    public static void saveFile( Series s, File fromFile, File toFile )
        throws FileNotFoundException, IOException
    {
        OptionsSerializer<UserData> options = JChartAppConstants.getOptions();

        options.getOptions().lastDataFile = toFile;

        if( fromFile == null )
        {
            FilteredWriter.write( toFile, s.data );
        }
        else
        {
            FilteredWriter.write( fromFile, toFile, s.data );
        }
    }
}
