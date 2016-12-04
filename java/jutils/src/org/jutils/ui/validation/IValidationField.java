package org.jutils.ui.validation;

import java.awt.Color;

import javax.swing.JComponent;

import org.jutils.ui.model.IComponentView;

/*******************************************************************************
 * Defines an {@link IComponentView} that supports validation.
 ******************************************************************************/
public interface IValidationField extends IComponentView
{
    /***************************************************************************
     * Adds a listener that will be called when the user has entered data that
     * changed the validity of this field.
     * @param l the listener to be added.
     **************************************************************************/
    public void addValidityChanged( IValidityChangedListener l );

    /***************************************************************************
     * Removes the provided listener. Does nothing if the listener is not found.
     * @param l the listener to be removed.
     **************************************************************************/
    public void removeValidityChanged( IValidityChangedListener l );

    /***************************************************************************
     * Returns the current validity state for the field
     * @return the reason for invalidation.
     **************************************************************************/
    public Validity getValidity();

    /***************************************************************************
     * Sets the background color of the component when it is valid.
     * @param bg the new valid background color.
     **************************************************************************/
    public void setValidBackground( Color bg );

    /***************************************************************************
     * Sets the background color of the component when it is invalid.
     * @param bg the new invalid background color.
     **************************************************************************/
    public void setInvalidBackground( Color bg );

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView();
}
