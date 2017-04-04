package org.jutils.apps.jhex.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.io.IStream;
import org.jutils.ui.*;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.hex.*;
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
    private final TitleView fileTitleView;
    /**  */
    private final HexPanel hexView;
    /**  */
    private final TitleView dataTitleView;
    /**
     * The view to show the data in different formats starting at the currently
     * selected byte.
     */
    private final ValueView valuePanel;

    private final BlockBuffer buffer;

    /***************************************************************************
     * 
     **************************************************************************/
    public HexFileView()
    {
        this.progressBar = new PositionIndicator();
        this.offsetLabel = new JLabel( "" );
        this.hexView = new HexPanel();
        this.fileTitleView = new TitleView( "No File Loaded", createContent() );
        this.valuePanel = new ValueView();
        this.dataTitleView = new TitleView( "Value Selected",
            valuePanel.getView() );
        this.view = createView();

        this.buffer = new BlockBuffer();

        valuePanel.addSizeSelectedListener( new SizeSelectedListener( this ) );
        addRangeSelectedListener( new SelectionListener( this ) );
    }

    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        fileTitleView.getView().setBorder( new ShadowBorder() );

        fileTitleView.getView().setMinimumSize( new Dimension( 500, 100 ) );
        dataTitleView.getView().setMinimumSize(
            dataTitleView.getView().getPreferredSize() );

        panel.add( fileTitleView.getView(),
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );
        panel.add( dataTitleView.getView(),
            new GridBagConstraints( 1, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createContent()
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
        progressBar.addPositionListener(
            ( e ) -> updatePosition( e.getItem() ) );

        // editor.setAlternateRowBG( true );
        // editor.setShowGrid( true );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createToolbar(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( new JSeparator(), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( hexView.getView(), constraints );

        constraints = new GridBagConstraints( 0, 3, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( new JSeparator(), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( progressBar, constraints );

        return panel;
    }

    private Component createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        JToggleButton jtb = new JToggleButton();
        jtb.setIcon( IconConstants.getIcon( IconConstants.SHOW_DATA ) );
        jtb.setToolTipText( "Show Data" );
        jtb.setFocusable( false );
        jtb.setSelected( true );
        jtb.addActionListener( new ShowDataListener( this, jtb ) );
        toolbar.add( jtb );

        return toolbar;
    }

    private void updatePosition( long position )
    {
        long pos = buffer.getBufferStart( position );

        try
        {
            loadBuffer( pos );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( getView(), ex.getMessage(), "ERROR",
                JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * @param position
     * @throws IOException
     **************************************************************************/
    private void loadBuffer( long position ) throws IOException
    {
        if( position < 0 || position >= buffer.getLength() ||
            buffer.isLoaded( position ) )
        {
            return;
        }

        DataBlock block = buffer.loadBufferAt( position );

        hexView.setStartingAddress( position );
        hexView.setBuffer( new ByteBuffer( block.buffer ) );
        long nextOffset = position + block.buffer.length;

        offsetLabel.setText(
            String.format( "Showing 0x%016X - 0x%016X of 0x%016X", position,
                nextOffset - 1, buffer.getLength() ) );

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

    public void jumpFirst()
    {
        jump( 0L );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void jumpPrevious()
    {
        jump( buffer.getPreviousPosition() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void jumpForward()
    {
        jump( buffer.getNextPosition() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void jumpLast()
    {
        jump( buffer.getLastPosition() );
    }

    /***************************************************************************
     * @param position
     **************************************************************************/
    private void jump( long position )
    {
        try
        {
            loadBuffer( position );
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
        long pos = buffer.setBufferSize( size );

        progressBar.setUnitLength( size );

        if( buffer.isOpen() )
        {
            try
            {
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
        return buffer.getFile();
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

        fileTitleView.setTitle( file.getName() );
        loadBuffer( 0 );
        progressBar.setLength( buffer.getLength() );
        progressBar.setUnitLength( buffer.getBufferSize() );
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
            offset = buffer.getPositionAt( index );
        }

        return offset;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isOpen()
    {
        return buffer.isOpen();
    }

    /***************************************************************************
     * @return
     * @throws FileNotFoundException
     **************************************************************************/
    public IStream openStreamCopy() throws FileNotFoundException
    {
        return buffer.openStreamCopy();
    }

    /***************************************************************************
     * Action listener for displaying the data dialog.
     **************************************************************************/
    private static class ShowDataListener implements ActionListener
    {
        private final HexFileView view;
        private final JToggleButton button;

        public ShowDataListener( HexFileView view, JToggleButton jtb )
        {
            this.view = view;
            this.button = jtb;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            view.dataTitleView.getView().setVisible( button.isSelected() );

            if( button.isSelected() )
            {
                view.setHighlightLength( view.valuePanel.getSelectedSize() );
            }
            else
            {
                view.setHighlightLength( -1 );
            }
        }
    }

    /***************************************************************************
     * Listener to update the buffer size.
     **************************************************************************/
    private static class SizeSelectedListener
        implements ItemActionListener<Integer>
    {
        private final HexFileView view;

        public SizeSelectedListener( HexFileView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<Integer> event )
        {
            int len = event.getItem() == null ? -1 : event.getItem();

            view.setHighlightLength( len );
        }
    }

    /***************************************************************************
     * Listener to update the data dialog as bytes are selected.
     **************************************************************************/
    private static class SelectionListener implements IRangeSelectedListener
    {
        private final HexFileView view;

        public SelectionListener( HexFileView view )
        {
            this.view = view;
        }

        @Override
        public void rangeSelected( int start, int end )
        {
            view.valuePanel.setBytes( view.getBuffer().getBytes(), end );

            // LogUtils.printDebug( "col: " + col + ", row: " + row +
            // ", start: "
            // +
            // start + ", end: " + end );
        }
    }
}
