package org.jutils.ui.hex;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.SwingUtils;
import org.jutils.io.IStream;
import org.jutils.ui.*;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.hex.BlockBuffer.DataBlock;
import org.jutils.ui.hex.HexTable.IRangeSelectedListener;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 *
 ******************************************************************************/
public class HexFileView implements IDataView<File>
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

    private final BlockBuffer buffer;

    /***************************************************************************
     * 
     **************************************************************************/
    public HexFileView()
    {
        this.progressBar = new PositionIndicator();
        this.offsetLabel = new JLabel( "" );
        this.hexView = new HexPanel();
        this.titleContent = createTitleContent();
        this.titlePanel = new TitleView();
        this.view = new JPanel( new GridBagLayout() );

        this.buffer = new BlockBuffer();

        // ---------------------------------------------------------------------
        // Setup main panel.
        // ---------------------------------------------------------------------
        titlePanel.setTitle( "No File Loaded" );
        titlePanel.setComponent( titleContent );

        view.add( createContentPanel(),
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
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
        progressBar.add( offsetLabel,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 10, 2, 10 ), 0, 0 ) );

        progressBar.setLength( 100 );
        progressBar.setOffset( 0 );
        progressBar.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        progressBar.addPositionListener( new PositionChanged( this ) );

        // editor.setAlternateRowBG( true );
        // editor.setShowGrid( true );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
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
     * @param position
     * @throws IOException
     **************************************************************************/
    private void loadBuffer( long position ) throws IOException
    {
        if( position < 0 || position >= buffer.fileLength )
        {
            return;
        }

        DataBlock block = buffer.loadBufferAt( position );

        hexView.setStartingAddress( position );
        hexView.setBuffer( new ByteBuffer( block.buffer ) );
        long nextOffset = position + block.buffer.length;

        offsetLabel.setText(
            String.format( "Showing 0x%016X - 0x%016X of 0x%016X", position,
                nextOffset - 1, buffer.fileLength ) );

        // TODO create listener list to notify when buttons should be
        // dis/en-abled
        // nextButton.setEnabled( nextOffset < fileLength );
        // backButton.setEnabled( position > 0 );
        progressBar.setOffset( position );
        progressBar.setUnitLength( block.buffer.length );
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
     * 
     **************************************************************************/
    public void jumpPrevious()
    {
        long lastOffset = buffer.getPreviousPosition();
        lastOffset = Math.max( lastOffset, 0 );

        try
        {
            loadBuffer( lastOffset );
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
        long nextOffset = buffer.getNextPosition();

        try
        {
            loadBuffer( nextOffset );
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
        buffer.maxBufferSize = size;
        progressBar.setUnitLength( size );

        if( buffer.isOpen() )
        {
            try
            {
                long pos = buffer.getBufferStart( buffer.startOffset );
                loadBuffer( pos );
            }
            catch( IOException ex )
            {
                JOptionPane.showMessageDialog( view, ex.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public File getData()
    {
        return buffer.currentFile;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( File data )
    {
        try
        {
            openFile( data );
        }
        catch( IOException ex )
        {
            SwingUtils.showErrorMessage( getView(), ex.getMessage(),
                "I/O Error" );
        }
    }

    /***************************************************************************
     * @param file
     * @throws IOException
     **************************************************************************/
    public void openFile( File file ) throws IOException
    {
        buffer.openFile( file );

        titlePanel.setTitle( file.getName() );
        loadBuffer( 0 );
        progressBar.setLength( buffer.fileLength );
        progressBar.setUnitLength( buffer.maxBufferSize );
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    public void closeFile() throws IOException
    {
        buffer.closeFile();

        hexView.setBuffer( null );
    }

    /***************************************************************************
     * @param file
     * @throws IOException
     **************************************************************************/
    public void saveFile( File file ) throws IOException
    {
        // TODO fix saving of files.
        // if( file.compareTo( currentFile ) == 0 )
        // {
        // byteStream.close();
        // }
        //
        // try( FileOutputStream fileStream = new FileOutputStream( file ) )
        // {
        // byte [] buffer = hexView.getBuffer().getBytes();
        // fileStream.write( buffer );
        // fileStream.close();
        // }
        //
        // if( file.compareTo( currentFile ) == 0 )
        // {
        // openFile( file );
        // }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IStream getStream()
    {
        return buffer.byteStream;
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
            long blockOffset = buffer.getBlockStart( offset );
            loadBuffer( blockOffset );

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
     * @return
     **************************************************************************/
    public long getSelectedOffset()
    {
        int index = hexView.getSelectedByte();
        long offset = -1;

        if( index > -1 )
        {
            offset = buffer.startOffset + index;
        }

        return offset;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class PositionChanged implements ItemActionListener<Long>
    {
        private final HexFileView view;

        public PositionChanged( HexFileView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<Long> event )
        {
            long pos = view.buffer.getBufferStart( event.getItem() );

            try
            {
                view.loadBuffer( pos );
            }
            catch( IOException ex )
            {
                JOptionPane.showMessageDialog( view.view, ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE );
            }
        }
    }
}
