package org.jutils.ui.validators;

import org.jutils.ValidationException;
import org.jutils.ui.FormatException;

/*******************************************************************************
 * Defines a method for validating the text format of some data.
 * @param <T> the type of data to be validated.
 ******************************************************************************/
public interface IDataValidator<T>
{
    /***************************************************************************
     * Validates and creates the data from the text representation if possible
     * or throws a {@link FormatException} otherwise.
     * @param text the text representation of the data to be validated.
     * @return the object parsed from the text.
     * @throws FormatException a description of the errorneous format of the
     * text supplied.
     **************************************************************************/
    public T validate( String text ) throws ValidationException;
}
