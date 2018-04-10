package org.jutils.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.LineBorder;

import org.jutils.data.UIProperty;
import org.jutils.io.LogUtils;
import org.jutils.ui.event.*;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.event.updater.UpdaterList;
import org.jutils.ui.fields.IDescriptor;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * Defines a control similar to a horizontal scroll-bar that pops-up the
 * position when moved. The position will be adjusted to the previous unit
 * increment value. For example a item of length 100 and unit length 10 will
 * have a position of 40 when set to 42.
 ******************************************************************************/
public class PositionIndicator implements IView<JComponent>
{
    /** The window that pops up when the position indicator is moved. */
    private final JWindow positionWindow;
    /** The label that shows the position in the popup window. */
    private final JLabel posLabel;
    /** The list of listers to be notified when the position changes. */
    private final ItemActionList<Long> posititonListeners;
    /** The object that draws the component. */
    private final PositionIndicatorPaintable paintable;
    /** The control to be drawn. */
    private final PaintingComponent component;
    /**  */
    private final PiMouseListener mouseListener;

    // TODO install escape key listener to abort dragging.

    /***************************************************************************
     * Creates a new control with the default descriptor (which displays the
     * position in a zero-padded hexadecimal string).
     **************************************************************************/
    public PositionIndicator()
    {
        this( null );
    }

    /***************************************************************************
     * Creates a new control with the provided descriptor.
     * @param positionDescriptor converts a {@link Long} into a {@link String}.
     **************************************************************************/
    public PositionIndicator( IDescriptor<Long> positionDescriptor )
    {
        IDescriptor<Long> descriptor = positionDescriptor == null
            ? createDefaultPositionDescriptor()
            : positionDescriptor;

        this.paintable = new PositionIndicatorPaintable();
        this.component = new PaintingComponent( paintable );
        this.posLabel = new JLabel();
        this.positionWindow = createWindow();
        this.posititonListeners = new ItemActionList<>();
        this.mouseListener = new PiMouseListener( this, descriptor );

        component.setMinimumSize( new Dimension( 20, 20 ) );
        component.setPreferredSize( new Dimension( 20, 20 ) );

        component.addMouseListener( mouseListener );
        component.addMouseMotionListener( mouseListener );
    }

    /***************************************************************************
     * Creates a position indicator descriptor that displays the position in a
     * zero-padded hexadecimal string.
     * @return the default position descriptor.
     **************************************************************************/
    private IDescriptor<Long> createDefaultPositionDescriptor()
    {
        return ( v ) -> String.format( "0x%016X", v );
    }

    /***************************************************************************
     * Creates the windows that displays the position while dragging the thumb.
     * @return the created window.
     * @see #positionWindow
     **************************************************************************/
    private JWindow createWindow()
    {
        JWindow win = new JWindow();
        JPanel panel = new JPanel( new BorderLayout() );

        posLabel.setForeground( Color.white );
        posLabel.setHorizontalAlignment( SwingConstants.HORIZONTAL );
        posLabel.setFont( new Font( "Monospaced", Font.PLAIN, 12 ) );

        panel.setBackground( new Color( 0x006699 ) );
        panel.setBorder( new LineBorder( Color.darkGray ) );

        panel.add( posLabel, BorderLayout.CENTER );

        win.setContentPane( panel );
        win.setSize( 140, 25 );
        win.validate();

        return win;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return component;
    }

    /***************************************************************************
     * Add the provided listener to be called when the position is updated.
     * @param l the listener to be added.
     **************************************************************************/
    public void addPositionListener( ItemActionListener<Long> l )
    {
        posititonListeners.addListener( l );
    }

    /***************************************************************************
     * Adds a method to be invoked when the tab is right-clicked.
     * @param callback the method to be invoked.
     **************************************************************************/
    public void addRightClick( IUpdater<MouseEvent> callback )
    {
        mouseListener.addRightClick( callback );
    }

    /***************************************************************************
     * Sets the color of the "Thumb" that represents the position.
     * @param c the new color of the "Thumb"
     **************************************************************************/
    public void setUnitColor( Color c )
    {
        paintable.thumbColor = c;
        component.repaint();
    }

    /***************************************************************************
     * Sets the length of the item containing the indicated position.
     * @param length the length of the item.
     **************************************************************************/
    public void setLength( long length )
    {
        paintable.length = length;
        component.repaint();
    }

    /***************************************************************************
     * Returns the previously set length of the item containing the indicated
     * position.
     * @return the length of the item.
     **************************************************************************/
    public long getLength()
    {
        return paintable.length;
    }

    /***************************************************************************
     * Sets the length of the units that break up the length of the item.
     * @param length the length of a unit.
     * @throws IllegalArgumentException if the provided length is less than 1.
     **************************************************************************/
    public void setUnitLength( long length ) throws IllegalArgumentException
    {
        if( length > 0 )
        {
            paintable.unitLength = length;
            component.repaint();
        }
        else
        {
            throw new IllegalArgumentException(
                "The unit increment length must be greater than 0" );
        }
    }

