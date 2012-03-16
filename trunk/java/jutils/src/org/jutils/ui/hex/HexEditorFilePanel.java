package org.jutils.ui.hex;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;

import org.jutils.IconConstants;

/*******************************************************************************
 *
 ******************************************************************************/
public class HexEditorFilePanel extends JPanel
{
    /**  */
    private JProgressBar progressBar = new JProgressBar();
    /**  */
    private JLabel descLabel = new JLabel( "File not loaded" );
    /**  */
    private JLabel offsetLabel = new JLabel( "" );
    /**  */
    private JButton backButton = new JButton();
    /**  */
    private JButton nextButton = new JButton();
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
        progressBar.add( descLabel, new GridBagConstraints( 0, 0, 1, 1, 1.0,
            1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                2, 10, 0, 10 ), 0, 0 ) );
        progressBar.add( offsetLabel, new GridBagConstraints( 0, 1, 1, 1, 1.0,
            1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                0, 10, 2, 10 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Setup button panel.
        // ---------------------------------------------------------------------
        JPanel buttonPanel = new JPanel();
        ActionListener nextListener = new NextListener();
        ActionListener backListener = new BackListener();

        buttonPanel.setLayout( new GridBagLayout() );

        progressBar.setStringPainted( true );
        progressBar.setString( "" );
        progressBar.setIndeterminate( false );
        progressBar.setMaximum( 100 );
        progressBar.setMinimum( 0 );
        progressBar.setValue( 0 );

        backButton.setEnabled( false );
        backButton.setIcon( IconConstants.getIcon( IconConstants.BACK_24 ) );
        backButton.addActionListener( backListener );

        nextButton.setEnabled( false );
        nextButton.setIcon( IconConstants.getIcon( IconConstants.FORWARD_24 ) );
        nextButton.addActionListener( nextListener );

        buttonPanel.add( progressBar, new GridBagConstraints( 0, 0, 1, 1, 1.0,
            1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(
                0, 0, 0, 10 ), 0, 4 ) );
        buttonPanel.add( backButton, new GridBagConstraints( 1, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 10 ), 0, 0 ) );
        buttonPanel.add( nextButton, new GridBagConstraints( 2, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 10, 0, 0 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Setup main panel.
        // ---------------------------------------------------------------------
        this.setLayout( new GridBagLayout() );

        // editor.setAlternateRowBG( true );
        // editor.setShowGrid( true );
        // editor.setBorder( BorderFactory.createLoweredBevelBorder() );

        this.add( buttonPanel, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 4,
                4, 4, 4 ), 0, 0 ) );

        this.add( editor.getView(), new GridBagConstraints( 0, 1, 1, 1, 1.0,
            1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );
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
        // editor.setStartOffset( startOffset );

        offsetLabel.setText( "Showing 0x" + Long.toHexString( startOffset ) +
            " - 0x" + Long.toHexString( nextOffset ) + " of 0x" +
            Long.toHexString( fileLength ) );

        nextButton.setEnabled( nextOffset < fileLength );
        backButton.setEnabled( startOffset > 0 );
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
            descLabel.setText( "File not loaded" );
            startOffset = 0;
            fileLength = 0;
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
        descLabel.setText( file.getName() );
        descLabel.setToolTipText( file.getAbsolutePath() );
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

    /***************************************************************************
     * 
     **************************************************************************/
    private class BackListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
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
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class NextListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
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
    }
}
