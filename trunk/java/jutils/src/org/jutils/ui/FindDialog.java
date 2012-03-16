package org.jutils.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

import javax.swing.*;

/*******************************************************************************
 *
 ******************************************************************************/
public class FindDialog extends JDialog
{
    // --------------------------------------------------------------------------
    // Main panel widgets.
    // --------------------------------------------------------------------------
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
     * @param owner Frame
     * @param title String
     * @param modal boolean
     **************************************************************************/
    public FindDialog( Frame owner, String title, boolean modal )
    {
        super( owner, title, modal );
        try
        {
            setDefaultCloseOperation( DISPOSE_ON_CLOSE );
            jbInit();
            pack();
        }
        catch( Exception exception )
        {
            exception.printStackTrace();
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    public FindDialog()
    {
        this( new Frame(), "FindDialog", false );
    }

    /***************************************************************************
     * @throws Exception
     **************************************************************************/
    private void jbInit() throws Exception
    {
        KeyAdapter escListener = new FindDialog_this_keyAdapter( this );

        // ----------------------------------------------------------------------
        // Setup Options Panel
        // ----------------------------------------------------------------------
        optionsPanel.setLayout( optionsLayout );
        optionsPanel.setBorder( BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Options" ) );

        matchCheckBox.setText( "Match Case" );
        matchCheckBox.addKeyListener( escListener );

        regexCheckBox.setText( "Regular Expression" );
        regexCheckBox.addKeyListener( escListener );
        wrapCheckBox.setText( "Wrap Around" );
        wrapCheckBox.addKeyListener( escListener );

        optionsPanel.add( matchCheckBox, new GridBagConstraints( 0, 0, 2, 1,
            0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        optionsPanel.add( regexCheckBox, new GridBagConstraints( 0, 1, 2, 1,
            0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        optionsPanel.add( wrapCheckBox, new GridBagConstraints( 0, 2, 2, 1,
            0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        // ----------------------------------------------------------------------
        // Setup Main Panel
        // ----------------------------------------------------------------------
        addKeyListener( escListener );

        contentPane.setLayout( contentLayout );

        findLable.setText( "Find What:" );
        findTextField.setColumns( 25 );
        findTextField.addKeyListener( escListener );

        this.getRootPane().setDefaultButton( findButton );
        findButton.setText( "Find" );
        findButton.addActionListener( new FindDialog_findButton_actionAdapter(
            this ) );
        findButton.setDefaultCapable( true );
        findButton.addKeyListener( escListener );

        cancelButton.setText( "Cancel" );
        cancelButton.addActionListener( new FindDialog_cancelButton_actionAdapter(
            this ) );
        cancelButton.addKeyListener( escListener );

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

        this.setFocusTraversalPolicy( new UFocusPolicy( list ) );
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
    public void findButton_actionPerformed( ActionEvent e )
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
     * @param e ActionEvent
     **************************************************************************/
    public void cancelButton_actionPerformed( ActionEvent e )
    {
        setVisible( false );
    }

    /***************************************************************************
     * @param e KeyEvent
     **************************************************************************/
    public void this_keyReleased( KeyEvent e )
    {
        if( e.getKeyCode() == KeyEvent.VK_ESCAPE )
        {
            // System.out.println( "ESC Pressed" );
            this.dispose();
        }
    }
}

class FindDialog_this_keyAdapter extends KeyAdapter
{
    private FindDialog adaptee;

    FindDialog_this_keyAdapter( FindDialog adaptee )
    {
        this.adaptee = adaptee;
    }

    public void keyReleased( KeyEvent e )
    {
        adaptee.this_keyReleased( e );
    }
}

class FindDialog_cancelButton_actionAdapter implements ActionListener
{
    private FindDialog adaptee;

    FindDialog_cancelButton_actionAdapter( FindDialog adaptee )
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e )
    {
        adaptee.cancelButton_actionPerformed( e );
    }
}

class FindDialog_findButton_actionAdapter implements ActionListener
{
    private FindDialog adaptee;

    FindDialog_findButton_actionAdapter( FindDialog adaptee )
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e )
    {
        adaptee.findButton_actionPerformed( e );
    }
}
