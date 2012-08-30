package nmrc.controller;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import nmrc.NmrConnectMain;
import nmrc.data.AminoAcid;
import nmrc.data.NmrData;
import nmrc.io.*;
import nmrc.model.*;
import nmrc.ui.NmrConnectFrame;

import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NmrConnectController
{
    /**  */
    private NmrConnectFrame nmrFrame;

    /***************************************************************************
     * @param frame
     **************************************************************************/
    public NmrConnectController( NmrConnectFrame frame )
    {
        nmrFrame = frame;

        ItemActionListener<File> newListener = new ItemActionListener<File>()
        {
            @Override
            public void actionPerformed( ItemActionEvent<File> event )
            {
                createAndSetNmrData( event.getItem() );
            }
        };

        ItemActionListener<File> openListener = new ItemActionListener<File>()
        {
            @Override
            public void actionPerformed( ItemActionEvent<File> event )
            {
                openNmrc( event.getItem() );
            }
        };

        ItemActionListener<File> saveListener = new ItemActionListener<File>()
        {
            @Override
            public void actionPerformed( ItemActionEvent<File> event )
            {
                saveNmrc( event.getItem() );
            }
        };

        frame.addNewListener( newListener );
        frame.addOpenListener( openListener );
        frame.addSaveListener( saveListener );

        new RecordsController( frame.getRecordsPanel() );
        new PeaksController( frame.getPeaksPanel() );
        new AminoAcidsController( frame.getAminoAcidsPanel() );
    }

    /***************************************************************************
     * @param peaksFile
     **************************************************************************/
    private void createAndSetNmrData( File peaksFile )
    {
        IPeakFile peakFile = readPeakFile( peaksFile );
        List<IShiftxRecord> shiftx = readShiftxFile();
        List<IAminoAcid> aas = createAminoAcids( shiftx );

        INmrData nmrData = new NmrData( peakFile.getPeaks(), shiftx, aas );

        nmrFrame.setData( nmrData );
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private IPeakFile readPeakFile( File file )
    {
        IPeakFile peakFile = null;

        try
        {
            FileReader reader = new FileReader( file );
            LineNumberReader lnReader = new LineNumberReader( reader );
            PeakFileReader pfReader = new PeakFileReader( reader );

            peakFile = pfReader.read( lnReader );
            reader.close();
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( nmrFrame, ex.getMessage(),
                "Error reading peak file", JOptionPane.ERROR_MESSAGE );
        }

        return peakFile;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private List<IShiftxRecord> readShiftxFile()
    {
        List<IShiftxRecord> records = null;
        URL shiftxUrl = NmrConnectMain.class.getResource( "shiftX.shx" );
        try
        {
            InputStream stream = shiftxUrl.openStream();
            InputStreamReader reader = new InputStreamReader( stream );
            LineNumberReader lnReader = new LineNumberReader( reader );
            ShiftxFileReader shiftxReader = new ShiftxFileReader( reader );

            records = shiftxReader.read( lnReader );
            stream.close();

        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( nmrFrame, ex.getMessage(),
                "Error reading ShiftX file", JOptionPane.ERROR_MESSAGE );
        }
        return records;
    }

    /***************************************************************************
     * @param shiftx
     * @return
     **************************************************************************/
    private List<IAminoAcid> createAminoAcids( List<IShiftxRecord> shiftx )
    {
        List<IAminoAcid> aas = new ArrayList<IAminoAcid>();

        for( IShiftxRecord record : shiftx )
        {
            aas.add( new AminoAcid( record ) );
        }

        return aas;
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void openNmrc( File file )
    {
        NmrDataSerializer serializer = new NmrDataSerializer();

        try
        {
            FileInputStream stream = new FileInputStream( file );
            INmrData data = serializer.read( stream );
            nmrFrame.setData( data );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( nmrFrame,
                "Yell at Joseph for not doing this yet.",
                "Functionality Not Implemented", JOptionPane.WARNING_MESSAGE );
        }
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void saveNmrc( File file )
    {
        NmrDataSerializer serializer = new NmrDataSerializer();

        try
        {
            FileOutputStream stream = new FileOutputStream( file );
            serializer.write( nmrFrame.getData(), stream );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( nmrFrame,
                "Yell at Joseph for not doing this yet.",
                "Functionality Not Implemented", JOptionPane.WARNING_MESSAGE );
        }
    }
}
