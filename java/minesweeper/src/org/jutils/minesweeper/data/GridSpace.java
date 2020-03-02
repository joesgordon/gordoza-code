package org.jutils.minesweeper.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GridSpace
{
    /**  */
    public final int index;
    /**  */
    public final int x;
    /**  */
    public final int y;

    /**
     * The number of adjacent mines; < 0 indicates this space is a mine.
     * @see #isMine()
     */
    public int numAdj;

    /**  */
    public SpaceStatus status;

    /***************************************************************************
     * @param index
     * @param x
     * @param y
     **************************************************************************/
    public GridSpace( int index, int x, int y )
    {
        this.index = index;
        this.x = x;
        this.y = y;

        this.numAdj = 0;

        this.status = SpaceStatus.TILE;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void toggleFlag()
    {
        switch( status )
        {
            case TILE:
                status = SpaceStatus.FLAGGED;
                break;

            case FLAGGED:
                status = SpaceStatus.TILE;
                break;

            case REVEALED:
                break;

            default:
                break;
        }
    }

    /***************************************************************************
     * @return {@code true} if this space is a mine.
     **************************************************************************/
    public boolean isMine()
    {
        return numAdj < 0;
    }

    /***************************************************************************
     * @return {@code true} if this space is not a mine.
     **************************************************************************/
    public boolean isClear()
    {
        return numAdj > -1;
    }

    /***************************************************************************
     * @return {@code true} if this space is a revealed mine.
     **************************************************************************/
    public boolean isDetonated()
    {
        return isMine() && status == SpaceStatus.REVEALED;
    }

    /***************************************************************************
     * @param x
     * @param y
     * @return
     **************************************************************************/
    public boolean isAdjacent( int x, int y )
    {
        int dx = Math.abs( x - this.x );
        int dy = Math.abs( y - this.y );

        return dx < 2 && dy < 2;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static enum SpaceStatus
    {
        /**  */
        TILE,
        /**  */
        FLAGGED,
        /**  */
        REVEALED;
    }
}
