package org.jutils.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.LineBorder;

import org.jutils.data.UIProperty;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PositionIndicator implements IView<JComponent>
{
    /**  */
    private final JWindow posWin;
    /**  */
    private final JLabel posLabel;
    /**  */
    private final ItemActionList<Long> posititonListeners;
    /**  */
    private final PiComponent component;

    /***************************************************************************
     * 
     **************************************************************************/
    public PositionIndicator()
    {
        this.component = new PiComponent();
        this.posLabel = new JLabel();
        this.posWin = createWindow();
        this.posititonListeners = new ItemActionList<>();

        component.setMinimumSize( new Dimension( 20, 20 ) );
        component.setPreferredSize( new Dimension( 20, 20 ) );

        MouseListener ml = new MouseListener( this );

        component.addMouseListener( ml );
        component.addMouseMotionListener( ml );
    }

    /***************************************************************************
     * @return
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
     * @param l
     **************************************************************************/
    public void addPositionListener( ItemActionListener<Long> l )
    {
        posititonListeners.addListener( l );
    }

    /***************************************************************************
     * @param c
     **************************************************************************/
    public void setUnitColor( Color c )
    {
        component.thumbColor = c;
        component.repaint();
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void setLength( long l )
    {
        component.length = l;
        component.repaint();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getLength()
    {
        return component.length;
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void setUnitLength( long l )
    {
        component.unitLength = l;
        component.repaint();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getUnitLength()
    {
        return component.unitLength;
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void setOffset( long l )
    {
        component.offset = l;
        component.repaint();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getOffset()
    {
        return component.offset;
    }

    /***************************************************************************
     * @param x
     **************************************************************************/
    private void fireThumbMoved( int x )
    {
        long pos = getPosition( x );

        if( pos != component.offset )
        {
            posititonListeners.fireListeners( this, pos );
        }
    }

    /***************************************************************************
     * @param x
     * @return
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

        int posCnt = component.getWidth() - 1 - component.thumbRect.width;
        int posIdx = ( int )( xpc * posCnt );
        double posPc = posIdx / ( double )posCnt;

        int unitCount = component.getUnitCount();
        int unitIdx = ( int )( posPc * unitCount );
        long position = unitIdx * component.unitLength;

        return position;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class PiComponent extends JComponent
    {
        private static final long serialVersionUID = 3185302681799744337L;

        private final Rectangle thumbRect;

        private Color thumbColor;
        private Color thumbShadow;
        private long length;
        private long unitLength;
        private long offset;
        private Long dragOffset;

        public PiComponent()
        {
            this.thumbRect = new Rectangle();

            this.thumbColor = UIProperty.SCROLLBAR_THUMB.getColor();
            this.thumbShadow = UIProperty.SCROLLBAR_THUMBSHADOW.getColor();
            this.unitLength = 10;
            this.length = 0;
            this.offset = 50;
            this.dragOffset = null;
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        protected void paintComponent( Graphics g )
        {
            super.paintComponent( g );

            Graphics2D g2 = ( Graphics2D )g;

            Object aaHint = g2.getRenderingHint(
                RenderingHints.KEY_ANTIALIASING );

            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );

            long off = dragOffset == null ? offset : dragOffset;
            int unitCount = getUnitCount();
            int unitIndex = ( int )( ( double )unitCount * off / length );

            if( length == 0 )
            {
                return;
            }

            int x = 0;
            int y = 1;
            int w = ( int )( getWidth() * unitLength / ( double )length ) - 2;
            int h = getHeight() - 2;

            if( unitLength > 0 )
            {
                w = Math.max( w, 16 );
            }

            x = ( int )( unitIndex / ( double )unitCount *
                ( getWidth() - 2 ) ) + 1;

            if( x < 1 )
            {
                x = 1;
            }
            else if( x + w + 1 > getWidth() )
            {
                x = getWidth() - w - 1;
            }

            thumbRect.x = x;
            thumbRect.y = y;
            thumbRect.width = w;
            thumbRect.height = h;

            g2.setColor( thumbShadow );
            g2.fillRoundRect( x, y, w, h, 4, 4 );

            g2.setColor( thumbColor );
            g2.fillRoundRect( x + 1, y + 1, w - 2, h - 2, 4, 4 );

            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, aaHint );
        }

        /***************************************************************************
         * @return
         **************************************************************************/
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
     * 
     **************************************************************************/
    private static final class MouseListener extends MouseAdapter
    {
        private final PositionIndicator pi;

        private Point start;
        private boolean dragging = false;

        public MouseListener( PositionIndicator pi )
        {
            this.pi = pi;
            this.start = null;
        }

        @Override
        public void mouseClicked( MouseEvent e )
        {
            pi.fireThumbMoved( e.getX() );
        }

        @Override
        public void mousePressed( MouseEvent e )
        {
            start = e.getPoint();
        }

        @Override
        public void mouseDragged( MouseEvent e )
        {
            if( start != null && pi.component.thumbRect.contains( start ) )
            {
                dragging = true;
            }

            if( dragging )
            {
                pi.component.dragOffset = pi.getPosition( e.getX() );

                pi.component.repaint();

                pi.posLabel.setText(
                    String.format( "0x%016X", pi.getPosition( e.getX() ) ) );

                Point csp = pi.component.getLocationOnScreen();
                Point msp = e.getLocationOnScreen();

                msp.x = ( int )( csp.x + pi.component.getWidth() / 2.0 -
                    pi.posWin.getWidth() / 2.0 );
                msp.y = csp.y + pi.component.getHeight() + 2;

                pi.posWin.setLocation( msp );
                pi.posWin.setVisible( true );
            }
        }

        @Override
        public void mouseReleased( MouseEvent e )
        {
            boolean fire = dragging;

            pi.component.dragOffset = null;
            start = null;
            dragging = false;
            pi.posWin.setVisible( false );

            pi.component.repaint();

            if( fire )
            {
                pi.fireThumbMoved( e.getX() );
            }
        }
    }
}
