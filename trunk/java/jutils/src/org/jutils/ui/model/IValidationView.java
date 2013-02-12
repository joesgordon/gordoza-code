package org.jutils.ui.model;

import java.awt.Color;

import org.jutils.ui.UValidationTextField.ITextValidator;
import org.jutils.ui.UValidationTextField.ValidityChangedListener;

public interface IValidationView extends IComponentView
{
    public void addValidityChanged( ValidityChangedListener l );

    public void removeValidityChanged( ValidityChangedListener l );

    public boolean isValid();

    public void setValidBackground( Color bg );

    public void setInvalidBackground( Color bg );

    public void setValidator( ITextValidator tv );
}
