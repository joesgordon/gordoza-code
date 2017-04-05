package org.jutils.apps.jhex.ui;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.apps.jhex.JHexIcons;
import org.jutils.apps.jhex.task.DataDistributionTask;
import org.jutils.apps.jhex.task.SearchTask;
import org.jutils.chart.ChartIcons;
import org.jutils.datadist.DataDistribution;
import org.jutils.io.IStream;
import org.jutils.task.TaskView;
import org.jutils.ui.*;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.event.*;
import org.jutils.ui.fields.HexBytesFormField;
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

    /**  */
    public final Action prevAction;
    /**  */
    public final Action nextAction;
    /**  */
    public final Action searchAction;
    /**  */
    public final Action gotoAction;
    /**  */
    public final Action analyzeAction;
    /**  */
    public final Action plotAction;

    /**  */
    private final BlockBuffer buffer;

    /**  */
    private byte [] lastSearch;

    /***************************************************************************
     * 
     **************************************************************************/
    public HexFileView()
    {
        this.prevAction = new ActionAdapter( ( e ) -> jumpPrevious(),
            "Previous Data Block", JHexIcons.getIcon( JHexIcons.JUMP_LEFT ) );
        this.nextAction = new ActionAdapter( ( e ) -> jumpForward(),
            "Next Data Block", JHexIcons.getIcon( JHexIcons.JUMP_RIGHT ) );

        this.searchAction = new ActionAdapter( ( e ) -> showSearchDialog(),
            "Search", IconConstants.getIcon( IconConstants.FIND_16 ) );
        this.gotoAction = new ActionAdapter( ( e ) -> showGotoDialog(),
            "Go To Byte", JHexIcons.loader.getIcon( JHexIcons.GOTO ) );

        this.analyzeAction = new ActionAdapter( ( e ) -> showAnalyzer(),
            "Analyze", IconConstants.getIcon( IconConstants.ANALYZE_16 ) );
        this.plotAction = new ActionAdapter( ( e ) -> showPlot(), "Plot",
            ChartIcons.getIcon( ChartIcons.CHART_016 ) );

        this.progressBar = new PositionIndicator();
        this.offsetLabel = new JLabel( "" );
        this.hexView = new HexPanel();
        this.fileTitleView = new TitleView( "No File Loaded", createContent() );
        this.valuePanel = new ValueView();
        this.dataTitleView = new TitleView( "Value Selected",
            valuePanel.getView() );

        this.view = createView();

        this.buffer = new BlockBuffer();
        this.lastSearch = null;

        KeyStroke key;
        Action action;
        InputMap inMap = view.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW );
        ActionMap acMap = view.getActionMap();

        key = KeyStroke.getKeyStroke( "control F" );
        searchAction.putValue( Action.ACCELERATOR_KEY, key );
        searchAction.putValue( Action.MNEMONIC_KEY, ( int )'F' );

        action = new ActionAdapter( new FindAgainListener( this ), "Find Next",
            null );
        key = KeyStroke.getKeyStroke( "F3" );
        action.putValue( Action.ACCELERATOR_KEY, key );
        inMap.put( key, "findNextAction" );
        acMap.put( "findNextAction", action );

        action = new ActionAdapter( new FindAgainListener( this, false ),
            "Find Previous", null );
        key = KeyStroke.getKeyStroke( "shift F3" );
        action.putValue( Action.ACCELERATOR_KEY, key );
        inMap.put( key, "findPrevAction" );
        acMap.put( "findPrevAction", action );

        prevAction.setEnabled( false );
        nextAction.setEnabled( false );
        searchAction.setEnabled( false );
        gotoAction.setEnabled( false );
        analyzeAction.setEnabled( false );
        plotAction.setEnabled( false );

        valuePanel.addSizeSelectedListener( new SizeSelectedListener( this ) );
        addRangeSelectedListener( new SelectionListener( this ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        fileTitleView.getView().setBorder( new ShadowBorder() );
        fileTitleView.getView().setMinimumSize( new Dimension( 500, 100 ) );

        dataTitleView.getView().setBorder( new ShadowBorder() );
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

        // setAlternateRowBG( true );
        // setShowGrid( true );

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

        constraints = new GridBagConstraints( 0, 4, 1, 1, 1.0, 0.0,
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

        SwingUtils.addActionToToolbar( toolbar, prevAction );

        // SwingUtils.addActionToToolbar( toolbar, new ActionAdapter( null,
        // "Previous Data", JHexIcons.getIcon( JHexIcons.INCH_LEFT ) ) );
        //
        // SwingUtils.addActionToToolbar( toolbar, new ActionAdapter( null,
        // "Next Data", JHexIcons.getIcon( JHexIcons.INCH_RIGHT ) ) );

        SwingUtils.addActionToToolbar( toolbar, nextAction );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, searchAction );
        SwingUtils.addActionToToolbar( toolbar, gotoAction );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, analyzeAction );
        SwingUtils.addActionToToolbar( toolbar, plotAction );

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
     * Displays the dialog that allows the user to go to a particular offset
     * into the file.
     **************************************************************************/
    private void showGotoDialog()
    {
        Object ans = JOptionPane.showInputDialog( getView(),
            "Enter Offset in hexadecimal:", new Integer( 0 ) );
        if( ans != null )
        {
            try
            {
                long offset = Long.parseLong( ans.toString(), 16 );
                highlightOffset( offset, 1 );
            }
            catch( NumberFormatException ex )
            {
                JOptionPane.showMessageDialog( getView(),
                    "'" + ans.toString() + "' is not a hexadecimal string.",
                    "ERROR", JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showPlot()
    {
        try( IStream stream = openStreamCopy() )
        {
            if( stream == null )
            {
                return;
            }

            Window w = SwingUtils.getComponentsWindow( getView() );
            DataPlotView plotView = new DataPlotView( stream );
            OkDialogView dialogView = new OkDialogView( w, plotView.getView(),
                ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_ONLY );

            dialogView.setOkButtonText( "Close" );

            dialogView.show( "Data Plot", JHexIcons.getAppImages(),
                new Dimension( 640, 480 ) );
        }
        catch( FileNotFoundException ex )
        {
            SwingUtils.showErrorMessage(
                getView(), "Unable to open file: " +
                    getData().getAbsolutePath() + " because " + ex.getMessage(),
                "File Not Found Error" );
        }
        catch( IOException ex )
        {
            SwingUtils.showErrorMessage(
                getView(), "Unable to read file: " +
                    getData().getAbsolutePath() + " because " + ex.getMessage(),
                "I/O Error" );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showAnalyzer()
    {
        try( IStream stream = openStreamCopy() )
        {
            if( stream == null )
            {
                return;
            }

            DataDistributionTask ddt = new DataDistributionTask( stream );

            TaskView.startAndShow( getView(), ddt, "Analyzing Data" );

            DataDistribution dist = ddt.getDistribution();

            if( dist != null )
            {
                VerboseMessageView msgView = new VerboseMessageView();

                msgView.setMessages( "Finished Analyzing",
                    dist.getDescription() );

                msgView.show( getView(), "Finished Analyzing" );
            }
        }
        catch( FileNotFoundException ex )
        {
            SwingUtils.showErrorMessage(
                getView(), "Unable to open file: " +
                    getData().getAbsolutePath() + " because " + ex.getMessage(),
                "File Not Found Error" );
        }
        catch( IOException ex )
        {
            SwingUtils.showErrorMessage(
                getView(), "Unable to read file: " +
                    getData().getAbsolutePath() + " because " + ex.getMessage(),
                "I/O Error" );
        }
    }

    /***************************************************************************
     * Displays the dialog that allows the user to enter bytes to be found.
     **************************************************************************/
    private void showSearchDialog()
    {
        if( !isOpen() )
        {
            return;
        }

        HexBytesFormField hexField = new HexBytesFormField( "Hex Bytes" );
        StandardFormView form = new StandardFormView( true );

        form.addField( hexField.getName(), hexField.getView() );

        hexField.getTextField().addAncestorListener(
            new RequestFocusListener() );

        int ans = JOptionPane.showOptionDialog( getView(), form.getView(),
            "Enter Hexadecimal String", JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, null, null );

        if( ans == JOptionPane.OK_OPTION )
        {
            if( !hexField.getValidity().isValid )
            {
                JOptionPane.showMessageDialog( getView(),
                    hexField.getValidity().reason, "Invalid Hexadecimal Entry",
                    JOptionPane.ERROR_MESSAGE );
                return;
            }

            byte [] bytes = hexField.getValue();
            long fromOffset = getSelectedOffset();

            fromOffset = fromOffset > -1 ? fromOffset : 0;

            search( bytes, fromOffset );
        }
        // else
        // {
        // LogUtils.printDebug( "cancelled" );
        // }
    }

    /***************************************************************************
     * @param bytes
     * @param fromOffset
     **************************************************************************/
    private void search( byte [] bytes, long fromOffset )
    {
        search( bytes, fromOffset, true );
    }

    /***************************************************************************
     * @param bytes
     * @param fromOffset
     * @param isForward
     **************************************************************************/
    private void search( byte [] bytes, long fromOffset, boolean isForward )
    {
        this.lastSearch = bytes;

        // LogUtils.printDebug( "Searching for: " + HexUtils.toHexString( bytes
        // ) +
        // " @ " + fromOffset + " " + ( isForward ? "Forward" : "Backward" ) );

        try( IStream stream = openStreamCopy() )
        {
            if( stream == null )
            {
                return;
            }

            SearchTask task = new SearchTask( bytes, stream, fromOffset,
                isForward );

            TaskView.startAndShow( getView(), task, "Byte Search" );

            long foundOffset = task.foundOffset;

            if( foundOffset > -1 )
            {
                highlightOffset( foundOffset, bytes.length );
            }
        }
        catch( FileNotFoundException ex )
        {
            SwingUtils.showErrorMessage(
                getView(), "Unable to open file: " +
                    getData().getAbsolutePath() + " because " + ex.getMessage(),
                "File Not Found Error" );
        }
        catch( IOException ex )
        {
            SwingUtils.showErrorMessage(
                getView(), "Unable to read file: " +
                    getData().getAbsolutePath() + " because " + ex.getMessage(),
                "I/O Error" );
        }
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
        finally
        {
            boolean enabled = isOpen();

            prevAction.setEnabled( enabled );
            nextAction.setEnabled( enabled );

            searchAction.setEnabled( enabled );
            gotoAction.setEnabled( enabled );

            analyzeAction.setEnabled( enabled );
            plotAction.setEnabled( enabled );
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

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FindAgainListener implements ActionListener
    {
        private final HexFileView view;
        private final boolean isForward;

        public FindAgainListener( HexFileView view )
        {
            this( view, true );
        }

        public FindAgainListener( HexFileView view, boolean forward )
        {
            this.view = view;
            this.isForward = forward;
        }

        @Override
        public synchronized void actionPerformed( ActionEvent e )
        {
            if( view.lastSearch != null )
            {
                long off = view.getSelectedOffset();
                off = off + ( isForward ? 1 : -1 );

                // LogUtils.printDebug( "Searching for: " +
                // HexUtils.toHexString( view.lastSearch ) + " @ " +
                // String.format( "%016X", off ) + " " +
                // ( isForward ? "Forward" : "Backward" ) );

                view.search( view.lastSearch, off, isForward );
            }
        }
    }
}
