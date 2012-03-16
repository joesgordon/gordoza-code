package nmrc.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import nmrc.data.AminoAcid;
import nmrc.model.*;
import nmrc.ui.tables.ObjectTable;
import nmrc.ui.tables.models.AminoAcidTableModel;

import org.jutils.Utils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PeakAssignerDialog extends JDialog
{
    /**  */
    private boolean cancelled;
    /**  */
    private PeakAssignerPanel assignerPanel;
    /**  */
    private JLabel messageLabel;

    /***************************************************************************
     * @param aAcids
     * @param chain
     * @param options
     **************************************************************************/
    public PeakAssignerDialog( List<IAminoAcid> aAcids, List<IPeak> chain,
        List<IAssignmentOption> options, Frame parent )
    {
        super( parent, "Choose peak assignment", true );

        JPanel mainPanel = new JPanel( new GridBagLayout() );

        messageLabel = new JLabel();

        assignerPanel = new PeakAssignerPanel();

        mainPanel.add( messageLabel, new GridBagConstraints( 0, 0, 1, 1, 1.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        mainPanel.add( assignerPanel, new GridBagConstraints( 0, 1, 1, 1, 1.0,
            1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        mainPanel.add( createButtonPanel(), new GridBagConstraints( 0, 2, 1, 1,
            0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        setContentPane( mainPanel );

        pack();
        setLocationRelativeTo( parent );

        if( options.size() > 1 )
        {
            messageLabel.setText( "Choose the desired assignment." );
        }
        else
        {
            messageLabel.setText( "Do you want the assignment below?" );
        }

        assignerPanel.setData( aAcids, chain, options );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createButtonPanel()
    {
        JPanel buttonPanel = new JPanel( new GridBagLayout() );

        JButton okButton = new JButton( "Ok" );
        JButton cancelButton = new JButton( "Cancel" );

        Dimension dim = Utils.getMaxComponentSize( okButton, cancelButton );

        okButton.setPreferredSize( dim );
        cancelButton.setPreferredSize( dim );

        okButton.addActionListener( new ButtonListener( false ) );
        cancelButton.addActionListener( new ButtonListener( true ) );

        buttonPanel.add( okButton, new GridBagConstraints( 0, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 10, 0, 10, 10 ), 10, 5 ) );

        buttonPanel.add( cancelButton, new GridBagConstraints( 1, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 10, 10, 10, 0 ), 10, 5 ) );

        return buttonPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IAssignmentOption getChoice()
    {
        if( cancelled )
        {
            return null;
        }

        return assignerPanel.getChoice();
    }

    private class ButtonListener implements ActionListener
    {
        private boolean cancel;

        public ButtonListener( boolean cancel )
        {
            this.cancel = cancel;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            if( cancel )
            {
                cancelled = true;
            }
            dispose();
        }
    }
}

/*******************************************************************************
 * 
 ******************************************************************************/
class PeakAssignerPanel extends JPanel
{
    /**  */
    private JComboBox optionComboBox;
    /**  */
    private AminoAcidTableModel aaModel;

    // -------------------------------------------------------------------------
    // Data to set your components by.
    // -------------------------------------------------------------------------
    /**  */
    private List<IAminoAcid> acids;
    /**  */
    private List<IPeak> peakChain;

    /***************************************************************************
     * 
     **************************************************************************/
    public PeakAssignerPanel()
    {
        super( new GridBagLayout() );

        ObjectTable<IAminoAcid, AminoAcidTableModel> aaTable;
        JLabel optionsLabel;
        JScrollPane tableScrollPane;

        aaModel = new AminoAcidTableModel();
        optionComboBox = new JComboBox();
        aaTable = new ObjectTable<IAminoAcid, AminoAcidTableModel>( aaModel );
        tableScrollPane = new JScrollPane( aaTable );
        optionsLabel = new JLabel( "Select Option :" );

        optionComboBox.addActionListener( new ComboBoxListener() );

        add( optionsLabel, new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 2,
                2, 2, 2 ), 0, 0 ) );
        add( optionComboBox, new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        add( tableScrollPane, new GridBagConstraints( 0, 1, 2, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2,
                2, 2, 2 ), 0, 0 ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IAssignmentOption getChoice()
    {
        IAssignmentOption option = null;
        Object item = optionComboBox.getSelectedItem();

        if( item != null )
        {
            option = ( ( ChoiceStringAdapter )item ).getOption();
        }

        return option;
    }

    /***************************************************************************
     * @param aAcids
     * @param chain
     * @param options
     **************************************************************************/
    public void setData( List<IAminoAcid> aAcids, List<IPeak> chain,
        List<IAssignmentOption> options )
    {
        acids = aAcids;
        peakChain = chain;

        for( IAssignmentOption option : options )
        {
            String str = "";
            for( int i = option.getPeakStart(); i < option.getPeakEnd(); i++ )
            {
                if( i > option.getPeakStart() )
                {
                    str += " -> ";
                }
                str += chain.get( i ).getRecord().getPeakName();
            }
            optionComboBox.addItem( new ChoiceStringAdapter( option, str ) );
        }
    }

    /**
     * @param option
     */
    private void setChoice( IAssignmentOption option )
    {
        if( option == null )
        {
            aaModel.clearItems();
            return;
        }

        List<IAminoAcid> proposedList = new ArrayList<IAminoAcid>();

        for( int i = 0; i < option.getLength(); i++ )
        {
            IAminoAcid aa = acids.get( option.getAminoAcidStart() + i );
            IPeak p = peakChain.get( option.getPeakStart() + i );

            AminoAcid realaa = new AminoAcid( aa.getShiftX() );

            realaa.setPeak( p );

            proposedList.add( realaa );
        }

        aaModel.setItems( proposedList );
    }

    private class ComboBoxListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            setChoice( getChoice() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ChoiceStringAdapter
    {
        private IAssignmentOption option;
        private String str;

        public ChoiceStringAdapter( IAssignmentOption option, String str )
        {
            this.option = option;
            this.str = str;
        }

        public IAssignmentOption getOption()
        {
            return option;
        }

        @Override
        public String toString()
        {
            return str;
        }
    }
}
