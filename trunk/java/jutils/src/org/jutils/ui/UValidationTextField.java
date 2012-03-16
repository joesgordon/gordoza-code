package org.jutils.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jutils.Utils;

import com.jgoodies.looks.Options;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UValidationTextField extends UFormattedTextField
{
    /**  */
    public Color validBackground;
    /**  */
    public Color errorBackground;
    /**  */
    public TextValidator validator;
    /**  */
    public List<ValidityChangedListener> validityChangedListeners;
    /**  */
    public boolean valid;

    /***************************************************************************
     * 
     **************************************************************************/
    public UValidationTextField()
    {
        this( "" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public UValidationTextField( AbstractFormatterFactory factory )
    {
        super( factory );
    }

    /***************************************************************************
     * @param str
     **************************************************************************/
    public UValidationTextField( String str )
    {
        this( null, str );
    }

    /***************************************************************************
     * @param str
     **************************************************************************/
    public UValidationTextField( AbstractFormatterFactory factory, String str )
    {
        super( str );

        setFormatterFactory( factory );

        if( validBackground == null )
        {
            validBackground = this.getBackground();
        }
        errorBackground = Color.red;
        validator = null;
        valid = true;
        validityChangedListeners = new LinkedList<ValidityChangedListener>();
        getDocument().addDocumentListener( new ValidationDocumentListener() );

        super.setBackground( validBackground );
    }

    // TODO add function setting the invalid tool tip text.

    /***************************************************************************
     * 
     **************************************************************************/
    public boolean isValid()
    {
        return valid;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void validateText()
    {
        if( validator != null )
        {
            boolean oldValidity = valid;
            valid = validator.validateText( getText() );

            if( oldValidity != valid )
            {
                fireValidityChanged();
                setComponentValid( valid );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected void setComponentValid( boolean valid )
    {
        if( valid )
        {
            super.setBackground( validBackground );
        }
        else
        {
            super.setBackground( errorBackground );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected void fireValidityChanged()
    {
        for( ValidityChangedListener vcl : validityChangedListeners )
        {
            vcl.validityChanged( valid );
        }
    }

    /***************************************************************************
     * @param vcl
     **************************************************************************/
    public void addValidityChanged( ValidityChangedListener vcl )
    {
        validityChangedListeners.add( 0, vcl );
    }

    /***************************************************************************
     * @param vcl
     **************************************************************************/
    public void removeValidityChanged( ValidityChangedListener vcl )
    {
        validityChangedListeners.remove( vcl );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setBackground( Color bg )
    {
        validBackground = bg;
        if( valid )
        {
            super.setBackground( bg );
        }
    }

    /***************************************************************************
     * @param tv
     **************************************************************************/
    public final void setValidator( TextValidator tv )
    {
        validator = tv;
        validateText();
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel( Options.PLASTICXP_NAME );
                    // UIManager
                    // .setLookAndFeel(
                    // "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel" );
                    Options.setSelectOnFocusGainEnabled( true );
                }
                catch( Exception ex )
                {
                    ex.printStackTrace();
                }

                final JFrame frame = new JFrame();
                JPanel panel = new JPanel();

                JLabel jl1 = new JLabel( "Number, (0-25):" );
                JLabel jl2 = new JLabel( "Number, (0-25):" );
                JLabel jl3 = new JLabel( "Number, [0-25]:" );
                JLabel jl4 = new JLabel( "The phrase 'e pluribus unum':" );

                final UValidationTextField uvtf1a = new UValidationTextField();
                final UValidationTextField uvtf2a = new UValidationTextField();
                final UValidationTextField uvtf3b = new UValidationTextField();
                final UValidationTextField uvtf4c = new UValidationTextField();

                final JButton okButton = new JButton( "OK" );
                JButton cancelButton = new JButton( "Cancel" );
                Dimension btnSize = Utils.getMaxComponentSize( new Component[] {
                    okButton, cancelButton } );

                TextValidator tvA = new TextValidator()
                {
                    public boolean validateText( String text )
                    {
                        boolean valid = false;
                        int i = 0;
                        try
                        {
                            i = Integer.parseInt( text );
                            if( i > -1 && i < 26 )
                            {
                                valid = true;
                            }
                        }
                        catch( NumberFormatException ex )
                        {
                            valid = false;
                        }
                        return valid;
                    }
                };

                TextValidator tvB = new TextValidator()
                {
                    public boolean validateText( String text )
                    {
                        boolean valid = false;
                        int i = 0;
                        try
                        {
                            i = Integer.parseInt( text );
                            if( i > 0 && i < 25 )
                            {
                                valid = true;
                            }
                        }
                        catch( NumberFormatException ex )
                        {
                            valid = false;
                        }
                        return valid;
                    }
                };

                TextValidator tvC = new TextValidator()
                {
                    public boolean validateText( String text )
                    {
                        return text.compareTo( "e pluribus unum" ) == 0;
                    }
                };

                ValidityChangedListener vcl = new ValidityChangedListener()
                {
                    public void validityChanged( boolean newValidity )
                    {
                        newValidity = uvtf1a.isValid() && uvtf2a.isValid() &&
                            uvtf3b.isValid() && uvtf4c.isValid();
                        okButton.setEnabled( newValidity );
                    }
                };

                ActionListener buttonListener = new ActionListener()
                {
                    public void actionPerformed( ActionEvent e )
                    {
                        frame.dispose();
                    }
                };

                panel.setLayout( new GridBagLayout() );

                okButton.setPreferredSize( btnSize );
                okButton.addActionListener( buttonListener );
                okButton.setEnabled( false );
                cancelButton.setPreferredSize( btnSize );
                cancelButton.addActionListener( buttonListener );

                uvtf1a.setValidator( tvA );
                uvtf1a.addValidityChanged( vcl );

                uvtf2a.setValidator( tvA );
                uvtf2a.addValidityChanged( vcl );

                uvtf3b.setValidator( tvB );
                uvtf3b.addValidityChanged( vcl );

                uvtf4c.setValidator( tvC );
                uvtf4c.addValidityChanged( vcl );

                panel.add( jl1, new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets( 2, 2, 2, 2 ), 0, 0 ) );
                panel.add( uvtf1a, new GridBagConstraints( 1, 0, 4, 1, 1.0,
                    0.0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, new Insets( 2, 2, 2, 2 ), 0,
                    0 ) );

                panel.add( jl2, new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets( 2, 2, 2, 2 ), 0, 0 ) );
                panel.add( uvtf2a, new GridBagConstraints( 1, 1, 4, 1, 1.0,
                    0.0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, new Insets( 2, 2, 2, 2 ), 0,
                    0 ) );

                panel.add( jl3, new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets( 2, 2, 2, 2 ), 0, 0 ) );
                panel.add( uvtf3b, new GridBagConstraints( 1, 2, 4, 1, 1.0,
                    0.0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, new Insets( 2, 2, 2, 2 ), 0,
                    0 ) );

                panel.add( jl4, new GridBagConstraints( 0, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets( 2, 2, 2, 2 ), 0, 0 ) );
                panel.add( uvtf4c, new GridBagConstraints( 1, 3, 4, 1, 1.0,
                    0.0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, new Insets( 2, 2, 2, 2 ), 0,
                    0 ) );

                panel.add( Box.createHorizontalStrut( 0 ),
                    new GridBagConstraints( 1, 4, 1, 1, 1.0, 0.0,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets( 2, 2, 2, 2 ), 0, 0 ) );
                panel.add( okButton, new GridBagConstraints( 2, 4, 1, 1, 0.0,
                    0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets( 2, 2, 2, 2 ), 0, 0 ) );
                panel.add( cancelButton, new GridBagConstraints( 3, 4, 1, 1,
                    0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets( 2, 2, 2, 2 ), 0, 0 ) );

                frame.setContentPane( panel );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setSize( 500, 200 );
                frame.validate();
                frame.setMinimumSize( frame.getSize() );
                frame.setLocationRelativeTo( null );
                frame.setVisible( true );
            }
        } );
    }

    private class ValidationDocumentListener implements DocumentListener
    {
        public void removeUpdate( DocumentEvent e )
        {
            validateText();
        }

        public void insertUpdate( DocumentEvent e )
        {
            validateText();
        }

        public void changedUpdate( DocumentEvent e )
        {
            validateText();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface ValidityChangedListener
    {
        public void validityChanged( boolean newValidity );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface TextValidator
    {
        public boolean validateText( String text );
    }
}
