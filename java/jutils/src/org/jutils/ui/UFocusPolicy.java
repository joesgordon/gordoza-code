package org.jutils.ui;

import java.awt.*;
import java.util.ArrayList;

/*******************************************************************************
 *
 ******************************************************************************/
public class UFocusPolicy extends FocusTraversalPolicy
{
    /**  */
    private ArrayList<Component> order;

    /***************************************************************************
     * @param order
     **************************************************************************/
    public UFocusPolicy( ArrayList<? extends Component> order )
    {
        this.order = new ArrayList<Component>( order.size() );
        this.order.addAll( order );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Component getComponentAfter( Container focusCycleRoot,
        Component aComponent )
    {
        int idx = ( order.indexOf( aComponent ) + 1 ) % order.size();
        return order.get( idx );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Component getComponentBefore( Container focusCycleRoot,
        Component aComponent )
    {
        int idx = order.indexOf( aComponent ) - 1;
        if( idx < 0 )
        {
            idx = order.size() - 1;
        }
        return order.get( idx );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Component getDefaultComponent( Container focusCycleRoot )
    {
        return order.get( 0 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Component getLastComponent( Container focusCycleRoot )
    {
        return order.get( order.size() - 1 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Component getFirstComponent( Container focusCycleRoot )
    {
        return order.get( 0 );
    }
}
