package org.jutils.ui.progress;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ProgressUpdater
{
    /**  */
    public final IProgressView progress;
    /**  */
    public final Number length;

    /**  */
    private Number position;
    /**  */
    private int percent;

    /***************************************************************************
     * @param progress
     * @param length
     **************************************************************************/
    public ProgressUpdater( IProgressView progress, Number length )
    {
        this.progress = progress;
        this.length = length;

        this.position = 0;
        this.percent = 0;

        progress.setIndeterminate( false );
        progress.setPercentComplete( 0 );
    }

    /***************************************************************************
     * @param position
     **************************************************************************/
    public void update( Number position )
    {
        this.position = position;
        int percent = ( int )( position.doubleValue() * 100.0 / length.doubleValue() );
        if( percent != this.percent )
        {
            this.percent = percent;
            progress.setPercentComplete( percent );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Number getPosition()
    {
        return position;
    }
}
