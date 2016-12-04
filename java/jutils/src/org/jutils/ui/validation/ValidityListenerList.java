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
    private Validity validity;

    /***************************************************************************
     * 
     **************************************************************************/
    public ValidityListenerList()
    {
        this.validityChangedListeners = new LinkedList<IValidityChangedListener>();

        this.validity = new Validity( "Uninitialized validity" );
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
    public void signalValidity()
    {
        signalValidity( new Validity() );
    }

    /***************************************************************************
     * @param reason
     **************************************************************************/
    public void signalValidity( String reason )
    {
        signalValidity( new Validity( reason ) );
    }

    /***************************************************************************
     * @param validity
     **************************************************************************/
    public void signalValidity( Validity validity )
    {
        for( IValidityChangedListener vcl : validityChangedListeners )
        {
            vcl.signalValidity( validity );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Validity getValidity()
    {
        return validity;
    }
}
