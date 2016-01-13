package nmrc.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

import org.jutils.SwingUtils;
import org.jutils.io.LogUtils;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

import nmrc.alg.PreviousPeakMatcher;
import nmrc.data.AssignmentOption;
import nmrc.model.*;
import nmrc.ui.ChoicesDialog;
import nmrc.ui.PeakAssignerQuery;
import nmrc.ui.panels.AminoAcidsPanel;
import nmrc.ui.tables.models.AminoAcidTableModel;
import nmrc.ui.tables.models.PeakTableModel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AminoAcidsController
{
    /**  */
    private AminoAcidsPanel aaPanel;

    /***************************************************************************
     * @param panel
     **************************************************************************/
    public AminoAcidsController( AminoAcidsPanel panel )
    {
        aaPanel = panel;

        panel.addSaveListener( new SaveListener() );
        panel.addAssignListener( new AssignListener() );
        panel.addUnassignListener( new UnassignListener() );
        panel.addSortListener( new SortListener() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class SaveListener implements ItemActionListener<File>
    {
        @Override
        public void actionPerformed( ItemActionEvent<File> event )
        {
            // TODO Save the amino acids
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class SortListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            // TODO sort the peaks into the amino acids
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class AssignListener implements ItemActionListener<IAminoAcid>
    {
        @Override
        public void actionPerformed( ItemActionEvent<IAminoAcid> event )
        {
            IAminoAcid aminoAcid = event.getItem();
            INmrData nmrData = aaPanel.getData();
            List<IPeak> peaks = nmrData.getPeaks();
            List<IPeak> choices = new ArrayList<IPeak>( peaks );

            for( IAminoAcid aa : nmrData.getAminoAcids() )
            {
                if( aa.getPeak() != null )
                {
                    choices.remove( aa.getPeak() );
                }
            }

            AminoAcidTableModel baseModel = new AminoAcidTableModel();
            PeakTableModel choicesModel = new PeakTableModel();

            baseModel.addRow( aminoAcid );
            choicesModel.setItems( choices );

            ChoicesDialog<IPeak> dialog = new ChoicesDialog<IPeak>(
                SwingUtils.getComponentsFrame( aaPanel ), "", baseModel,
                choicesModel );

            dialog.setVisible( true );

            if( !dialog.isCancelled() )
            {
                IPeak choice = dialog.getChoice();
                if( choice != null )
                {
                    PeakToAminoAcidAssigner assigner = new PeakToAminoAcidAssigner(
                        choice, aminoAcid, nmrData );

                    assigner.assignPeakToAminoAcid( new PeakAssignerQuery(
                        SwingUtils.getComponentsFrame( aaPanel ) ) );

                    aaPanel.setData( nmrData );
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class UnassignListener implements ItemActionListener<IAminoAcid>
    {
        @Override
        public void actionPerformed( ItemActionEvent<IAminoAcid> event )
        {
            // TODO Unassign the peak
        }
    }
}

/*******************************************************************************
 * 
 ******************************************************************************/
class PeakToAminoAcidAssigner
{
    /**  */
    private IPeak peak;
    /**  */
    private IAminoAcid aa;
    /**  */
    private INmrData nmrData;

    /***************************************************************************
     * @param peak
     * @param aa
     * @param nmrData
     **************************************************************************/
    public PeakToAminoAcidAssigner( IPeak peak, IAminoAcid aa,
        INmrData nmrData )
    {
        this.peak = peak;
        this.aa = aa;
        this.nmrData = nmrData;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void assignPeakToAminoAcid( IPeakAssignerQuery query )
    {
        IAssignmentOption option = null;
        List<IPeak> chain = null;
        List<IAssignmentOption> options = null;

        // ---------------------------------------------------------------------
        // Build the peak chain.
        // ---------------------------------------------------------------------
        chain = getPeakChain();

        // ---------------------------------------------------------------------
        // Get the available options.
        // ---------------------------------------------------------------------
        options = getOptions( chain );

        // ---------------------------------------------------------------------
        // Build the peak chain
        // ---------------------------------------------------------------------
        if( options.isEmpty() )
        {
            query.displayError( "The choice is invalid." );
        }
        else if( options.size() == 1 )
        {
            option = query.confirmOption( nmrData.getAminoAcids(), chain,
                options.get( 0 ) );
        }
        else
        {
            option = query.chooseOption( nmrData.getAminoAcids(), chain,
                options );
        }

        if( option != null )
        {
            assignPeaksToAminoAcids( chain, nmrData.getAminoAcids(),
                option.getPeakStart(), option.getAminoAcidStart(),
                option.getLength() );
        }
    }

    /***************************************************************************
     * @param chain
     * @param peakIndex
     * @param aaIndex
     **************************************************************************/
    private List<IAssignmentOption> getOptions( List<IPeak> chain )
    {
        List<IAssignmentOption> options;

        List<IAminoAcid> aas = nmrData.getAminoAcids();

        int peakIndex = chain.indexOf( peak );
        int aaIndex = aas.indexOf( aa );

        int aaToPeak = peakIndex - aaIndex;

        IAssignmentOption mainOption = getMainOption( aaIndex, peakIndex,
            chain );

        // ---------------------------------------------------------------------
        // Build the full list of options.
        // ---------------------------------------------------------------------
        List<Integer> startPoints = getStartPoints( aas,
            mainOption.getAminoAcidStart(), aaIndex );
        List<Integer> endPoints = getEndPoints( aas, aaIndex,
            mainOption.getAminoAcidEnd() );

        options = getAllOptions( startPoints, endPoints, aaToPeak );
        options.add( 0, mainOption );

        // ---------------------------------------------------------------------
        // Remove the options that don't make sense.
        // ---------------------------------------------------------------------
        removeInvalidOptions( options, chain );

        return options;
    }

    /***************************************************************************
     * @param aaIndex
     * @param peakIndex
     * @param chain
     * @return
     **************************************************************************/
    private IAssignmentOption getMainOption( int aaIndex, int peakIndex,
        List<IPeak> chain )
    {
        int aaStartIndex = aaIndex - peakIndex;
        int aaEndIndex = aaStartIndex + chain.size();

        List<IAminoAcid> aas = nmrData.getAminoAcids();

        IAssignmentOption mainOption = new AssignmentOption( 0, aaStartIndex,
            chain.size() );

        // ---------------------------------------------------------------------
        // Test if the assigned chain will extend before the beginning.
        // ---------------------------------------------------------------------
        if( aaStartIndex < 0 )
        {
            mainOption.setAminoAcidStart( 0 );
            mainOption.setPeakStart( mainOption.getPeakStart() - aaStartIndex );
            mainOption.setLength( mainOption.getLength() + aaStartIndex );
        }

        // ---------------------------------------------------------------------
        // Test if the assigned chain will extend past the end.
        // ---------------------------------------------------------------------
        if( aaEndIndex > aas.size() )
        {
            int diff = aaEndIndex - aas.size();

            mainOption.setLength( mainOption.getLength() - diff );
        }

        return mainOption;
    }

    /***************************************************************************
     * Get the start points.
     * @return
     **************************************************************************/
    private List<Integer> getStartPoints( List<IAminoAcid> aas, int startIndex,
        int chosenIndex )
    {
        List<Integer> points = new ArrayList<Integer>();

        for( int i = startIndex + 1; i < chosenIndex; i++ )
        {
            IAminoAcid aa = aas.get( i );
            IAminoAcid lastAa = aas.get( i - 1 );

            if( aa.getPeak() == null && lastAa.getPeak() != null )
            {
                points.add( i + 1 );
            }
        }

        return points;
    }

    /***************************************************************************
     * Get the end points.
     * @return
     **************************************************************************/
    private List<Integer> getEndPoints( List<IAminoAcid> aas, int chosenIndex,
        int endIndex )
    {
        List<Integer> points = new ArrayList<Integer>();

        for( int i = endIndex - 2; i > chosenIndex; i-- )
        {
            IAminoAcid aa = aas.get( i );
            IAminoAcid nextAa = aas.get( i + 1 );

            if( aa.getPeak() == null && nextAa.getPeak() != null )
            {
                LogUtils.printDebug( "\tEnd Point: " + i );
                points.add( i );
            }
        }

        return points;
    }

    /***************************************************************************
     * @param startPoints
     * @param endPoints
     * @param aaToPeak
     * @return
     **************************************************************************/
    private List<IAssignmentOption> getAllOptions( List<Integer> startPoints,
        List<Integer> endPoints, int aaToPeak )
    {
        List<IAssignmentOption> options = new ArrayList<IAssignmentOption>();

        for( int start = 0; start < startPoints.size(); start++ )
        {
            for( int end = 0; end < endPoints.size(); end++ )
            {
                int startIndex = startPoints.get( start );
                int endIndex = endPoints.get( end );
                int length = endIndex - startIndex;

                options.add( new AssignmentOption( startIndex + aaToPeak,
                    startIndex, length ) );
            }
        }

        return options;
    }

    /***************************************************************************
     * @param options
     **************************************************************************/
    private void removeInvalidOptions( List<IAssignmentOption> options,
        List<IPeak> chain )
    {
        boolean hasStart;
        boolean hasEnd;

        IAminoAcid startAa;
        IAminoAcid endAa;

        IPeak startPeak;
        IPeak endPeak;

        for( int i = 0; i < options.size(); i++ )
        {
            IAssignmentOption option = options.get( i );

            hasStart = option.getAminoAcidStart() - 1 > -1;
            hasEnd = option.getAminoAcidEnd() < nmrData.getAminoAcids().size();

            startAa = hasStart
                ? nmrData.getAminoAcids().get( option.getAminoAcidStart() - 1 )
                : null;
            endAa = hasEnd
                ? nmrData.getAminoAcids().get( option.getAminoAcidEnd() )
                : null;

            startPeak = chain.get( option.getPeakStart() );
            endPeak = chain.get( option.getPeakEnd() - 1 );

            if( ( hasStart && !isValid( startPeak, startAa.getPeak() ) ) ||
                ( hasEnd && !isValid( endPeak, endAa.getPeak() ) ) )
            {
                options.remove( option );
            }
        }
    }

    /***************************************************************************
     * @param previous
     * @param next
     * @return
     **************************************************************************/
    private boolean isValid( IPeak previous, IPeak next )
    {
        boolean valid = true;

        if( previous != null && next != null )
        {
            PreviousPeakMatcher ppeq = new PreviousPeakMatcher( previous );

            valid = ppeq.matches( next );
        }

        return valid;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private List<IPeak> getPeakChain()
    {
        IPeak p = peak;
        List<IPeak> chain = new LinkedList<IPeak>();

        chain.add( peak );

        while( p.hasPrevious() )
        {
            p = p.getPrevious();
            if( chain.contains( p ) )
            {
                throw new IllegalStateException(
                    "Infinite chain because of peak " +
                        p.getRecord().getPeakName() );
            }
            chain.add( 0, p );
        }

        p = peak;
        boolean foundLast = false;
        while( !foundLast )
        {
            IPeak nextPeak = null;
            for( IPeak pk : nmrData.getPeaks() )
            {
                if( pk.hasPrevious() && pk.getPrevious() == p )
                {
                    nextPeak = pk;
                    break;
                }
            }

            if( nextPeak != null )
            {
                p = nextPeak;
                if( chain.contains( p ) )
                {
                    throw new IllegalStateException(
                        "Infinite chain because of peak " +
                            p.getRecord().getPeakName() );
                }
                chain.add( chain.size(), p );
            }
            else
            {
                foundLast = true;
            }
        }

        return chain;
    }

    /***************************************************************************
     * @param chain
     * @param aas
     * @param peakIndex
     * @param aaIndex
     * @param length
     **************************************************************************/
    private void assignPeaksToAminoAcids( List<IPeak> chain,
        List<IAminoAcid> aas, int peakIndex, int aaIndex, int length )
    {
        for( int i = 0; i < length; i++ )
        {
            aas.get( aaIndex + i ).setPeak( chain.get( peakIndex + i ) );
        }
    }
}
