package org.jutils.ui.validation;

import java.awt.Color;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jutils.ui.fields.IValidationField;
import org.jutils.ui.validators.ITextValidator;

public class ValidationTextArea implements IValidationField
{
    private final JTextArea field;
    /**  */
    private final ValidityListenerList listenerList;

    /**  */
    private Color validBackground;
    /**  */
    private Color invalidBackground;
    /**  */
    private ITextValidator validator;

    public ValidationTextArea()
    {
        this.field = new JTextArea();
        this.listenerList = new ValidityListenerList();

        this.validBackground = field.getBackground();
        this.invalidBackground = Color.red;

        this.validator = null;

        field.getDocument().addDocumentListener(
            new ValidationDocumentListener( this ) );
        field.setBackground( validBackground );

        setComponentValid( listenerList.isValid() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JTextArea getView()
    {
        return field;
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
            boolean newValidity = true;
            String reason = null;

            try
            {
                validator.validateText( field.getText() );
                newValidity = true;
            }
            catch( ValidationException ex )
            {
                newValidity = false;
                reason = ex.getMessage();
            }

            if( ignorePreviousValidity || listenerList.isValid() != newValidity )
            {
                setComponentValid( newValidity );
            }

            if( newValidity )
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
            field.setBackground( validBackground );
        }
        else
        {
            field.setBackground( invalidBackground );
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
        field.setText( text );
        validateText();
    }

    /***************************************************************************
     * @param columns
     **************************************************************************/
    public void setColumns( int columns )
    {
        field.setColumns( columns );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ValidationDocumentListener implements DocumentListener
    {
        private ValidationTextArea field;

        public ValidationDocumentListener( ValidationTextArea field )
        {
            this.field = field;
        }

        public void removeUpdate( DocumentEvent e )
        {
            field.validateText();
        }

        public void insertUpdate( DocumentEvent e )
        {
            field.validateText();
        }

        public void changedUpdate( DocumentEvent e )
        {
            field.validateText();
        }
    }
}
