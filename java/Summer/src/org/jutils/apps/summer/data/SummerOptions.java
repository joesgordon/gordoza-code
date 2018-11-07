package org.jutils.apps.summer.data;

/**
 *
 */
public class SummerOptions
{
    /**  */
    public int numThreads;

    /**
     * 
     */
    public SummerOptions()
    {
    }

    /**
     * @param options
     */
    public SummerOptions( SummerOptions options )
    {
        this.numThreads = options.numThreads;
    }
}
