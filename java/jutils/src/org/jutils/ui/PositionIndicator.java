package org.jutils.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.LineBorder;

import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PositionIndicator extends JComponent
{
    /**  */
    private static final long serialVersionUID = 3185302681799744337L;
    /**  */
    private final JWindow posWin;
    /**  */
    private final JLabel posLabel;
    /**  */
    private final ItemActionList<Long> posititonListeners;
    /**  */
    private final Rectangle thumbRect;

    /**  */
    private Color thumbColor;
    /**  */
    private Color thumbShadow;
    /**  */
    private long length;
    /**  */
    private long unitLength;
    /**  */
    private long offset;
    /**  */
    private Long dragOffset;

    /***************************************************************************
     * 
     **************************************************************************/
    public PositionIndicator()
    {
        this.thumbRect = new Rectangle();
        this.posLabel = new JLabel();
        this.posWin = createWindow();
        this.posititonListeners = new ItemActionList<>();

        this.thumbColor = UIManager.getColor( "ScrollBar.thumb" );
        this.thumbShadow = UIManager.getColor( "ScrollBar.thumbShadow" );
        this.unitLength = 10;
        this.length = 0;
        this.offset = 50;
        this.dragOffset = null;

        setMinimumSize( new Dimension( 20, 20 ) );
        setPreferredSize( new Dimension( 20, 20 ) );

        MouseListener ml = new MouseListener( this );

        this.addMouseListener( ml );
        this.addMouseMotionListener( ml );
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
        thumbColor = c;
        repaint();
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void setLength( long l )
    {
        length = l;
        repaint();
    }

    public long getLength()
    {
        return length;
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void setUnitLength( long l )
    {
        unitLength = l;
        repaint();
    }

    public long getUnitLength()
    {
        return unitLength;
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void setOffset( long l )
    {
        offset = l;
        repaint();
    }

    public long getOffset()
    {
        return offset;
    }

    /***************************************************************************
     * @param x
     **************************************************************************/
    private void fireThumbMoved( int x )
    {
        long pos = getPosition( x );

        if( pos != offset )
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
        int xCnt = getWidth() - 2;
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

        int posCnt = getWidth() - 1 - thumbRect.width;
        int posIdx = ( int )( xpc * posCnt );
        double posPc = posIdx / ( double )posCnt;

        int unitCount = getUnitCount();
        int unitIdx = ( int )( posPc * unitCount );
        long position = unitIdx * unitLength;

        return position;
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

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected void paintComponent( Graphics g )
    {
        super.paintComponent( g );

        Graphics2D g2 = ( Graphics2D )g;

        Object aaHint = g2.getRenderingHint( RenderingHints.KEY_ANTIALIASING );

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

        x = ( int )( unitIndex / ( double )unitCount * ( getWidth() - 2 ) ) + 1;

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
     * 
     **************************************************************************/
    private static class MouseListener extends MouseAdapter
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
            if( start != null && pi.thumbRect.contains( start ) )
            {
                dragging = true;
            }

            if( dragging )
            {
                pi.dragOffset = pi.getPosition( e.getX() );

                pi.repaint();

                pi.posLabel.setText(
                    String.format( "0x%016X", pi.getPosition( e.getX() ) ) );

                Point csp = pi.getLocationOnScreen();
                Point msp = e.getLocationOnScreen();

                msp.x = ( int )( csp.x + pi.getWidth() / 2.0 -
                    pi.posWin.getWidth() / 2.0 );
                msp.y = csp.y + pi.getHeight() + 2;

                pi.posWin.setLocation( msp );
                pi.posWin.setVisible( true );
            }
        }

        @Override
        public void mouseReleased( MouseEvent e )
        {
            boolean fire = dragging;

            pi.dragOffset = null;
            start = null;
            dragging = false;
            pi.posWin.setVisible( false );

            pi.repaint();

            if( fire )
            {
                pi.fireThumbMoved( e.getX() );
            }
        }
    }
}
