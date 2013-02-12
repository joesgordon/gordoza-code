package org.jutils.ui;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jutils.ui.model.FormatException;
import org.jutils.ui.model.IValidationView;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class UValidationTextField implements IValidationView
{
    private final UFormattedTextField field;
    /**  */
    private final List<ValidityChangedListener> validityChangedListeners;

    /**  */
    private Color validBackground;
    /**  */
    private Color invalidBackground;
    /**  */
    private ITextValidator validator;
    /**  */
    private boolean valid;

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
        this( factory, "" );
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
        field = new UFormattedTextField( str );

        field.setFormatterFactory( factory );

        if( validBackground == null )
        {
            validBackground = field.getBackground();
        }

        invalidBackground = Color.red;
        validator = null;
        valid = true;
        validityChangedListeners = new LinkedList<ValidityChangedListener>();

        field.getDocument().addDocumentListener(
            new ValidationDocumentListener( this ) );

        field.setBackground( validBackground );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public UFormattedTextField getView()
    {
        return field;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
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

            try
            {
                validator.validateText( field.getText() );
                field.setToolTipText( "" );
                valid = true;
            }
            catch( FormatException ex )
            {
                field.setToolTipText( ex.getMessage() );
                valid = false;
            }

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
     * 
     **************************************************************************/
    private void fireValidityChanged()
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
    public void setValidBackground( Color bg )
    {
        validBackground = bg;
        validateText();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void setInvalidBackground( Color bg )
    {
        invalidBackground = bg;
        validateText();
    }

    /***************************************************************************
     * @param tv
     **************************************************************************/
    public final void setValidator( ITextValidator tv )
    {
        validator = tv;
        validateText();
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
     * @param l
     **************************************************************************/
    public void addActionListener( ActionListener l )
    {
        field.addActionListener( l );
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
        private UValidationTextField field;

        public ValidationDocumentListener( UValidationTextField field )
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
    public static interface ITextValidator
    {
        public void validateText( String text ) throws FormatException;
    }
}
