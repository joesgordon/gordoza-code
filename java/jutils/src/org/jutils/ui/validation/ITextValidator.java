package org.jutils.ui.validation;


/*******************************************************************************
 * 
 ******************************************************************************/
public interface ITextValidator
{
    /***************************************************************************
     * @param text
     * @throws ValidationException
     **************************************************************************/
    public void validateText( String text ) throws ValidationException;
}
