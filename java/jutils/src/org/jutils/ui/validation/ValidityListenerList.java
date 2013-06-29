package org.jutils.ui.validation;

import java.util.LinkedList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ValidityListenerList
{
    /**  */
    private final List<IValidityChangedListener> validityChangedListeners;

    /** The last evaluated validity; {@code true} by default. */
    private boolean lastValidity;
    /**  */
    private String lastReason;

    /***************************************************************************
     * 
     **************************************************************************/
    public ValidityListenerList()
    {
        this.validityChangedListeners = new LinkedList<IValidityChangedListener>();

        this.lastValidity = false;
        this.lastReason = "Uninitialized validity";
    }

    /***************************************************************************
     * @param vcl
     **************************************************************************/
    public void addListener( IValidityChangedListener vcl )
    {
        validityChangedListeners.add( 0, vcl );
    }

    /***************************************************************************
     * @param vcl
     **************************************************************************/
    public void removeListener( IValidityChangedListener vcl )
    {
        validityChangedListeners.remove( vcl );
    }

    /***************************************************************************
     * Updates the last known validity and calls listeners if it changed.
     * @param newValidity the latest validity.
     **************************************************************************/
    public void signalValid()
    {
        lastValidity = true;
        lastReason = null;

        if( !lastValidity )
        {
            for( IValidityChangedListener vcl : validityChangedListeners )
            {
                vcl.signalValid();
            }
        }
    }

    /***************************************************************************
     * @param reason
     **************************************************************************/
    public void signalInvalid( String reason )
    {
        lastValidity = false;
        lastReason = reason;

        for( IValidityChangedListener vcl : validityChangedListeners )
        {
            vcl.signalInvalid( reason );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isValid()
    {
        return lastValidity;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getInvalidationReason()
    {
        return lastReason;
    }
}
