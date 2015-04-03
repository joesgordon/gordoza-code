package org.jutils.ui.hex;

import java.awt.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.io.FileStream;
import org.jutils.io.IStream;
import org.jutils.ui.*;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.hex.HexTable.IRangeSelectedListener;
import org.jutils.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class HexEditorFilePanel implements IView<JPanel>
{
    /**  */
    private final JPanel view;

    /**  */
    private final PositionIndicator progressBar;
    /**  */
    private final JLabel offsetLabel;
    /**  */
    private final JPanel titleContent;
    /**  */
    private final TitleView titlePanel;
    /**  */
    private final HexPanel hexView;

    /**  */
    private long startOffset = 0;
    /**  */
    private long fileLength = 0;
    /**  */
    private int maxBufferSize = 1024 * 1024;
    /**  */
    private File currentFile = null;
    /**  */
    private IStream byteStream = null;

    /***************************************************************************
     * 
     **************************************************************************/
    public HexEditorFilePanel()
    {
        this.progressBar = new PositionIndicator();
        this.offsetLabel = new JLabel( "" );
        this.hexView = new HexPanel();
        this.titleContent = createTitleContent();
        this.titlePanel = new TitleView();
        this.view = new JPanel( new GridBagLayout() );

        // ---------------------------------------------------------------------
        // Setup main panel.
        // ---------------------------------------------------------------------
        titlePanel.setTitle( "No File Loaded" );
        titlePanel.setComponent( titleContent );

        view.add( createContentPanel(), new GridBagConstraints( 0, 0, 1, 1,
            1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createContentPanel()
    {
        titlePanel.setTitle( "No File Loaded" );
        titlePanel.setComponent( titleContent );
        titlePanel.getView().setBorder( new ShadowBorder() );

        return titlePanel.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createTitleContent()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        // ---------------------------------------------------------------------
        // Setup progress bar.
        // ---------------------------------------------------------------------
        progressBar.setLayout( new GridBagLayout() );
        progressBar.add( offsetLabel, new GridBagConstraints( 0, 0, 1, 1, 1.0,
            1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                2, 10, 2, 10 ), 0, 0 ) );

        progressBar.setLength( 100 );
        progressBar.setOffset( 0 );
        progressBar.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        progressBar.addPositionListener( new PositionChanged( this ) );

        // editor.setAlternateRowBG( true );
        // editor.setShowGrid( true );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 );
        panel.add( hexView.getView(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( new JSeparator(), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( progressBar, constraints );

        return panel;
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    private void loadBuffer() throws IOException
    {
        int bufLen = ( int )Math.min( maxBufferSize, fileLength - startOffset );
        byte [] buffer = new byte[bufLen];
        long nextOffset = startOffset + bufLen;

        // LogUtils.printDebug( "Loading buffer @ " + startOffset + " , " +
        // percent + "%" );

        byteStream.seek( startOffset );
        byteStream.readFully( buffer );
        hexView.setStartingAddress( startOffset );
        hexView.setBuffer( new ByteBuffer( buffer ) );

        offsetLabel.setText( String.format(
            "Showing 0x%016X - 0x%016X of 0x%016X", startOffset,
            nextOffset - 1, fileLength ) );

        // TODO create listener list to notify when buttons should be
        // dis/en-abled
        // nextButton.setEnabled( nextOffset < fileLength );
        // backButton.setEnabled( startOffset > 0 );
        progressBar.setOffset( startOffset );
        progressBar.setUnitLength( bufLen );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getSelectedColumn()
    {
        return hexView.getSelectedColumn();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getSelectedRow()
    {
        return hexView.getSelectedRow();
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addRangeSelectedListener( IRangeSelectedListener l )
    {
        hexView.addRangeSelectedListener( l );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IByteBuffer getBuffer()
    {
        return hexView.getBuffer();
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    public void closeFile() throws IOException
    {
        if( byteStream != null )
        {
            byteStream.close();
            byteStream = null;
            hexView.setBuffer( null );
            startOffset = 0;
            fileLength = 0;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void jumpPrevious()
    {
        long lastOffset = startOffset - ( startOffset % maxBufferSize ) -
            maxBufferSize;
        lastOffset = Math.max( lastOffset, 0 );

        try
        {
            setOffset( lastOffset );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( view, ex.getMessage(), "ERROR",
                JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void jumpForward()
    {
        long nextOffset = startOffset - ( startOffset % maxBufferSize ) +
            maxBufferSize;

        try
        {
            setOffset( nextOffset );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( view, ex.getMessage(), "ERROR",
                JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * @param c
     **************************************************************************/
    public void setHightlightColor( Color c )
    {
        hexView.setHightlightColor( c );
    }

    /***************************************************************************
     * @param length
     **************************************************************************/
    public void setHighlightLength( int length )
    {
        hexView.setHighlightLength( length );
    }

    /***************************************************************************
     * @param size
     **************************************************************************/
    public void setBufferSize( int size )
    {
        this.maxBufferSize = size;
        progressBar.setUnitLength( maxBufferSize );

        if( currentFile != null )
        {
            try
            {
                setOffset( startOffset );
            }
            catch( IOException ex )
            {
                JOptionPane.showMessageDialog( view, ex.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    /***************************************************************************
     * @param file
     * @throws IOException
     **************************************************************************/
    public void setFile( File file ) throws IOException
    {
        currentFile = file;
        if( byteStream != null )
        {
            closeFile();
        }
        titlePanel.setTitle( file.getName() );
        byteStream = new FileStream( file, true );
        fileLength = byteStream.getLength();
        setOffset( 0 );
        progressBar.setLength( fileLength );
        progressBar.setUnitLength( maxBufferSize );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IStream getStream()
    {
        return byteStream;
    }

    /***************************************************************************
     * @param file
     * @throws IOException
     **************************************************************************/
    public void saveFile( File file ) throws IOException
    {
        if( file.compareTo( currentFile ) == 0 )
        {
            byteStream.close();
        }

        try( FileOutputStream fileStream = new FileOutputStream( file ) )
        {
            byte [] buffer = hexView.getBuffer().getBytes();
            fileStream.write( buffer );
            fileStream.close();
        }

        if( file.compareTo( currentFile ) == 0 )
        {
            setFile( file );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JPanel getNonTitleView()
    {
        return titleContent;
    }

    /***************************************************************************
     * @param foundOffset
     **************************************************************************/
    public void highlightOffset( long offset, int length )
    {
        try
        {
            long blockOffset = getBlockStart( offset );
            setOffset( blockOffset );

            int startIndex = ( int )( offset - blockOffset );
            int endIndex = startIndex + length - 1;

            hexView.setSelected( startIndex, endIndex );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( getView(), ex.getMessage(), "ERROR",
                JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * @param offset
     * @return
     **************************************************************************/
    private long getBlockStart( long offset )
    {
        long blockCount = offset / maxBufferSize;

        return blockCount * maxBufferSize;
    }

    /***************************************************************************
     * @param offset
     * @throws IOException
     **************************************************************************/
    private void setOffset( long offset ) throws IOException
    {
        if( offset > -1 && offset < fileLength )
        {
            startOffset = offset;
            loadBuffer();
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getSelectedOffset()
    {
        int index = hexView.getSelectedByte();
        long offset = -1;

        if( index > -1 )
        {
            offset = startOffset + index;
        }

        return offset;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class PositionChanged implements ItemActionListener<Long>
    {
        private final HexEditorFilePanel view;

        public PositionChanged( HexEditorFilePanel view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<Long> event )
        {
            long pos = ( event.getItem() / view.maxBufferSize ) *
                view.maxBufferSize;
            try
            {
                view.setOffset( pos );
            }
            catch( IOException ex )
            {
                JOptionPane.showMessageDialog( view.view, ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE );
            }
        }
    }
}
