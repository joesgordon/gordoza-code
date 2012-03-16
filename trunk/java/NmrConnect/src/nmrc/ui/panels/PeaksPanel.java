package nmrc.ui.panels;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nmrc.model.IPeak;
import nmrc.ui.tables.ItemTable;
import nmrc.ui.tables.models.PeakTableModel;

import org.jutils.IconConstants;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.ItemTableModel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PeaksPanel extends JPanel
{
    /**  */
    private ItemTable<IPeak> peakTable;
    /**  */
    private JButton sortButton;
    /**  */
    private JButton findButton;
    /**  */
    private JButton assignButton;
    /**  */
    private JButton unassignButton;
    /**  */
    private JButton previousButton;
    /**  */
    private JButton nextButton;
    /**  */
    private List<IPeak> peaks;

    /**  */
    private ItemActionList<IPeak> assignListeners;
    /**  */
    private ItemActionList<IPeak> unassignListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public PeaksPanel()
    {
        super();

        assignListeners = new ItemActionList<IPeak>();
        unassignListeners = new ItemActionList<IPeak>();

        // ---------------------------------------------------------------------
        // Setup peak scroll pane.
        // ---------------------------------------------------------------------
        peakTable = new ItemTable<IPeak>( new PeakTableModel() );
        JScrollPane peakScrollPane = new JScrollPane( peakTable );

        peakTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

        peakTable.getSelectionModel().addListSelectionListener(
            new TableSelectedListener() );

        // ---------------------------------------------------------------------
        // 
        // ---------------------------------------------------------------------
        setLayout( new BorderLayout() );

        add( getToolbar(), BorderLayout.NORTH );
        add( peakScrollPane, BorderLayout.CENTER );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar getToolbar()
    {
        JToolBar toolbar = new JToolBar();

        sortButton = new JButton(
            IconConstants.getIcon( IconConstants.SORT_DOWN_16 ) );
        findButton = new JButton(
            IconConstants.getIcon( IconConstants.PAGEMAG_16 ) );
        assignButton = new JButton(
            IconConstants.getIcon( IconConstants.EDIT_ADD_16 ) );
        unassignButton = new JButton(
            IconConstants.getIcon( IconConstants.EDIT_DELETE_16 ) );
        previousButton = new JButton(
            IconConstants.getIcon( IconConstants.BACK_16 ) );
        nextButton = new JButton(
            IconConstants.getIcon( IconConstants.FORWARD_16 ) );

        sortButton.setFocusable( false );
        sortButton.setEnabled( false );
        sortButton.setToolTipText( "Find all previous peaks" );

        findButton.setFocusable( false );
        findButton.setEnabled( false );
        findButton.setToolTipText( "Search for previous peak" );

        assignButton.setFocusable( false );
        assignButton.setEnabled( false );
        assignButton.setToolTipText( "Assign previous" );
        assignButton.addActionListener( new AssignListener() );

        unassignButton.setFocusable( false );
        unassignButton.setEnabled( false );
        unassignButton.setToolTipText( "Unassign previous" );
        unassignButton.addActionListener( new UnassignListener() );

        previousButton.setFocusable( false );
        previousButton.setEnabled( false );
        previousButton.setToolTipText( "Jump to previous" );
        previousButton.addActionListener( new PreviousListener() );

        nextButton.setFocusable( false );
        nextButton.setEnabled( false );
        nextButton.setToolTipText( "Jump to next" );
        nextButton.addActionListener( new NextListener() );

        toolbar.add( sortButton );
        toolbar.add( findButton );
        toolbar.add( assignButton );
        toolbar.add( unassignButton );
        toolbar.add( previousButton );
        toolbar.add( nextButton );

        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        return toolbar;
    }

    /***************************************************************************
     * @param peaks
     **************************************************************************/
    public void setData( List<IPeak> peaks )
    {
        this.peaks = peaks;

        peakTable.getTableModel().setItems( peaks );
        peakTable.getTableModel().setColumnNames(
            Arrays.asList( PeakTableModel.COL_NAMES ) );

        sortButton.setEnabled( true );
        findButton.setEnabled( true );
        assignButton.setEnabled( true );
        unassignButton.setEnabled( true );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public List<IPeak> getData()
    {
        return peaks;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IPeak getSelectedPeak()
    {
        IPeak peak = null;
        int row = peakTable.getSelectedRow();

        if( row > -1 )
        {
            peak = peakTable.getTableModel().getRow( row );
        }

        return peak;
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addSortPeaksListener( ActionListener l )
    {
        sortButton.addActionListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addFindPreviousListener( ActionListener l )
    {
        findButton.addActionListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addAssignListener( ItemActionListener<IPeak> l )
    {
        assignListeners.addListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addUnassignListener( ItemActionListener<IPeak> l )
    {
        unassignListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class AssignListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            int row = peakTable.getSelectedRow();
            if( row > -1 )
            {
                IPeak selectedPeak = peakTable.getTableModel().getRow( row );
                assignListeners.fireListeners( this, selectedPeak );
            }
        }
    }

    private class PreviousListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            ItemTableModel<IPeak> model = peakTable.getTableModel();
            int row = peakTable.getSelectedRow();
            IPeak peak = row > -1 ? model.getRow( row ) : null;

            if( peak != null )
            {
                peak = peak.getPrevious();

                selectAndScrollToPeak( peak );
            }
        }
    }

    private int findIndexByName( String name )
    {
        ItemTableModel<IPeak> model = peakTable.getTableModel();
        String peakName;

        for( int row = 0; row < model.getItems().size(); row++ )
        {
            peakName = model.getItems().get( row ).getRecord().getPeakName();
            if( name.compareToIgnoreCase( peakName ) == 0 )
            {
                return row;
            }
        }

        return -1;
    }

    private void selectAndScrollToPeak( IPeak peak )
    {
        int row = findIndexByName( peak.getRecord().getPeakName() );

        peakTable.getSelectionModel().setSelectionInterval( row, row );
        Rectangle r = peakTable.getCellRect( row, 0, true );
        peakTable.scrollRectToVisible( r );
    }

    private class NextListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            ItemTableModel<IPeak> model = peakTable.getTableModel();
            int row = peakTable.getSelectedRow();
            IPeak peak = row > -1 ? model.getRow( row ) : null;

            if( peak != null )
            {
                String peakName = peak.getRecord().getPeakName();
                String prevName;

                for( IPeak p : model.getItems() )
                {
                    prevName = p.getPrevious().getRecord().getPeakName();
                    if( prevName.compareTo( peakName ) == 0 )
                    {
                        selectAndScrollToPeak( p );
                        break;
                    }
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class TableSelectedListener implements ListSelectionListener
    {
        @Override
        public void valueChanged( ListSelectionEvent e )
        {
            int row = peakTable.getSelectedRow();

            if( row > -1 )
            {
                IPeak p = peakTable.getTableModel().getRow( row );

                previousButton.setEnabled( p.hasPrevious() );
                nextButton.setEnabled( p.isPrevious() );
            }
            else
            {
                previousButton.setEnabled( false );
                nextButton.setEnabled( true );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class UnassignListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            int row = peakTable.getSelectedRow();
            if( row > -1 )
            {
                IPeak selectedPeak = peakTable.getTableModel().getRow( row );
                unassignListeners.fireListeners( this, selectedPeak );
            }
        }
    }
}
