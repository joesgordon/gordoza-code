package org.jutils.data;

import org.jutils.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum ColorMapType implements INamedItem
{
    GRAYSCALE( "Grayscale" ),
    MATLAB_DEFAULT( "Matlab Default" ),
    MATLAB_HOT( "Matlab Hot" ),
    NIGHT_VISION( "Night Vision" ),
    QUADRATIC( "RGB Quadratic" ),
    LINEAR_STEP( "Linear Step" );

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
