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
public class BusyBar2 extends JComponent
{
    public static enum BarColor
    {
        BLUE, RED, GREEN, PURPLE;
    }

    private static final int ALPHA = 125;

    private int currentBox = 7;
    private int size;
    private LinkedList<Color> colors;
    private Timer timer;

    private int numBoxes = 16;

    public BusyBar2()
    {
        this( 30 );
    }

    /***************************************************************************
     * Constructor
     * @param size
     **************************************************************************/
    public BusyBar2( int size )
    {
        this( size, BarColor.RED );
    }

    /***************************************************************************
     * Constructor
     * @param size
     * @param color
     **************************************************************************/
    public BusyBar2( int size, BarColor color )
    {
        this.size = size;

        setBackground( Color.white );
        setOpaque( true );

        setPreferredSize( new Dimension( ( size * numBoxes ) + 14, size ) );
        setMinimumSize( getPreferredSize() );

        colors = new LinkedList<Color>();
        setColors( color );

        timer = new Timer( 100, new BusyAction() );
    }

    /***************************************************************************
     * Starts/stops this component from updating.
     * @param busy
     **************************************************************************/
    public void setBusy( boolean busy )
    {
        if( busy )
        {
            timer.start();
        }
        else
        {
            timer.stop();
        }
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
                colors.add( new Color( 100, 119, 255, ALPHA ) );
                colors.add( new Color( 87, 102, 255, ALPHA ) );
                colors.add( new Color( 71, 85, 255, ALPHA ) );
                colors.add( new Color( 55, 68, 255, ALPHA ) );
                colors.add( new Color( 41, 51, 255, ALPHA ) );
                colors.add( new Color( 25, 34, 255, ALPHA ) );
                colors.add( new Color( 13, 17, 255, ALPHA ) );
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

    /***************************************************************************
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     **************************************************************************/
    @Override
    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );

        int xSpacing = 4;
        int ySpacing = 4;
        int x;
        int boxIdx;
        int height = getHeight() - ( 2 * ySpacing );
        int leftover;

        numBoxes = getWidth() / ( size + 2 );
        leftover = numBoxes * ( size + 2 );

        // g.setColor( getBackground() );
        // g.fillRect( 0, 0, getWidth(), getHeight() );

        // paint boxes
        for( int i = 0; i < colors.size(); i++ )
        {
            boxIdx = currentBox + i;
            boxIdx = boxIdx >= numBoxes ? boxIdx - numBoxes : boxIdx;
            x = xSpacing + boxIdx * ( size + 2 ) + 1;
            g.setColor( colors.get( i ) );

            g.fill3DRect( x, ySpacing, size, height, true );
        }

        g.setColor( colors.get( 0 ) );
        for( boxIdx = 0; boxIdx < numBoxes; boxIdx++ )
        {
            if( boxIdx >= currentBox && boxIdx < currentBox + colors.size() )
            {
                continue;
            }
            x = xSpacing + boxIdx * ( size + 2 ) + 1;
            g.fill3DRect( x, ySpacing, size, height, true );
        }

        if( leftover > 5 )
        {
            x = xSpacing + numBoxes * ( size + 2 ) + 1;
            g.fill3DRect( x, ySpacing, size, height, true );
        }

        // for( int i = 0; i < numBoxes; i++ )
        // {
        // // fill rectangle
        // colorIdx = currentBox + colors.size() > numBoxes &&
        // i < colors.size() ? numBoxes - currentBox + i : i - currentBox;
        // // colorIdx = colorIdx > numBoxes - colors.size() ? 0 : 2;
        // colorIdx = colorIdx < 0 ? 0 : colorIdx;
        // colorIdx = colorIdx >= colors.size() ? 0 : colorIdx;
        // g.setColor( colors.get( colorIdx ) );
        // x = xSpacing + ( i * size + i * 2 );
        // g.fill3DRect( x, ySpacing, size, getHeight() - ( 2 * ySpacing ),
        // true );
        // }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class BusyAction implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            currentBox++;
            if( currentBox >= numBoxes )
            {
                currentBox = 0;
            }

            getParent().repaint();
        }
    }
}
