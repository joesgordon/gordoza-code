package org.jutils.ui.hex;

import java.awt.*;
import java.io.*;

import javax.swing.*;

import org.jutils.ui.TitleView;

/*******************************************************************************
 *
 ******************************************************************************/
public class HexEditorFilePanel extends JPanel
{
    /**  */
    private JProgressBar progressBar = new JProgressBar();
    /**  */
    private JLabel offsetLabel = new JLabel( "" );
    /**  */
    private TitleView titlePanel = new TitleView();
    /**  */
    private HexPanel editor = new HexPanel();
    /**  */
    private long startOffset = 0;
    /**  */
    private long fileLength = 0;
    /**  */
    private int maxBufferSize = 1024 * 1024;
    /**  */
    private File currentFile = null;
    /**  */
    private RandomAccessFile raFile = null;

    /***************************************************************************
     * 
     **************************************************************************/
    public HexEditorFilePanel()
    {
        // ---------------------------------------------------------------------
        // Setup progress bar.
        // ---------------------------------------------------------------------
        progressBar.setLayout( new GridBagLayout() );
        progressBar.add( offsetLabel, new GridBagConstraints( 0, 0, 1, 1, 1.0,
            1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                2, 10, 2, 10 ), 0, 0 ) );

        progressBar.setStringPainted( true );
        progressBar.setString( "" );
        progressBar.setIndeterminate( false );
        progressBar.setMaximum( 100 );
        progressBar.setMinimum( 0 );
        progressBar.setValue( 0 );
        // progressBar.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        // ---------------------------------------------------------------------
        // Setup main panel.
        // ---------------------------------------------------------------------
        this.setLayout( new GridBagLayout() );

        // editor.setAlternateRowBG( true );
        // editor.setShowGrid( true );

        titlePanel.setTitle( "No File Loaded" );
        titlePanel.setComponent( editor.getView() );

        this.add( titlePanel.getView(), new GridBagConstraints( 0, 0, 1, 1,
            1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        this.add( progressBar, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 4,
                4, 4, 4 ), 0, 0 ) );
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    private void loadBuffer() throws IOException
    {
        int bufLen = ( int )Math.min( maxBufferSize, fileLength - startOffset );
        byte[] buffer = new byte[bufLen];
        long nextOffset = startOffset + bufLen;
        double percent = ( double )nextOffset / ( double )fileLength * 100.0;

        // System.out.println( "Loading buffer @ " + startOffset + " , " +
        // percent + "%" );

        raFile.seek( startOffset );
        // TODO Check the results of read or use a different class to paginate.
        raFile.read( buffer );
        editor.setStartingAddress( startOffset );
        editor.setBuffer( new ByteBuffer( buffer ) );

        offsetLabel.setText( String.format(
            "Showing 0x%016X - 0x%016X of 0x%016X", startOffset, nextOffset,
            fileLength ) );

        // TODO create listener list to notify when buttons should be
        // dis/en-abled
        // nextButton.setEnabled( nextOffset < fileLength );
        // backButton.setEnabled( startOffset > 0 );
        progressBar.setValue( ( int )percent );
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    public void closeFile() throws IOException
    {
        if( raFile != null )
        {
            raFile.close();
            editor.setBuffer( null );
            raFile = null;
            startOffset = 0;
            fileLength = 0;
        }
    }

    public void jumpPrevious()
    {
        long lastOffset = startOffset - ( startOffset % maxBufferSize ) -
            maxBufferSize;
        lastOffset = Math.max( lastOffset, 0 );

        try
        {
            setStartOffset( lastOffset );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( HexEditorFilePanel.this,
                ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE );
        }
    }

    public void jumpForward()
    {
        long nextOffset = startOffset - ( startOffset % maxBufferSize ) +
            maxBufferSize;

        try
        {
            setStartOffset( nextOffset );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( HexEditorFilePanel.this,
                ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * @param size
     **************************************************************************/
    public void setBufferSize( int size )
    {
        this.maxBufferSize = size;
        if( currentFile != null )
        {
            try
            {
                setStartOffset( startOffset );
            }
            catch( IOException ex )
            {
                JOptionPane.showMessageDialog( this, ex.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    /***************************************************************************
     * @param offset
     * @throws IOException
     **************************************************************************/
    public void setStartOffset( long offset ) throws IOException
    {
        if( offset > -1 && offset < fileLength )
        {
            startOffset = offset;
            loadBuffer();
        }
    }

    /***************************************************************************
     * @param file
     * @throws IOException
     **************************************************************************/
    public void setFile( File file ) throws IOException
    {
        currentFile = file;
        if( raFile != null )
        {
            closeFile();
        }
        titlePanel.setTitle( file.getName() );
        raFile = new RandomAccessFile( file, "r" );
        fileLength = raFile.length();
        setStartOffset( 0 );
    }

    public void saveFile( File file ) throws IOException
    {
        if( file.compareTo( currentFile ) == 0 )
        {
            raFile.close();
        }

        FileOutputStream fileStream = new FileOutputStream( file );

        byte[] buffer = editor.getBuffer().getBytes();
        fileStream.write( buffer );
        fileStream.close();

        if( file.compareTo( currentFile ) == 0 )
        {
            setFile( file );
        }
    }
}
