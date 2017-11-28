package org.jutils.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.AbstractBorder;

import org.jutils.ui.model.IView;

/***************************************************************************
 * 
 **************************************************************************/
public class PopupWindow implements IView<JComponent>
{
    private final JPanel glassPane;
    private final JPanel view;
    private final JLabel dots;

    public PopupWindow( boolean resizable, JComponent comp )
    {
        this.dots = new JLabel( new DotsIcon() );
        this.view = createView( resizable, comp );
        this.glassPane = createGlassPane();

        DotsMouseListener dml = new DotsMouseListener( this );

        view.setFocusable( true );
        dots.addMouseListener( dml );
        dots.addMouseMotionListener( dml );
        glassPane.addMouseListener( new GlassClickListener( this ) );

        setBorder( new ShadowBorder() );
        // setBorder( new LineBorder( Color.gray ) );
    }

    private JPanel createView( boolean resizable, JComponent comp )
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( comp, BorderLayout.CENTER );

        if( resizable )
        {
            panel.add( createBottom(), BorderLayout.SOUTH );
        }

        return panel;
    }

    private JPanel createGlassPane()
    {
        JPanel panel = new JPanel( null );

        panel.setOpaque( false );

        panel.add( view );

        return panel;
    }

    private Component createBottom()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( new JSeparator( SwingConstants.HORIZONTAL ), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( dots, constraints );

        return panel;
    }

    public void show( Component parent, int x, int y )
    {
        JFrame w = ( JFrame )SwingUtilities.getRoot( parent );
        Point pos = new Point( x, y );

        pos = SwingUtilities.convertPoint( parent, pos, w.getGlassPane() );

        view.revalidate();
        view.setSize( view.getPreferredSize() );

        view.setBounds( pos.x, pos.y, view.getWidth(), view.getHeight() );

        // glassPane.addPropertyChangeListener( ( e ) -> {
        // LogUtils.printDebug( "%s = %s, was %s", e.getPropertyName(),
        // e.getNewValue(), e.getOldValue() );
        // } );
        //
        // LogUtils.printDebug( "Setting glass pane on " + w.getTitle() );

        w.setGlassPane( glassPane );

        // LogUtils.printDebug( "ps = %s", view.getPreferredSize() );
        // LogUtils.printDebug( "size = %s", view.getSize() );

        glassPane.setVisible( true );

        w.invalidate();
        w.validate();

        glassPane.requestFocus();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void hide()
    {
        JFrame w = ( JFrame )SwingUtilities.getRoot( view );
        JPanel panel = new JPanel( null );
        panel.setOpaque( false );

        w.setGlassPane( panel );
    }

    /***************************************************************************
     * @param lineBorder
     **************************************************************************/
    public void setBorder( AbstractBorder lineBorder )
    {
        view.setBorder( lineBorder );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return glassPane;
    }

    private static final class DotsIcon implements Icon
    {
        private static final int DOT_SIZE = 2;
        private static final int DOT_START = 2;
        private static final int DOT_STEP = 4;

        @Override
        public void paintIcon( Component c, Graphics g, int x, int y )
        {
            x = c.getWidth() - 2;
            y = c.getHeight() - 2;

            Graphics g2 = g.create();

            try
            {
                for( int dy = DOT_START, j = 2; j > 0; j--, dy += DOT_STEP )
                {
                    for( int dx = DOT_START, i = 0; i < j; i++, dx += DOT_STEP )
                    {
                        drawDot( g2, x - dx, y - dy );
                    }
                }
            }
            finally
            {
                g2.dispose();
            }
        }

        private void drawDot( Graphics g, int x, int y )
        {
            g.setColor( Color.WHITE );
            g.fillRect( x, y, DOT_SIZE, DOT_SIZE );
            g.setColor( new Color( 205, 205, 205 ) );
            g.fillRect( x - 1, y - 1, DOT_SIZE, DOT_SIZE );
        }

        @Override
        public int getIconWidth()
        {
            return 10;
        }

        @Override
        public int getIconHeight()
        {
            return 10;
        }
    }

    private static final class DotsMouseListener extends MouseAdapter
    {
        private final PopupWindow popup;
        private final Cursor defaultCursor;
        private final Cursor resizeCursor;

        private Point mouseStart;
        private Dimension startSize;
        private boolean isResizing;

        public DotsMouseListener( PopupWindow popup )
        {
            this.popup = popup;
            this.defaultCursor = Cursor.getPredefinedCursor(
                Cursor.DEFAULT_CURSOR );
            this.resizeCursor = Cursor.getPredefinedCursor(
                Cursor.SE_RESIZE_CURSOR );

            this.mouseStart = new Point( Integer.MIN_VALUE, Integer.MIN_VALUE );
            this.startSize = null;
            this.isResizing = false;
        }

        @Override
        public void mouseMoved( MouseEvent e )
        {
        }

        private Point toScreen( MouseEvent e )
        {
            Point p = e.getPoint();
            SwingUtilities.convertPointToScreen( p, e.getComponent() );
            return p;
        }

        @Override
        public void mousePressed( MouseEvent e )
        {
            mouseStart = toScreen( e );
            startSize = popup.view.getSize();
            isResizing = true;
            // LogUtils.printDebug( "dots pressed" );
        }

        @Override
        public void mouseReleased( MouseEvent e )
        {
            mouseStart = new Point( Integer.MIN_VALUE, Integer.MIN_VALUE );
            isResizing = false;
            // LogUtils.printDebug( "dots released" );
        }

        @Override
        public void mouseDragged( MouseEvent e )
        {
            if( !isResizing )
            {
                return;
            }
            // LogUtils.printDebug( "dots dragging" );

            Point p = toScreen( e );

            int dx = p.x - mouseStart.x;
            int dy = p.y - mouseStart.y;

            Dimension minDim = popup.view.getSize();
            Dimension newDim = new Dimension( startSize.width + dx,
                startSize.height + dy );

            if( newDim.width != minDim.width && newDim.height != minDim.height )
            {
                resize( newDim.width, newDim.height );
            }
        }

        private void resize( final int width, final int height )
        {
            // final Window window = ( Window )menu.getTopLevelAncestor();
            // window.pack();
            // LogUtils.printDebug( "resizing to %d x %d", width, height );
            Point pos = popup.view.getLocation();
            popup.view.setBounds( pos.x, pos.y, width, height );
            popup.view.repaint();
            popup.view.revalidate();
            // menu.validate();
        }

        @Override
        public void mouseEntered( MouseEvent e )
        {
            popup.dots.setCursor( resizeCursor );
        }

        @Override
        public void mouseExited( MouseEvent e )
        {
            popup.dots.setCursor( defaultCursor );
        }
    }

    private static final class GlassClickListener extends MouseAdapter
    {
        private final PopupWindow popup;

        public GlassClickListener( PopupWindow popup )
        {
            this.popup = popup;
        }

        @Override
        public void mouseClicked( MouseEvent e )
        {
            // LogUtils.printDebug( "mouse clicked" );

            evaluateEvent( e, false );
        }

        @Override
        public void mousePressed( MouseEvent e )
        {
            // LogUtils.printDebug( "mouse pressed" );

            evaluateEvent( e, false );
        }

        @Override
        public void mouseReleased( MouseEvent e )
        {
            // LogUtils.printDebug( "mouse released" );

            evaluateEvent( e, true );
        }

        public void evaluateEvent( MouseEvent e, boolean hide )
        {
            // TODO Auto-generated method stub

            // LogUtils.printDebug( "mouse clicked" );

            JFrame w = ( JFrame )SwingUtilities.getRoot( popup.getView() );

            Container contentPane = w.getContentPane();

            Point ept = e.getPoint();
            Component component = null;
            Point cp = SwingUtilities.convertPoint( popup.getView(), ept,
                contentPane );

            component = SwingUtilities.getDeepestComponentAt( contentPane, cp.x,
                cp.y );

            Point vp = popup.view.getLocation();
            Dimension vs = popup.view.getSize();
            Rectangle vr = new Rectangle( vp, vs );

            if( !vr.contains( cp ) )
            {
                if( component != null )
                {
                    int eventID = e.getID();

                    Component src = ( Component )e.getSource();

                    Point componentPoint = SwingUtilities.convertPoint( src,
                        ept, component );

                    if( hide )
                    {
                        popup.hide();
                    }

                    // LogUtils.printDebug(
                    // "redispatching event from %s @ %d,%d to %s @ %d,%d",
                    // src.getClass().getName(), ept.x, ept.y,
                    // component.getClass().getName(), componentPoint.x,
                    // componentPoint.y );

                    component.dispatchEvent( new MouseEvent( component, eventID,
                        e.getWhen(), e.getModifiers(), componentPoint.x,
                        componentPoint.y, e.getClickCount(),
                        e.isPopupTrigger() ) );
                }
                else
                {
                    // LogUtils.printDebug( "component is null" );
                    popup.hide();
                }
            }
        }
    }
}
