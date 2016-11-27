package org.jutils.ui.validation;

import javax.swing.JTextArea;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ValidationTextArea extends ValidationTextComponentField<JTextArea>
{
    /***************************************************************************
     * 
     **************************************************************************/
    public ValidationTextArea()
    {
        super( new JTextArea() );
    }

    /***************************************************************************
     * @param columns
     **************************************************************************/
    public void setColumns( int columns )
    {
        super.getView().setColumns( columns );
    }
}