    /***************************************************************************
     * Returns the previously set length of the units that break up the length
     * of the item.
     * @return the length of a unit.
     **************************************************************************/
    public long getUnitLength()
    {
        return paintable.unitLength;
    }

    /***************************************************************************
     * Sets the indicated position.
     * @param position the position to be displayed.
     **************************************************************************/
    public void setPosition( long position )
    {
        paintable.position = position;
        component.repaint();
    }

    /***************************************************************************
     * Returns the previously set indicated position.
     * @return the position being displayed.
     **************************************************************************/
    public long getPosition()
    {
        return paintable.position;
    }

    /***************************************************************************
     * Invokes the callback on all listeners to the position change based on the
     * x-coordinate of the thumb.
     * @param x the x-position in the {@link #component}'s coordinate space that
     * defines the left of the thumb.
     **************************************************************************/
    private void fireThumbMoved( int x )
    {
        long pos = paintable.calculatePosition( x );

        firePositionUpdated( pos );
    }

    /***************************************************************************
     * Invokes the callback on all listeners to the position change.
     * @param position the new indicated positon.
     **************************************************************************/
    private void firePositionUpdated( long position )
    {
        if( position != paintable.position )
        {
            posititonListeners.fireListeners( this, position );
        }
    }

    /***************************************************************************
     * Defines the object that draws the control.
     **************************************************************************/
    private static final class PositionIndicatorPaintable implements IPaintable
    {
        /** The bounds of the thumb. Defaults to zeros. */
        private final Rectangle thumb;
        /**  */
        private final Rectangle track;

        /** The color of the thumb. */
        private Color thumbColor;
        /** The color of the shadow of the thumb. */
        private Color thumbShadow;

        /** The length of the item containing the indicated position. */
        private long length;
        /** The length of units which break up the item's length. */
        private long unitLength;
        /** The current indicated position. */
        private long position;

        /**
         * Creates a new paintable.
         */
        public PositionIndicatorPaintable()
        {
            this.thumb = new Rectangle();
            this.track = new Rectangle();

            this.thumbColor = UIProperty.SCROLLBAR_THUMB.getColor();
            this.thumbShadow = UIProperty.SCROLLBAR_THUMBSHADOW.getColor();
            this.unitLength = 10;
            this.length = 0;
            this.position = 50;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void paint( JComponent c, Graphics2D g2 )
        {
            Rectangle cb = c.getBounds();
            Insets insets = c.getInsets();

            track.x = insets.left;
            track.y = insets.top + 1;
            track.width = cb.width - ( insets.left + insets.right );
            track.height = cb.height - ( insets.top + insets.bottom + 2 );

            thumb.y = track.y;
            thumb.height = track.height;

            calculateThumb();

            Object aaHint = g2.getRenderingHint(
                RenderingHints.KEY_ANTIALIASING );

            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );

            g2.setColor( thumbShadow );
            g2.fillRoundRect( thumb.x, thumb.y, thumb.width, thumb.height, 5,
                5 );

            g2.setColor( thumbColor );
            g2.fillRoundRect( thumb.x + 1, thumb.y + 1, thumb.width - 2,
                thumb.height - 2, 4, 4 );

            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, aaHint );
        }

        /**
         * 
         */
        private void calculateThumb()
        {
            thumb.width = ( int )( track.width *
                ( unitLength / ( double )length ) );

            thumb.width = Math.max( thumb.width, 16 );
            thumb.width = Math.min( thumb.width, track.width );

            thumb.x = calculateThumbPosition();
        }

        /**
         * @return
         */
        private int calculateThumbPosition()
        {
            return calculateThumbPosition( position, length, unitLength,
                track.x, track.width, thumb.width );
        }

        /**
         * Gets the indicated position based on the x-coordinate of the thumb.
         * @param x the x-position in the {@link #component}'s coordinate space
         * that defines the left of the thumb.
         * @return the calculated position.
         */
        private long calculatePosition( int x )
        {
            return calculatePosition( x, length, unitLength, track.x,
                track.width, thumb.width );
        }

        /**
         * @param position
         * @param unitCount
         * @param length
         * @param unitLength
         * @param trackx
         * @param trackWidth
         * @param thumbWidth
         * @return
         */
        private static int calculateThumbPosition( long position, long length,
            long unitLength, int trackx, int trackWidth, int thumbWidth )
        {
            int posMax = trackWidth - thumbWidth - 1;

            long unitCount = calculateUnitCount( length, unitLength );
            long unitIndex = ( long )( position / ( double )length *
                unitCount );

            int posIdx = ( int )( unitIndex / ( double )unitCount * posMax ) +
                trackx;

            posIdx = Math.max( posIdx, 0 );
            posIdx = Math.min( posIdx, posMax );
            int x = posIdx + trackx;

            LogUtils.printDebug(
                "Calculated index %d (x = %d) for position = %d; posMax = %d; unitLength = %d, unitCount = %d",
                posIdx, x, position, posMax, unitLength, unitCount );

            return x;
        }

