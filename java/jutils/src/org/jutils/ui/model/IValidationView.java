package org.jutils.ui.model;

import java.awt.Color;

import org.jutils.ui.ValidationTextField.IValidityChangedListener;

public interface IValidationView extends IComponentView
{
    public void addValidityChanged( IValidityChangedListener l );

    public void removeValidityChanged( IValidityChangedListener l );

    public boolean isValid();

    public void setValidBackground( Color bg );

    public void setInvalidBackground( Color bg );
}
