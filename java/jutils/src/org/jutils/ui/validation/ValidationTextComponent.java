package org.jutils.ui.validation;

import java.awt.Color;
import java.awt.Font;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.jutils.ValidationException;
import org.jutils.ui.fields.IValidationField;
import org.jutils.ui.validators.ITextValidator;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ValidationTextComponent<T extends JTextComponent>
    implements IValidationField
{
    private final T field;
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
    public ValidationTextComponent( T comp )
    {
        this.field = comp;
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
    public T getView()
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

            if( ignorePreviousValidity ||
                listenerList.isValid() != newValidity )
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
     * @param validator
     **************************************************************************/
    public final void setValidator( ITextValidator validator )
    {
        this.validator = validator;

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
     * @return
     **************************************************************************/
    public String getText()
    {
        return field.getText();
    }

    /***************************************************************************
     * @param font
     **************************************************************************/
    public void setFont( Font font )
    {
        field.setFont( font );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ValidationDocumentListener implements DocumentListener
    {
        private ValidationTextComponent<?> field;

        public ValidationDocumentListener( ValidationTextComponent<?> field )
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
