package org.jutils.ui.validation;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jutils.ui.validators.ITextValidator;

/*******************************************************************************
 * A validation field that validates user input according to an
 * {@link ITextValidator}.
 ******************************************************************************/
public final class ValidationTextField implements IValidationField
{
    /**  */
    private final JTextField textfield;
    /**  */
    private final ValidityListenerList listenerList;

    /**  */
    private Color validBackground;
    /**  */
    private Color invalidBackground;
    /**  */
    private ITextValidator validator;

    /***************************************************************************
     * 
     **************************************************************************/
    public ValidationTextField()
    {
        this( "" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public ValidationTextField( int columns )
    {
        this();

        setColumns( columns );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public ValidationTextField( AbstractFormatterFactory factory )
    {
        this( factory, "" );
    }

    /***************************************************************************
     * @param str
     **************************************************************************/
    public ValidationTextField( String str )
    {
        this( null, str );
    }

    /***************************************************************************
     * @param str
     **************************************************************************/
    public ValidationTextField( AbstractFormatterFactory factory, String str )
    {
        this.textfield = new JFormattedTextField( factory, str );
        this.listenerList = new ValidityListenerList();

        this.validBackground = textfield.getBackground();
        this.invalidBackground = Color.red;

        this.validator = null;

        textfield.getDocument().addDocumentListener(
            new ValidationDocumentListener( this ) );
        textfield.setBackground( validBackground );

        setComponentValid( listenerList.isValid() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JTextField getView()
    {
        return textfield;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isValid()
    {
        return listenerList.isValid();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getInvalidationReason()
    {
        return listenerList.getInvalidationReason();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void validateText()
    {
        validateText( false );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void validateText( boolean ignorePreviousValidity )
    {
        if( validator != null )
        {
            boolean validity = true;
            String reason = null;

            try
            {
                validator.validateText( textfield.getText() );
                validity = true;
            }
            catch( ValidationException ex )
            {
                validity = false;
                reason = ex.getMessage();
            }

            if( ignorePreviousValidity || listenerList.isValid() != validity )
            {
                setComponentValid( validity );
            }

            // System.out.println( ">>>Validating text \"" + textfield.getText()
            // +
            // "\", old validity: " + listenerList.isValid() +
            // ", new validity: " + validity );
            // Utils.printStackTrace();

            if( validity )
            {
                listenerList.signalValid();
            }
            else
            {
                listenerList.signalInvalid( reason );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setComponentValid( boolean valid )
    {
        if( valid )
        {
            textfield.setBackground( validBackground );
        }
        else
        {
            textfield.setBackground( invalidBackground );
        }
    }

    /***************************************************************************
     * @param vcl
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener vcl )
    {
        listenerList.addListener( vcl );
    }

    /***************************************************************************
     * @param vcl
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener vcl )
    {
        listenerList.removeListener( vcl );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValidBackground( Color bg )
    {
        validBackground = bg;
        setComponentValid( listenerList.isValid() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setInvalidBackground( Color bg )
    {
        invalidBackground = bg;
        setComponentValid( listenerList.isValid() );
    }

    /***************************************************************************
     * @param tv
     **************************************************************************/
    public final void setValidator( ITextValidator tv )
    {
        validator = tv;

        validateText( true );
    }

    /***************************************************************************
     * @param name
     **************************************************************************/
    public void setText( String text )
    {
        textfield.setText( text );
        // validateText();
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addActionListener( ActionListener l )
    {
        textfield.addActionListener( l );
    }

    /***************************************************************************
     * @param columns
     **************************************************************************/
    public void setColumns( int columns )
    {
        textfield.setColumns( columns );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ValidationDocumentListener implements DocumentListener
    {
        private ValidationTextField field;

        public ValidationDocumentListener( ValidationTextField field )
        {
            this.field = field;
        }

        @Override
        public void removeUpdate( DocumentEvent e )
        {
            // System.out.println( "Updating text" );
            field.validateText();
        }

        @Override
        public void insertUpdate( DocumentEvent e )
        {
            // System.out.println( "Inserting text" );
            field.validateText();
        }

        @Override
        public void changedUpdate( DocumentEvent e )
        {
            // System.out.println( "Changing text" );
            field.validateText();
        }
    }
}
