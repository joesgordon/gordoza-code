package org.jutils.ui.validation;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jutils.ValidationException;
import org.jutils.ui.validators.ITextValidator;

/*******************************************************************************
 * A validation field that validates user input according to an
 * {@link ITextValidator}.
 ******************************************************************************/
public final class ValidationTextField implements IValidationField
{
    /**  */
    private final JTextField field;
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
     * @param str
     **************************************************************************/
    public ValidationTextField( String str )
    {
        this.field = new JTextField( str );
        this.listenerList = new ValidityListenerList();

        this.validBackground = field.getBackground();
        this.invalidBackground = Color.red;

        this.validator = null;

        field.getDocument().addDocumentListener(
            new ValidationDocumentListener( this ) );
        field.setBackground( validBackground );

        setComponentValid( listenerList.getValidity().isValid );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JTextField getView()
    {
        return field;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return listenerList.getValidity();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void validateText()
    {
        if( validator != null )
        {
            Validity v;

            try
            {
                validator.validateText( field.getText() );
                v = new Validity();
            }
            catch( ValidationException ex )
            {
                v = new Validity( ex.getMessage() );
            }

            setComponentValid( v.isValid );

            // LogUtils.printDebug(
            // ">>> Validating text \"%s\", old validity: %s, new validity: %s",
            // field.getText(), listenerList.getValidity(), v );
            // Utils.printStackTrace();

            listenerList.signalValidity( v );
        }
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        field.setBackground( null );
        field.setEditable( editable );

        if( editable )
        {
            setComponentValid( getValidity().isValid );
        }
        else
        {
            setComponentValid( true );
            listenerList.signalValidity();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setComponentValid( boolean valid )
    {
        if( field.isEditable() && field.isEnabled() )
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
        else
        {
            field.setBackground( null );
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
        setComponentValid( listenerList.getValidity().isValid );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setInvalidBackground( Color bg )
    {
        invalidBackground = bg;
        setComponentValid( listenerList.getValidity().isValid );
    }

    /***************************************************************************
     * @param validator
     **************************************************************************/
    public final void setValidator( ITextValidator validator )
    {
        this.validator = validator;

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
        private ValidationTextField field;

        public ValidationDocumentListener( ValidationTextField field )
        {
            this.field = field;
        }

        @Override
        public void removeUpdate( DocumentEvent e )
        {
            // LogUtils.printDebug( "Updating text" );
            field.validateText();
        }

        @Override
        public void insertUpdate( DocumentEvent e )
        {
            // LogUtils.printDebug( "Inserting text" );
            field.validateText();
        }

        @Override
        public void changedUpdate( DocumentEvent e )
        {
            // LogUtils.printDebug( "Changing text" );
            field.validateText();
        }
    }
}
