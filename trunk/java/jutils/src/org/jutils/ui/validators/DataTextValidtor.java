package org.jutils.ui.validators;

import org.jutils.ui.validation.IUpdater;
import org.jutils.ui.validation.ValidationException;


/*******************************************************************************
 * Validator that updates an object with the latest data when the data is valid.
 * @param <T> the type of data to be validated/updated.
 ******************************************************************************/
public class DataTextValidtor<T> implements ITextValidator
{
    /** The validator to be used. */
    private final IDataValidator<T> validator;
    /** The updator to be called when data is valid. */
    private final IUpdater<T> updater;

    /***************************************************************************
     * Creates a new validator with the provided data:
     * @param validator the validator to be used to ensure the format of the
     * data.
     * @param updater the updater to be called when the data is valid.
     **************************************************************************/
    public DataTextValidtor( IDataValidator<T> validator, IUpdater<T> updater )
    {
        this.validator = validator;
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void validateText( String text ) throws ValidationException
    {
        T data = validator.validate( text );

        updater.update( data );
    }

}
