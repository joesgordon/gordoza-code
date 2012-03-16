package org.jutils.ui;

import java.io.*;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;

import org.jutils.Utils;

/***************************************************************************
 *
 **************************************************************************/
public class UTextArea extends JTextArea
{

    /***************************************************************************
     *
     **************************************************************************/
    public UTextArea()
    {
        this( null, null, 0, 0 );
    }

    /***************************************************************************
     * @param doc Document
     * @param text String
     * @param rows int
     * @param columns int
     **************************************************************************/
    public UTextArea( Document doc, String text, int rows, int columns )
    {
        super( doc, text, rows, columns );
    }

    /***************************************************************************
     * @param reader Reader
     * @throws IOException
     **************************************************************************/
    public void loadFrom( Reader reader ) throws IOException
    {
        BufferedReader buffer = new BufferedReader( reader );

        for( String line = buffer.readLine(); line != null; line = buffer.readLine() )
        {
            append( line + Utils.NEW_LINE );
        }
    }

    /***************************************************************************
     * @param stream InputStream
     * @throws IOException
     **************************************************************************/
    public void loadFrom( InputStream stream ) throws IOException
    {
        BufferedInputStream buffer = new BufferedInputStream( stream );
        byte[] byteBuffer = new byte[1024 * 16];
        int bytesRead = 0;

        while( ( bytesRead = buffer.read( byteBuffer ) ) > 0 )
        {
            this.append( new String( byteBuffer, 0, bytesRead ) );
        }
    }

    /***************************************************************************
     * @param text String
     **************************************************************************/
    public void addTextSafely( String text )
    {
        SwingUtilities.invokeLater( new UTextArea_textAdder( this, text ) );
    }
}

class UTextArea_textAdder implements Runnable
{
    private UTextArea area = null;

    private String text = null;

    public UTextArea_textAdder( UTextArea u, String txt )
    {
        area = u;
        text = txt;
    }

    public void run()
    {
        area.append( text );
    }
}