        /**
         * @param x
         * @param length
         * @param unitLength
         * @param trackx
         * @param trackWidth
         * @param thumbWidth
         * @return
         */
        private static long calculatePosition( int x, long length,
            long unitLength, int trackx, int trackWidth, int thumbWidth )
        {
            int posMax = trackWidth - thumbWidth - 1;
            int posIdx = x - trackx;

            posIdx = Math.max( posIdx, 0 );
            posIdx = Math.min( posIdx, posMax );

            long unitCount = calculateUnitCount( length, unitLength );
            long unitIndex = ( long )( posIdx / ( double )posMax * unitCount );

            long position = unitIndex * unitLength;

            LogUtils.printDebug(
                "Calculated position %d for posIdx = %d (x = %d); posMax = %d; unitLength = %d, unitCount = %d",
                position, x, posIdx, posMax, unitLength, unitCount );

            return position;
        }

        /**
         * Calculates and returns the number of units for the item length.
         * @return the number of units.
         */
        private static long calculateUnitCount( long length, long unitLength )
        {
            long count = 0;

            if( unitLength != 0 )
            {
                // count = ( length + unitLength - 1 ) / unitLength;
                count = length / unitLength;
            }

            return count;
        }
    }

    /***************************************************************************
     * Defines the mouse listener to move and interact with the thumb.
     **************************************************************************/
    private static final class PiMouseListener extends MouseAdapter
    {
        /** The position indicator listened to. */
        private final PositionIndicator indicator;
        /** The descriptor used to create a string for a position. */
        private final IDescriptor<Long> positionDescriptor;
        /**  */
        private final UpdaterList<MouseEvent> rightClickListeners;

        /** The last point at which the mouse was pressed. */
        private Point start;
        /**  */
        private int thumbxStart;
        /**  */
        private int dragx;
        /**  */
        private boolean dragging;

        /**
         * Creates a new listener with the provided indicator and description.
         * @param pi the position indicator listened to.
         * @param descriptor the descriptor used to create a string for a
         * position.
         */
        public PiMouseListener( PositionIndicator pi,
            IDescriptor<Long> descriptor )
        {
            this.indicator = pi;
            this.positionDescriptor = descriptor;
            this.rightClickListeners = new UpdaterList<>();

            this.start = null;
            this.thumbxStart = 0;
            this.dragx = 0;
            this.dragging = false;
        }

        /**
         * Adds a method to be invoked when the tab is right-clicked.
         * @param callback the method to be invoked.
         */
        public void addRightClick( IUpdater<MouseEvent> callback )
        {
            this.rightClickListeners.addUpdater( callback );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseClicked( MouseEvent e )
        {
            if( SwingUtilities.isLeftMouseButton( e ) &&
                !indicator.paintable.thumb.contains( e.getPoint() ) )
            {
                indicator.fireThumbMoved( e.getX() );
            }
            else if( RightClickListener.isRightClick( e ) )
            {
                rightClickListeners.fireListeners( e );
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mousePressed( MouseEvent e )
        {
            if( SwingUtilities.isLeftMouseButton( e ) )
            {
                start = e.getPoint();
                thumbxStart = indicator.paintable.thumb.x;
                dragx = start.x;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseDragged( MouseEvent e )
        {
            if( !SwingUtilities.isLeftMouseButton( e ) )
            {
                return;
            }

            if( start != null && indicator.paintable.thumb.contains( start ) )
            {
                dragging = true;
            }

            if( dragging && e.getX() != dragx )
            {
                dragx = e.getX();

                int delta = e.getX() - start.x;
                int thumbx = thumbxStart + delta;
                long position = indicator.paintable.calculatePosition( thumbx );
                indicator.posLabel.setText(
                    positionDescriptor.getDescription( position ) );

                Point csp = indicator.component.getLocationOnScreen();
                Point msp = e.getLocationOnScreen();

                msp.x = ( int )( csp.x + indicator.component.getWidth() / 2.0 -
                    indicator.positionWindow.getWidth() / 2.0 );
                msp.y = csp.y + indicator.component.getHeight() + 2;

                indicator.positionWindow.setLocation( msp );
                indicator.positionWindow.setVisible( true );

                indicator.firePositionUpdated( position );
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseReleased( MouseEvent e )
        {
            start = null;
            dragging = false;
            indicator.positionWindow.setVisible( false );

            indicator.component.repaint();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseMoved( MouseEvent e )
        {
            if( indicator.paintable.thumb.contains( e.getPoint() ) )
            {
                indicator.component.setCursor(
                    new Cursor( Cursor.HAND_CURSOR ) );
            }
            else
            {
                indicator.component.setCursor(
                    new Cursor( Cursor.DEFAULT_CURSOR ) );
            }
        }
    }
}
