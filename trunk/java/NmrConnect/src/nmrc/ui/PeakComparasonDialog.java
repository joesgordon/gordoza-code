package nmrc.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import nmrc.model.IPeak;
import nmrc.model.IPeakRecord;
import nmrc.ui.tables.ItemTable;
import nmrc.ui.tables.models.PeakRecordTableModel;

/**
 * 
 */
public class PeakComparasonDialog extends JDialog
{
    /**
     * @param owner
     * @param basePeak
     * @param altPeak
     */
    public PeakComparasonDialog( Window owner, IPeakRecord basePeak,
        IPeakRecord altPeak )
    {
        super( owner, "Compare Peaks", ModalityType.APPLICATION_MODAL );

        // ---------------------------------------------------------------------
        // Button Panel
        // ---------------------------------------------------------------------
        JPanel buttonPanel = new JPanel( new GridBagLayout() );
        JButton cancelButton = new JButton( "They All Suck!" );
        JButton confirmButton = new JButton( "Choose Selected" );

        confirmButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                dispose();
            }
        } );

        cancelButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                dispose();
            }
        } );

        buttonPanel.add( confirmButton, new GridBagConstraints( 0, 0, 1, 1,
            0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 4, 4, 4, 10 ), 0, 0 ) );

        buttonPanel.add( cancelButton, new GridBagConstraints( 1, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                4, 10, 4, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Main Panel
        // ---------------------------------------------------------------------
        JPanel mainPanel = new JPanel( new GridBagLayout() );

        JLabel questionLabel = new JLabel( "Are these peaks the same?" );

        ItemTable<IPeak> baseTable = new ItemTable<IPeak>(
            new PeakRecordTableModel() );
        JScrollPane baseScrollPane = new JScrollPane( baseTable );

        ItemTable<IPeak> altTable = new ItemTable<IPeak>(
            new PeakRecordTableModel() );
        JScrollPane altScrollPane = new JScrollPane( altTable );

        baseTable.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
        baseScrollPane.setMinimumSize( new Dimension( 36, 36 ) );
        baseScrollPane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER );

        altTable.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
        altScrollPane.setMinimumSize( new Dimension( 36, 36 ) );
        altScrollPane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER );

        mainPanel.add( questionLabel, new GridBagConstraints( 0, 1, 1, 1, 1.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 4, 5, 4 ), 0, 0 ) );

        mainPanel.add( baseScrollPane, new GridBagConstraints( 0, 2, 1, 1, 1.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 4, 5, 4 ), 0, 0 ) );

        mainPanel.add( altScrollPane, new GridBagConstraints( 0, 3, 1, 1, 1.0,
            1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(
                5, 4, 4, 4 ), 0, 0 ) );

        mainPanel.add( buttonPanel, new GridBagConstraints( 0, 4, 1, 1, 1.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 5, 4, 4, 4 ), 0, 0 ) );

        setContentPane( mainPanel );
        setSize( 700, 300 );
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        validate();
    }
}
