package org.jutils.ui.validation;

import java.awt.Color;

import org.jutils.ui.model.IComponentView;

/*******************************************************************************
 * Defines an {@link IComponentView} that supports validation.
 ******************************************************************************/
public interface IValidationView extends IComponentView
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
     * Returns {@code true} if the component is currently valid.
     * @return the validity of the component.
     **************************************************************************/
    public boolean isValid();

    /***************************************************************************
     * Returns the reason for invalidation or {@code null} if the field is
     * valid.
     * @return the reason for invalidation.
     **************************************************************************/
    public String getInvalidationReason();

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
}
