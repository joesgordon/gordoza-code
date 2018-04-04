package org.jutils.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.LineBorder;

import org.jutils.data.UIProperty;
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
     * @param x the x-position in the {@link #component}'s coordinate space of
     * the left of the thumb.
     **************************************************************************/
    private void fireThumbMoved( int x )
    {
        long pos = getPosition( x );

        fireThumbMoved( pos );
    }

    /***************************************************************************
     * Invokes the callback on all listeners to the position change.
     * @param position the new indicated positon.
     **************************************************************************/
    private void fireThumbMoved( long position )
    {
        if( position != paintable.position )
        {
            posititonListeners.fireListeners( this, position );
        }
    }

    /***************************************************************************
     * Gets the indicated position based on the x-coordinate of the thumb.
     * @param x the x-position in the {@link #component}'s coordinate space of
     * the left of the thumb.
     * @return the calculated position.
     **************************************************************************/
    private long getPosition( final int x )
    {
        int xCnt = component.getWidth() - 2;
        int xIdx = x - 1;

        if( xIdx < 0 )
        {
            xIdx = 0;
        }
        else if( xIdx >= xCnt )
        {
            xIdx = xCnt - 1;
        }

        double xpc = xIdx / ( double )( xCnt );

        int posCnt = component.getWidth() - 1 - paintable.thumbRect.width;
        int posIdx = ( int )( xpc * posCnt );
        double posPc = posIdx / ( double )posCnt;

        int unitCount = paintable.getUnitCount();
        int unitIdx = ( int )( posPc * unitCount );
        long position = unitIdx * paintable.unitLength;

        return position;
    }

    /***************************************************************************
     * Defines the object that draws the control.
     **************************************************************************/
    private static final class PositionIndicatorPaintable implements IPaintable
    {
        /** The bounds of the thumb. Defaults to zeros. */
        private final Rectangle thumbRect;

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
        /** The indicated position of the item while dragging. */
        private Long dragPosition;

        /**
         * Creates a new paintable.
         */
        public PositionIndicatorPaintable()
        {
            this.thumbRect = new Rectangle();

            this.thumbColor = UIProperty.SCROLLBAR_THUMB.getColor();
            this.thumbShadow = UIProperty.SCROLLBAR_THUMBSHADOW.getColor();
            this.unitLength = 10;
            this.length = 0;
            this.position = 50;
            this.dragPosition = null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void paint( JComponent c, Graphics2D g2 )
        {
            Rectangle bounds = c.getBounds();
            Insets insets = c.getInsets();

            bounds.x = insets.left;
            bounds.y = insets.top;
            bounds.width -= ( insets.left + insets.right );
            bounds.height -= ( insets.top + insets.bottom );

            int width = bounds.width;
            int height = bounds.height;

            Object aaHint = g2.getRenderingHint(
                RenderingHints.KEY_ANTIALIASING );

            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );

            long off = dragPosition == null ? position : dragPosition;
            int unitCount = getUnitCount();
            int unitIndex = ( int )( ( double )unitCount * off / length );

            if( length == 0 )
            {
                return;
            }

            int x = bounds.x;
            int y = bounds.y;
            int w = ( int )( width * unitLength / ( double )length ) - 2;
            int h = height;

            if( unitLength > 0 )
            {
                w = Math.max( w, 16 );
            }

            x = ( int )( unitIndex / ( double )unitCount * ( width - 2 ) ) +
                bounds.x;

            if( x < bounds.x )
            {
                x = bounds.x;
            }
            else if( x + w + bounds.x > bounds.x + bounds.width )
            {
                x = bounds.x + width - w - 1;
            }

            thumbRect.x = x;
            thumbRect.y = y;
            thumbRect.width = w;
            thumbRect.height = h;

            g2.setColor( thumbShadow );
            g2.fillRoundRect( x, y, w, h, 5, 5 );

            g2.setColor( thumbColor );
            g2.fillRoundRect( x + 1, y + 1, w - 2, h - 2, 4, 4 );

            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, aaHint );
        }

        /**
         * Calculates and returns the number of units for the item length.
         * @return the number of units.
         */
        private int getUnitCount()
        {
            int count = 0;

            if( unitLength != 0 )
            {
                count = ( int )( ( length + unitLength - 1 ) / unitLength );
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
        private final PositionIndicator pi;
        /** The descriptor used to create a string for a position. */
        private final IDescriptor<Long> positionDescriptor;
        /**  */
        private final UpdaterList<MouseEvent> rightClickListeners;

        /** The last point at which the mouse was pressed. */
        private Point start;
        /**  */
        private boolean dragging = false;

        /**
         * Creates a new listener with the provided indicator and description.
         * @param pi the position indicator listened to.
         * @param descriptor the descriptor used to create a string for a
         * position.
         */
        public PiMouseListener( PositionIndicator pi,
            IDescriptor<Long> descriptor )
        {
            this.pi = pi;
            this.positionDescriptor = descriptor;
            this.start = null;
            this.rightClickListeners = new UpdaterList<>();
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
            if( e.isConsumed() && SwingUtilities.isLeftMouseButton( e ) &&
                !pi.paintable.thumbRect.contains( e.getPoint() ) )
            {
                pi.fireThumbMoved( e.getX() );
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

            if( start != null && pi.paintable.thumbRect.contains( start ) )
            {
                dragging = true;
            }

            if( dragging )
            {
                pi.paintable.dragPosition = pi.getPosition( e.getX() );

                pi.component.repaint();

                long position = pi.getPosition( e.getX() );
                pi.posLabel.setText(
                    positionDescriptor.getDescription( position ) );

                Point csp = pi.component.getLocationOnScreen();
                Point msp = e.getLocationOnScreen();

                msp.x = ( int )( csp.x + pi.component.getWidth() / 2.0 -
                    pi.positionWindow.getWidth() / 2.0 );
                msp.y = csp.y + pi.component.getHeight() + 2;

                pi.positionWindow.setLocation( msp );
                pi.positionWindow.setVisible( true );

                pi.fireThumbMoved( position );
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseReleased( MouseEvent e )
        {
            boolean fire = dragging;

            pi.paintable.dragPosition = null;
            start = null;
            dragging = false;
            pi.positionWindow.setVisible( false );

            pi.component.repaint();

            if( fire )
            {
                pi.fireThumbMoved( e.getX() );
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseMoved( MouseEvent e )
        {
            if( pi.paintable.thumbRect.contains( e.getPoint() ) )
            {
                pi.component.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
            }
            else
            {
                pi.component.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
            }
        }
    }
}
