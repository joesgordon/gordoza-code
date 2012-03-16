package testbed;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.Timer;

/*******************************************************************************
 * This component represents a bar signifying some indeterminate amount of
 * processing is taking place.
 ******************************************************************************/
public class BusyBar extends JComponent implements ActionListener
{
    public static enum BarColor
    {
        BLUE, RED, GREEN, PURPLE;
    }

    private static final int ALPHA = 125;

    private final int size;
    private int currentBox;
    private LinkedList<Color> colors;
    private Timer timer;

    /***************************************************************************
     * Constructor
     * @param size
     **************************************************************************/
    public BusyBar( int size )
    {
        this( size, BarColor.BLUE );
    }

    /***************************************************************************
     * Constructor
     * @param size
     * @param color
     **************************************************************************/
    public BusyBar( int size, BarColor color )
    {
        this.size = size;

        setPreferredSize( new Dimension( ( size * 8 ) + 14, size ) );
        setMinimumSize( new Dimension( ( size * 8 ) + 14, size ) );

        currentBox = 0;

        colors = new LinkedList<Color>();
        setColors( color );

        timer = new Timer( 100, this );
    }

    /***************************************************************************
     * Starts/stops this component from updating.
     * @param busy
     **************************************************************************/
    public void setBusy( boolean busy )
    {
        if( busy )
            timer.start();
        else
            timer.stop();
    }

    /***************************************************************************
     * Sets the colors associated with this bar.
     * @param color
     **************************************************************************/
    public void setColors( BarColor color )
    {
        colors.clear();
        switch( color )
        {
            default:
            case BLUE:
                colors.add( new Color( 119, 119, 255, ALPHA ) );
                colors.add( new Color( 102, 102, 255, ALPHA ) );
                colors.add( new Color( 85, 85, 255, ALPHA ) );
                colors.add( new Color( 68, 68, 255, ALPHA ) );
                colors.add( new Color( 51, 51, 255, ALPHA ) );
                colors.add( new Color( 34, 34, 255, ALPHA ) );
                colors.add( new Color( 17, 17, 255, ALPHA ) );
                colors.add( new Color( 0, 0, 255, ALPHA ) );
                break;
            case RED:
                colors.add( new Color( 255, 119, 119, ALPHA ) );
                colors.add( new Color( 255, 102, 102, ALPHA ) );
                colors.add( new Color( 255, 85, 85, ALPHA ) );
                colors.add( new Color( 255, 68, 68, ALPHA ) );
                colors.add( new Color( 255, 51, 51, ALPHA ) );
                colors.add( new Color( 255, 34, 34, ALPHA ) );
                colors.add( new Color( 255, 17, 17, ALPHA ) );
                colors.add( new Color( 255, 0, 0, ALPHA ) );
                break;
            case GREEN:
                colors.add( new Color( 119, 255, 119, ALPHA ) );
                colors.add( new Color( 102, 255, 102, ALPHA ) );
                colors.add( new Color( 85, 255, 85, ALPHA ) );
                colors.add( new Color( 68, 255, 68, ALPHA ) );
                colors.add( new Color( 51, 255, 51, ALPHA ) );
                colors.add( new Color( 34, 255, 34, ALPHA ) );
                colors.add( new Color( 17, 255, 17, ALPHA ) );
                colors.add( new Color( 0, 255, 0, ALPHA ) );
                break;
            case PURPLE:
                colors.add( new Color( 255, 119, 255, ALPHA ) );
                colors.add( new Color( 255, 102, 255, ALPHA ) );
                colors.add( new Color( 255, 85, 255, ALPHA ) );
                colors.add( new Color( 255, 68, 255, ALPHA ) );
                colors.add( new Color( 255, 51, 255, ALPHA ) );
                colors.add( new Color( 255, 34, 255, ALPHA ) );
                colors.add( new Color( 255, 17, 255, ALPHA ) );
                colors.add( new Color( 255, 0, 255, ALPHA ) );
                break;
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed( ActionEvent arg0 )
    {
        currentBox++;
        if( currentBox > 7 )
        {
            currentBox = 0;
        }

        // "shift" color array
        colors.addFirst( colors.removeLast() );

        getParent().repaint();
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     */
    @Override
    public void paint( Graphics g )
    {
        // paint boxes
        for( int i = 0; i < 8; i++ )
        {
            // fill rectangle
            g.setColor( colors.get( i ) );
            g.fillRoundRect( ( i * size + i * 2 ), 0, size, size, 3, 3 );
        }
    }
}
