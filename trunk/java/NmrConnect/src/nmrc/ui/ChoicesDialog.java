package nmrc.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.TableModel;

import nmrc.ui.tables.*;

import org.jutils.ui.ResizingTable;
import org.jutils.ui.model.ItemTableModel;

/*******************************************************************************
 *
 ******************************************************************************/
public class ChoicesDialog<T> extends JDialog
{
    // -------------------------------------------------------------------------
    // Widgets
    // -------------------------------------------------------------------------
    /**  */
    private ItemTableModel<T> choicesModel;
    /**  */
    private ResizingTable<ItemTableModel<T>> choicesTable;
    /**  */
    private JButton chooseButton;
    /**  */
    private JButton noneButton;

    // -------------------------------------------------------------------------
    // Supporting data.
    // -------------------------------------------------------------------------
    /**  */
    private List<T> choices;
    /**  */
    private boolean cancelled;

    /***************************************************************************
     * @param owner Frame
     * @param dots Vector
     * @param peak Dot
     * @param heading DotHeading
     **************************************************************************/
    public ChoicesDialog( Frame owner, String message, TableModel baseModel,
        ItemTableModel<T> choicesModel )
    {
        super( owner, "Choose Dot...", true );

        this.choicesModel = choicesModel;
        cancelled = false;

        // ---------------------------------------------------------------------
        // Setup listeners.
        // ---------------------------------------------------------------------
        ActionListener chooseListener = new ActionListener()
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
                    JOptionPane.showMessageDialog( ChoicesDialog.this,
                        "Try selecting something first.", "Nothing selected",
                        JOptionPane.ERROR_MESSAGE );
                }
            }
        };

        ActionListener noneListener = new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                chooseNoPeaks();
            }
        };

        ActionListener cancelListener = new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                cancel();
            }
        };

        // ---------------------------------------------------------------------
        // Button Panel
        // ---------------------------------------------------------------------
        JPanel buttonPanel = createButtonPanel( chooseListener, noneListener,
            cancelListener );

        // ---------------------------------------------------------------------
        // Main Panel
        // ---------------------------------------------------------------------
        JPanel mainPanel = createMainPanel( message, buttonPanel, baseModel,
            choicesModel );

        setContentPane( mainPanel );
        mainPanel.setSize( new Dimension( 500, 300 ) );
        getContentPane().setSize( new Dimension( 500, 300 ) );
        setSize( 700, 300 );

        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        validate();
        setLocationRelativeTo( owner );

        addWindowListener( new WindowAdapter()
        {
            public void windowClosing( WindowEvent e )
            {
                cancelled = true;
            }
        } );
    }

    /***************************************************************************
     * @param message
     * @param buttonPanel
     * @param heading
     * @param baseModel
     * @param choicesModel
     * @return
     **************************************************************************/
    private JPanel createMainPanel( String message, JPanel buttonPanel,
        TableModel baseModel, ItemTableModel<T> choicesModel )
    {
        JPanel mainPanel = new JPanel( new GridBagLayout() );

        JLabel questionLabel = new JLabel( message );
        @SuppressWarnings( "rawtypes")
        DefaultObjectTable<?> refTable = new DefaultObjectTable( baseModel );
        JScrollPane refScrollPane = new JScrollPane( refTable );

        refTable.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
        refTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

        refScrollPane.setMinimumSize( new Dimension( 36, 36 ) );
        refScrollPane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER );

        choicesTable = new ItemTable<T>( choicesModel );
        JScrollPane dotScrollPane = new JScrollPane( choicesTable );

        choicesTable.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );

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

        return mainPanel;
    }

    /***************************************************************************
     * @param chooseListener
     * @param noneListener
     * @param cancelListener
     * @return
     **************************************************************************/
    private JPanel createButtonPanel( ActionListener chooseListener,
        ActionListener noneListener, ActionListener cancelListener )
    {
        JPanel buttonPanel = new JPanel( new GridBagLayout() );
        noneButton = new JButton( "None Match" );
        chooseButton = new JButton( "Choose Selected" );
        JButton cancelButton = new JButton( "Cancel" );

        chooseButton.addActionListener( chooseListener );

        noneButton.addActionListener( noneListener );

        cancelButton.addActionListener( cancelListener );

        buttonPanel.add( chooseButton, new GridBagConstraints( 0, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                4, 4, 4, 10 ), 0, 0 ) );

        buttonPanel.add( noneButton, new GridBagConstraints( 1, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                4, 10, 4, 4 ), 0, 0 ) );

        buttonPanel.add( cancelButton, new GridBagConstraints( 2, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                4, 10, 4, 4 ), 0, 0 ) );

        return buttonPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isCancelled()
    {
        return cancelled;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T getChoice()
    {
        if( cancelled )
        {
            throw new RuntimeException(
                "No choice was made because the operation was cancelled." );
        }
        return choices != null ? choices.get( 0 ) : null;
    }

    /***************************************************************************
     * @return Dot
     **************************************************************************/
    public List<T> getChoices()
    {
        if( cancelled )
        {
            throw new RuntimeException(
                "No choice was made because the operation was cancelled." );
        }
        return choices;
    }

    /***************************************************************************
     * @param row
     **************************************************************************/
    public void setSelectedRow( int row )
    {
        choicesTable.getSelectionModel().setSelectionInterval( row, row );
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
    private List<T> chooseSelectedPeaks()
    {
        List<T> peaks = new ArrayList<T>();
        int[] rowsChosen = choicesTable.getSelectedRows();
        T peak = null;

        if( rowsChosen != null && rowsChosen.length > 0 )
        {
            for( int row : rowsChosen )
            {
                peak = choicesModel.getRow( row );
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
        choices = null;
        this.dispose();
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
