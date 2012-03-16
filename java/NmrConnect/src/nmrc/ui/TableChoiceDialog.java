package nmrc.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.Utils;

/**
 * 
 */
public class TableChoiceDialog extends JDialog
{
    /**  */
    private JTable table;
    /**  */
    private int choice = -1;

    /**
     * @param owner
     * @param title
     * @param message
     * @param choices
     */
    public TableChoiceDialog( Window owner, String title, String message,
        JTable choices )
    {
        super( owner, title, ModalityType.DOCUMENT_MODAL );

        table = choices;

        // ---------------------------------------------------------------------
        // Setup button panel.
        // ---------------------------------------------------------------------
        JPanel buttonPanel = new JPanel( new GridBagLayout() );
        JButton okButton = new JButton( "Ok" );
        JButton cancelButton = new JButton( "Cancel" );

        Dimension maxSize = Utils.getMaxComponentSize( okButton, cancelButton );

        okButton.setPreferredSize( maxSize );
        okButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                int row = table.getSelectedRow();
                if( row > -1 )
                {
                    choice = row;
                }
                dispose();
            }
        } );
        cancelButton.setPreferredSize( maxSize );
        cancelButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                choice = -1;
                dispose();
            }
        } );

        buttonPanel.add( okButton, new GridBagConstraints( 0, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                0, 0, 0, 10 ), 20, 0 ) );
        buttonPanel.add( cancelButton, new GridBagConstraints( 1, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                10, 0, 0, 0 ), 20, 0 ) );

        // ---------------------------------------------------------------------
        // Setup content panel.
        // ---------------------------------------------------------------------
        JPanel contentPanel = new JPanel( new GridBagLayout() );
        JScrollPane tableScrollPane = new JScrollPane( choices );
        JLabel label = new JLabel( message );

        table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

        contentPanel.add( label, new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 4, 4,
                4, 4 ), 0, 0 ) );

        contentPanel.add( tableScrollPane, new GridBagConstraints( 0, 1, 1, 1,
            1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        contentPanel.add( buttonPanel, new GridBagConstraints( 0, 2, 1, 1, 1.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 4, 4, 4 ), 0, 0 ) );
    }

    /**
     * @return
     */
    public int getChoosenRow()
    {
        return choice;
    }
}
