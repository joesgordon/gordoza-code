package org.jutils.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

import javax.swing.*;

import org.jutils.SwingUtils;

/*******************************************************************************
 *
 ******************************************************************************/
public class FindDialog extends JDialog
{
    // -------------------------------------------------------------------------
    // Main panel widgets.
    // -------------------------------------------------------------------------
    /**  */
    private JPanel contentPane = new JPanel();

    /**  */
    private GridBagLayout contentLayout = new GridBagLayout();

    /**  */
    private JLabel findLable = new JLabel();

    /**  */
    private JTextField findTextField = new JTextField();

    /**  */
    private JButton findButton = new JButton();

    /**  */
    private JButton cancelButton = new JButton();

    /**  */
    private JTextArea errorLabel = new JTextArea();

    // --------------------------------------------------------------------------
    // Options panel widgets.
    // --------------------------------------------------------------------------
    /**  */
    private JPanel optionsPanel = new JPanel();

    /**  */
    private GridBagLayout optionsLayout = new GridBagLayout();

    /**  */
    private JCheckBox matchCheckBox = new JCheckBox();

    /**  */
    private JCheckBox regexCheckBox = new JCheckBox();

    /**  */
    private JCheckBox wrapCheckBox = new JCheckBox();

    // --------------------------------------------------------------------------
    //
    // --------------------------------------------------------------------------
    /**  */
    private ArrayList<FindListener> findListeners = new ArrayList<FindListener>();

    /***************************************************************************
     *
     **************************************************************************/
    public FindDialog()
    {
        this( new Frame(), "FindDialog", false );
    }

    /***************************************************************************
     * @param owner Frame
     * @param title String
     * @param modal boolean
     **************************************************************************/
    public FindDialog( Frame owner, String title, boolean modal )
    {
        super( owner, title, modal );

        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        pack();

        SwingUtils.installEscapeCloseOperation( this );

        // ----------------------------------------------------------------------
        // Setup Options Panel
        // ----------------------------------------------------------------------
        optionsPanel.setLayout( optionsLayout );
        optionsPanel.setBorder( BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Options" ) );

        matchCheckBox.setText( "Match Case" );

        regexCheckBox.setText( "Regular Expression" );

        wrapCheckBox.setText( "Wrap Around" );

        optionsPanel.add( matchCheckBox, new GridBagConstraints( 0, 0, 2, 1,
            0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        optionsPanel.add( regexCheckBox, new GridBagConstraints( 0, 1, 2, 1,
            0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        optionsPanel.add( wrapCheckBox, new GridBagConstraints( 0, 2, 2, 1,
            0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Setup Main Panel
        // ---------------------------------------------------------------------

        contentPane.setLayout( contentLayout );

        findLable.setText( "Find What:" );
        findTextField.setColumns( 25 );

        this.getRootPane().setDefaultButton( findButton );
        findButton.setText( "Find" );
        findButton.addActionListener( new FindTextListener( this ) );
        findButton.setDefaultCapable( true );

        cancelButton.setText( "Cancel" );
        cancelButton.addActionListener( new CancelListener( this ) );

        errorLabel.setText( "" );
        errorLabel.setEditable( false );
        errorLabel.setBackground( this.getBackground() );
        errorLabel.setBorder( BorderFactory.createLineBorder( Color.red ) );
        errorLabel.setVisible( false );

        contentPane.add( findLable, new GridBagConstraints( 0, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                5, 5, 5, 5 ), 0, 0 ) );
        contentPane.add( findTextField, new GridBagConstraints( 1, 0, 2, 1,
            0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 5, 5, 5, 5 ), 0, 0 ) );
        contentPane.add( findButton, new GridBagConstraints( 3, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 5, 5, 5, 5 ), 0, 0 ) );

        contentPane.add( optionsPanel, new GridBagConstraints( 0, 1, 2, 2, 1.0,
            1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                0, 0, 0, 0 ), 0, 0 ) );
        contentPane.add( cancelButton, new GridBagConstraints( 3, 1, 1, 1, 0.0,
            0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
            new Insets( 5, 5, 5, 5 ), 0, 0 ) );

        contentPane.add( errorLabel, new GridBagConstraints( 2, 2, 2, 1, 1.0,
            1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(
                0, 0, 0, 0 ), 0, 0 ) );

        getContentPane().add( contentPane );
        pack();

        setOptions( null );

        // ----------------------------------------------------------------------
        // Set tab-order.
        // ----------------------------------------------------------------------
        ArrayList<JComponent> list = new ArrayList<JComponent>();
        list.add( this.findTextField );

        list.add( this.matchCheckBox );
        list.add( this.regexCheckBox );
        list.add( this.wrapCheckBox );

        list.add( this.findButton );
        list.add( this.cancelButton );

        this.setFocusTraversalPolicy( new FocusPolicyList( list ) );
    }

    /***************************************************************************
     *
     **************************************************************************/
    public void requestFocus()
    {
        super.requestFocus();

        findTextField.requestFocus();
    }

    /***************************************************************************
     * @param visible boolean
     **************************************************************************/
    public void setVisible( boolean visible )
    {
        super.setVisible( visible );
        if( visible )
        {
            findTextField.requestFocus();
        }
    }

    /***************************************************************************
     * @param fl FindListener
     **************************************************************************/
    public void addFindListener( FindListener fl )
    {
        findListeners.add( fl );
    }

    /***************************************************************************
     * @param fl FindListener
     **************************************************************************/
    public void removeFindListener( FindListener fl )
    {
        findListeners.remove( fl );
    }

    /***************************************************************************
     * @param e ActionEvent
     **************************************************************************/
    public void findText()
    {
        FindOptions options = getOptions();
        if( options.textToFind.length() > 0 )
        {
            for( int i = findListeners.size() - 1; i > -1; i-- )
            {
                FindListener fl = ( FindListener )findListeners.get( i );
                try
                {
                    fl.findText( options );
                    errorLabel.setVisible( false );
                }
                catch( PatternSyntaxException ex )
                {
                    errorLabel.setText( ex.getMessage() );
                    errorLabel.setVisible( true );
                }
            }
        }
        this.setVisible( errorLabel.isVisible() );
    }

    /***************************************************************************
     * @return FindOptions
     **************************************************************************/
    public FindOptions getOptions()
    {
        FindOptions options = new FindOptions();

        options.textToFind = findTextField.getText();
        options.matchCase = matchCheckBox.isSelected();
        options.wrapAround = wrapCheckBox.isSelected();
        options.useRegex = regexCheckBox.isSelected();

        return options;
    }

    /***************************************************************************
     * @param options FindOptions
     **************************************************************************/
    public void setOptions( FindOptions options )
    {
        if( options == null )
        {
            options = new FindOptions();
        }

        findTextField.setText( options.textToFind );
        matchCheckBox.setSelected( options.matchCase );
        wrapCheckBox.setSelected( options.wrapAround );
        regexCheckBox.setSelected( options.useRegex );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class CancelListener implements ActionListener
    {
        private FindDialog adaptee;

        public CancelListener( FindDialog adaptee )
        {
            this.adaptee = adaptee;
        }

        public void actionPerformed( ActionEvent e )
        {
            ExitListener.doDefaultCloseOperation( adaptee );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FindTextListener implements ActionListener
    {
        private FindDialog adaptee;

        public FindTextListener( FindDialog adaptee )
        {
            this.adaptee = adaptee;
        }

        public void actionPerformed( ActionEvent e )
        {
            adaptee.findText();
        }
    }
}
