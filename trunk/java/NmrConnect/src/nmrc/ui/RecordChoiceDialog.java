package nmrc.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import javax.swing.*;

import nmrc.model.IPeak;
import nmrc.ui.tables.ItemTable;
import nmrc.ui.tables.models.PeakRecordTableModel;

/**
 *
 */
public class RecordChoiceDialog extends JDialog
{
    // -------------------------------------------------------------------------
    // Widgets
    // -------------------------------------------------------------------------
    /**  */
    private ItemTable<IPeak> dotTable;
    /**  */
    private JButton chooseButton;
    /**  */
    private JButton noneButton;

    // -------------------------------------------------------------------------
    // Supporting data.
    // -------------------------------------------------------------------------
    /**  */
    private List<IPeak> choices;
    /**  */
    private boolean cancelled;

    /***************************************************************************
     * @param owner Frame
     * @param dots Vector
     * @param peak Dot
     * @param heading DotHeading
     **************************************************************************/
    public RecordChoiceDialog( Frame owner, List<IPeak> peaks, IPeak peak,
        String[] heading, String message )
    {
        super( owner, "Choose Dot...", true );
        cancelled = false;

        // ---------------------------------------------------------------------
        // Button Panel
        // ---------------------------------------------------------------------
        JPanel buttonPanel = new JPanel( new GridBagLayout() );
        noneButton = new JButton( "None Match" );
        chooseButton = new JButton( "Choose Selected" );
        JButton cancelButton = new JButton( "Cancel" );

        chooseButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                choices = chooseSelectedPeaks();
                if( choices.size() > 0 )
                {
                    dispose();
                }
                else
                {
                    JOptionPane.showMessageDialog( RecordChoiceDialog.this,
                        "Try selecting something first.", "Nothing selected",
                        JOptionPane.ERROR_MESSAGE );
                }
            }
        } );

        noneButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                chooseNoPeaks();
            }
        } );

        cancelButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                cancel();
            }
        } );

        buttonPanel.add( chooseButton, new GridBagConstraints( 0, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                4, 4, 4, 10 ), 0, 0 ) );

        buttonPanel.add( noneButton, new GridBagConstraints( 1, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                4, 10, 4, 4 ), 0, 0 ) );

        buttonPanel.add( cancelButton, new GridBagConstraints( 2, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                4, 10, 4, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Main Panel
        // ---------------------------------------------------------------------
        JPanel mainPanel = new JPanel( new GridBagLayout() );

        JLabel questionLabel = new JLabel( message );
        ItemTable<IPeak> refTable = new ItemTable<IPeak>(
            new PeakRecordTableModel() );
        JScrollPane refScrollPane = new JScrollPane( refTable );

        List<IPeak> data = new ArrayList<IPeak>();
        data.add( peak );
        refTable.getTableModel().setItems( data );
        refTable.getTableModel().setColumnNames( Arrays.asList( heading ) );
        refTable.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
        refTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

        refScrollPane.setMinimumSize( new Dimension( 36, 36 ) );
        refScrollPane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER );

        dotTable = new ItemTable<IPeak>( new PeakRecordTableModel() );
        JScrollPane dotScrollPane = new JScrollPane( dotTable );

        dotTable.getTableModel().setColumnNames( Arrays.asList( heading ) );
        dotTable.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );

        dotScrollPane.setMinimumSize( new Dimension( 0, 0 ) );

        mainPanel.add( questionLabel, new GridBagConstraints( 0, 1, 1, 1, 1.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 4, 5, 4 ), 0, 0 ) );

        mainPanel.add( refScrollPane, new GridBagConstraints( 0, 2, 1, 1, 1.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 4, 5, 4 ), 0, 0 ) );

        mainPanel.add( dotScrollPane, new GridBagConstraints( 0, 3, 1, 1, 1.0,
            1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(
                5, 4, 4, 4 ), 0, 0 ) );

        mainPanel.add( buttonPanel, new GridBagConstraints( 0, 4, 1, 1, 1.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 5, 4, 4, 4 ), 0, 0 ) );

        setContentPane( mainPanel );
        mainPanel.setSize( new Dimension( 500, 300 ) );
        getContentPane().setSize( new Dimension( 500, 300 ) );
        setSize( 700, 300 );
        setData( peaks );

        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        validate();
        setLocationRelativeTo( owner );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isCancelled()
    {
        return cancelled;
    }

    /***************************************************************************
     * @return Dot
     **************************************************************************/
    public List<IPeak> getChoices()
    {
        if( cancelled )
        {
            throw new RuntimeException(
                "No choice was made because the operation was cancelled." );
        }
        return choices;
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setSelectButtonText( String text )
    {
        chooseButton.setText( text );
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setDeclineButtonText( String text )
    {
        noneButton.setText( text );
    }

    /***************************************************************************
     *
     **************************************************************************/
    private void setData( List<IPeak> peaks )
    {
        dotTable.getTableModel().setItems( peaks );
        dotTable.getSelectionModel().setSelectionInterval( 0, 0 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private List<IPeak> chooseSelectedPeaks()
    {
        List<IPeak> peaks = new ArrayList<IPeak>();
        int[] rowsChosen = dotTable.getSelectedRows();
        IPeak peak = null;

        if( rowsChosen != null && rowsChosen.length > 0 )
        {
            for( int row : rowsChosen )
            {
                peak = dotTable.getTableModel().getRow( row );
                peaks.add( peak );
            }
        }

        return peaks;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void chooseNoPeaks()
    {
        this.dispose();
        choices = null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void cancel()
    {
        cancelled = true;
        dispose();
    }
}
