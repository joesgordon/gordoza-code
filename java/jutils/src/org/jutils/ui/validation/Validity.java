package org.jutils.ui.validation;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Validity
{
    /**  */
    public final boolean isValid;
    /**  */
    public final String reason;

    /***************************************************************************
     * 
     **************************************************************************/
    public Validity()
    {
        this.isValid = true;
        this.reason = "";
    }

    /***************************************************************************
     * @param reason
     **************************************************************************/
    public Validity( String reason )
    {
        this.isValid = false;
        this.reason = reason;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return isValid + " - " + reason;
    }

    public <C> C choose( C validChoice, C invalidChoice )
    {
        return isValid ? validChoice : invalidChoice;
    }
}
