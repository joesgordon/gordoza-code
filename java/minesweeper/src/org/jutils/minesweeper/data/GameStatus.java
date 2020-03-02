package org.jutils.minesweeper.data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jutils.minesweeper.data.GridSpace.SpaceStatus;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GameStatus
{
    /**  */
    public Difficulty difficulty;
    /**  */
    public boolean started;
    /**  */
    public int flagsLeft;
    /**  */
    public int revealed;
    /**  */
    public boolean over;
    /**  */
    private LocalDateTime startTime;
    /**  */
    private LocalDateTime stopTime;

    /**  */
    public final List<GridSpace> spaces;

    /***************************************************************************
     * 
     **************************************************************************/
    public GameStatus()
    {
        this.difficulty = Difficulty.EASY;
        this.started = false;
        this.flagsLeft = difficulty.numFlags;
        this.revealed = 0;
        this.over = false;
        this.startTime = null;
        this.stopTime = null;
        this.spaces = new ArrayList<>();
    }

    /***************************************************************************
     * @param difficulty
     **************************************************************************/
    public void generateSpaces( Difficulty difficulty )
    {
        this.difficulty = difficulty;
        this.started = false;
        this.flagsLeft = difficulty.numFlags;
        this.revealed = 0;
        this.over = false;
        this.startTime = LocalDateTime.now();
        this.stopTime = null;
        this.spaces.clear();

        int size = difficulty.size;
        int count = size * size;

        for( int i = 0; i < count; i++ )
        {
            int x = i % size;
            int y = i / size;
            GridSpace space = new GridSpace( i, x, y );

            spaces.add( space );
        }
    }

    /***************************************************************************
     * @param startIndex
     **************************************************************************/
    public void startGame( int startIndex )
    {
        this.started = true;

        int size = difficulty.size;
        int numFlags = difficulty.numFlags;

        int startX = startIndex % size;
        int startY = startIndex / size;

        List<GridSpace> mineList = new ArrayList<>( spaces );
        Collections.shuffle( mineList );

        // Mark mines

        int numMarked = 0;
        for( int i = 0; numMarked < numFlags && i < mineList.size(); )
        {
            GridSpace space = mineList.get( i );

            if( !space.isAdjacent( startX, startY ) )
            {
                space.numAdj = -1;
                numMarked++;
                i++;
            }
            else
            {
                mineList.remove( i );
            }
        }

        // Update adjacent

        for( int i = 0; i < numFlags; i++ )
        {
            GridSpace space = mineList.get( i );

            for( GridSpace adjSpace : getAdjacent( space ) )
            {
                if( adjSpace.isClear() )
                {
                    adjSpace.numAdj++;
                }
            }
        }
    }

    /***************************************************************************
     * @param space
     **************************************************************************/
    public void toggleFlag( GridSpace space )
    {
        if( started && !over )
        {
            boolean canToggle = space.status != SpaceStatus.REVEALED;

            if( canToggle )
            {
                space.toggleFlag();

                if( space.status == SpaceStatus.FLAGGED )
                {
                    flagsLeft--;
                }
                else
                {
                    flagsLeft++;
                }
            }
        }
    }

    /***************************************************************************
     * @param space
     * @return {@code true} if the game is finished.
     **************************************************************************/
    public GameResult reveal( GridSpace space )
    {
        if( !started || over )
        {
            return GameResult.NOT_STARTED;
        }

        if( space.status == SpaceStatus.TILE )
        {
            space.status = SpaceStatus.REVEALED;
            revealed++;

            if( space.isMine() )
            {
                stopTime = LocalDateTime.now();
                over = true;
                return GameResult.LOST;
            }

            if( space.numAdj == 0 )
            {
                revealAdditional( space );
            }

            if( revealed == difficulty.getSafeCount() )
            {
                return GameResult.WON;
            }
        }

        return GameResult.PLAYING;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getGameTime()
    {
        long time = 0;

        if( !started )
        {
            time = 0;
        }
        else if( over )
        {
            time = toMillis( stopTime );
        }
        else
        {
            time = toMillis( LocalDateTime.now() );
        }

        if( time > 999000L )
        {
            time = 999000L;
        }

        return time;
    }

    /***************************************************************************
     * @param endTime
     * @return
     **************************************************************************/
    private long toMillis( LocalDateTime endTime )
    {
        Duration d = Duration.between( startTime, endTime );

        return d.getSeconds();
    }

    /***************************************************************************
     * @param space
     **************************************************************************/
    private void revealAdditional( GridSpace space )
    {
        // for( GridSpace adjSpace : getCross( space ) )
        for( GridSpace adjSpace : getAdjacent( space ) )
        {
            if( adjSpace.status == SpaceStatus.TILE )
            {
                adjSpace.status = SpaceStatus.REVEALED;
                revealed++;

                if( adjSpace.numAdj == 0 )
                {
                    revealAdditional( adjSpace );
                }
            }
        }
    }

    /***************************************************************************
     * @param space
     * @return
     **************************************************************************/
    private List<GridSpace> getAdjacent( GridSpace space )
    {
        List<GridSpace> adjacent = new ArrayList<>();
        int size = difficulty.size;

        for( int x = space.x - 1; x < space.x + 2; x++ )
        {
            for( int y = space.y - 1; y < space.y + 2; y++ )
            {
                int index = toIndex( x, y );

                if( x > -1 && y > -1 && x < size && y < size )
                {
                    GridSpace adjSpace = spaces.get( index );

                    adjacent.add( adjSpace );
                }
            }
        }

        return adjacent;
    }

    /***************************************************************************
     * @param x
     * @param y
     * @return
     **************************************************************************/
    private int toIndex( int x, int y )
    {
        return y * difficulty.size + x;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void revealAll()
    {
        for( GridSpace space : spaces )
        {
            space.status = SpaceStatus.REVEALED;
        }
    }

    public static enum GameResult
    {
        NOT_STARTED,
        PLAYING,
        WON,
        LOST;
    }
}
