package org.jutils.ui.validation;

import java.awt.*;

import javax.swing.*;

import org.jutils.ui.fields.IValidationField;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * Displays only an {@link IValidationField} unless the field is invalid; in
 * which case, the error is also displayed in a non-editable text field.
 ******************************************************************************/
public class ValidationView implements IView<JPanel>
{
    /** The view that contains all the components. */
    private final JPanel view;
    /** The validation field to be displayed. */
    private final IValidationField field;
    /** Any units to be shown; only visible when units are specified. */
    private final JLabel unitsField;
    /** The field that displays validation errors; only visible when invalid. */
    private final JTextField errorField;
    /** The constraints for the error field. Only keep one for add/removal. */
    private final GridBagConstraints errorConstraints;

    /***************************************************************************
     * Creates a new view with the supplied field and no units.
     * @param field the validation field to be displayed.
     **************************************************************************/
    public ValidationView( IValidationField field )
    {
        this( field, null );
    }

    /***************************************************************************
     * Creates a new view with the supplied field and units.
     * @param field the validation field to be displayed.
     * @param units the units to be displayed; units field is not visible if
     * {@code null}.
     **************************************************************************/
    public ValidationView( IValidationField field, String units )
    {
        this.field = field;

        this.errorField = new JTextField();
        this.unitsField = units == null ? null : new JLabel( units );
        this.errorConstraints = new GridBagConstraints( 0, 1, 2, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                4, 0, 0, 0 ), 0, 0 );

        Dimension dim = errorField.getPreferredSize();
        dim.width = field.getView().getPreferredSize().width;

        errorField.setText( "ERROR: " + field.getInvalidationReason() );
        errorField.setPreferredSize( dim );
        errorField.setEditable( false );

        this.view = createView();

        // LogUtils.printDebug( "Adding validity changed listner" );
        field.addValidityChanged( new FieldValidityChangedListener( this ) );

        setErrorFieldVisible( !field.isValid() );
    }

    /***************************************************************************
     * Creates the view and adds the components.
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                0, 0, 0, 0 ), 0, 0 );
        panel.add( field.getView(), constraints );

        if( unitsField != null )
        {
            constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                    0, 4, 0, 0 ), 0, 0 );
            panel.add( unitsField, constraints );
        }

        return panel;
    }

    /***************************************************************************
     * Returns the validation field with which this class was initialized.
     **************************************************************************/
    public IValidationField getField()
    {
        return field;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * Shows or hides the error field according to the supplied parameter.
     * @param visible shows the field when {@code true}, hides otherwise.
     **************************************************************************/
    private void setErrorFieldVisible( boolean visible )
    {
        if( visible )
        {
            view.add( errorField, errorConstraints.clone() );
        }
        else
        {
            view.remove( errorField );
        }

        view.revalidate();
        view.repaint();
    }

    /***************************************************************************
     * Validity listener added to the validation field that show/hides/populates
     * the error field.
     **************************************************************************/
    private static class FieldValidityChangedListener implements
        IValidityChangedListener
    {
        private final ValidationView view;

        public FieldValidityChangedListener( ValidationView view )
        {
            this.view = view;
        }

        @Override
        public void signalValid()
        {
            // LogUtils.printDebug( "Valid" );
            view.setErrorFieldVisible( false );
            view.errorField.setText( "" );
        }

        @Override
        public void signalInvalid( String reason )
        {
            // LogUtils.printDebug( "Invalid: " + reason );
            view.setErrorFieldVisible( true );
            view.errorField.setText( "ERROR: " + reason );
        }
    }
}
