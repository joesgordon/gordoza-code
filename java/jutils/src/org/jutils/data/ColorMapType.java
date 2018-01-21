package org.jutils.data;

import org.jutils.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum ColorMapType implements INamedItem
{
    STANDARD( "Standard" ),
    QUADRATIC( "RGB Quadratic" ),
    LINEAR_STEP( "Linear Step" ),
    GRAYSCALE( "Grayscale" ),
    MATLAB_HOT( "Matlab Hot" ),
    MATLAB_DEFAULT( "Matlab Default" ),
    NIGHT_VISION( "Night Vision" );

    /**  */
    public final String name;

    /***************************************************************************
     * @param name
     **************************************************************************/
    private ColorMapType( String name )
    {
        this.name = name;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }
}
