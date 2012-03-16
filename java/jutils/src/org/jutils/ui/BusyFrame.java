package org.jutils.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;

import javax.swing.*;

import org.jutils.ui.event.InertMouseListener;

/*******************************************************************************
 *
 ******************************************************************************/
public class BusyFrame extends JFrame
{
    private SeeThroughPane glassPane = new SeeThroughPane();

    /***************************************************************************
     * @throws HeadlessException
     **************************************************************************/
    public BusyFrame() throws HeadlessException
    {
        super();
        init();
    }

    /***************************************************************************
     * @param gc
     **************************************************************************/
    public BusyFrame( GraphicsConfiguration gc )
    {
        super( gc );
        init();
    }

    /***************************************************************************
     * @param title
     * @throws HeadlessException
     **************************************************************************/
    public BusyFrame( String title ) throws HeadlessException
    {
        super( title );
        init();
    }

    /***************************************************************************
     * @param title
     * @param gc
     **************************************************************************/
    public BusyFrame( String title, GraphicsConfiguration gc )
    {
        super( title, gc );
        init();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void init()
    {
        setGlassPane( glassPane );
        glassPane.setContentPane( getContentPane() );
        glassPane.addMouseListener( new InertMouseListener() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public final void setContentPane( Container contentPane )
    {
        super.setContentPane( contentPane );

        glassPane.setContentPane( contentPane );
    }

    /***************************************************************************
     * @param busy
     * @param message
     * @param cancelable
     * @param cancelAction
     **************************************************************************/
    public final void setFrameBusy( boolean busy, String message,
        boolean cancelable, ActionListener cancelAction )
    {
        BusySetter setter = new BusySetter( this, busy, message, cancelable,
            cancelAction );
        setter.run();
    }

    /***************************************************************************
     * @param busy
     * @param message
     * @param cancelable
     * @param cancelAction
     **************************************************************************/
    public final void setFrameBusyLater( boolean busy, String message,
        boolean cancelable, ActionListener cancelAction )
    {
        SwingUtilities.invokeLater( new BusySetter( this, busy, message,
            cancelable, cancelAction ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isBusy()
    {
        return glassPane.isVisible();
    }
}

class WaitingComponent extends JComponent implements ActionListener
{
    int whichDot = 0;

    public WaitingComponent()
    {
        this( 75 );
    }

    public WaitingComponent( int size )
    {
        setPreferredSize( new Dimension( size, size ) );
        setMinimumSize( new Dimension( size, size ) );

        Timer t = new Timer( 100, this );
        t.start();
    }

    public void actionPerformed( ActionEvent e )
    {
        whichDot++;
        if( whichDot > 7 )
        {
            whichDot = 0;
        }
        this.repaint();
    }

    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );

        double w = getWidth();
        double h = getHeight();
        double l = Math.min( w, h );

        double wmid = w / 2;
        double hmid = h / 2;
        double lmid = l / 2.0;

        double dia = ( int )( ( double )l / 5 );
        double rad = dia / 2.0;

        double l45 = lmid * Math.sqrt( 2.0 ) / 2.0;

        double x;
        double y;

        Color light = new Color( 85, 85, 85 );
        Color dark = new Color( 45, 45, 45 );

        // 0 degrees
        if( whichDot == 7 )
        {
            g.setColor( dark );
        }
        else
        {
            g.setColor( light );
        }
        x = wmid + lmid - dia;
        y = hmid - rad;
        g.fillOval( ( int )x, ( int )y, ( int )dia, ( int )dia );

        // 45 degrees
        if( whichDot == 6 )
        {
            g.setColor( dark );
        }
        else
        {
            g.setColor( light );
        }

        x = wmid + l45 - dia;
        y = hmid - l45;
        g.fillOval( ( int )x, ( int )y, ( int )dia, ( int )dia );

        // 90 degrees
        if( whichDot == 5 )
        {
            g.setColor( dark );
        }
        else
        {
            g.setColor( light );
        }

        x = wmid - rad;
        y = hmid - lmid;
        g.fillOval( ( int )x, ( int )y, ( int )dia, ( int )dia );

        // 135 degrees
        if( whichDot == 4 )
        {
            g.setColor( dark );
        }
        else
        {
            g.setColor( light );
        }

        x = wmid - l45;
        y = hmid - l45;
        g.fillOval( ( int )x, ( int )y, ( int )dia, ( int )dia );

        // 180 degrees
        if( whichDot == 3 )
        {
            g.setColor( dark );
        }
        else
        {
            g.setColor( light );
        }

        x = wmid - lmid;
        y = hmid - rad;
        g.fillOval( ( int )x, ( int )y, ( int )dia, ( int )dia );

        // -135 degrees
        if( whichDot == 2 )
        {
            g.setColor( dark );
        }
        else
        {
            g.setColor( light );
        }

        x = wmid - l45;
        y = hmid + l45 - dia;
        g.fillOval( ( int )x, ( int )y, ( int )dia, ( int )dia );

        // -90 degrees
        if( whichDot == 1 )
        {
            g.setColor( dark );
        }
        else
        {
            g.setColor( light );
        }

        x = wmid - rad;
        y = hmid + lmid - dia;
        g.fillOval( ( int )x, ( int )y, ( int )dia, ( int )dia );

        // -45 degrees
        if( whichDot == 0 )
        {
            g.setColor( dark );
        }
        else
        {
            g.setColor( light );
        }

        x = wmid + l45 - dia;
        y = hmid + l45 - dia;
        g.fillOval( ( int )x, ( int )y, ( int )dia, ( int )dia );
    }
}

class SeeThroughPane extends JComponent implements ActionListener
{
    private JLabel label = new JLabel();

    private JButton button = new JButton();

    private WaitingComponent waitLabel = new WaitingComponent();

    private Container contentPane = null;

    private ActionListener cancelAction = null;

    public SeeThroughPane()
    {
        Font f = label.getFont();

        setOpaque( false );
        setLayout( new GridBagLayout() );

        label.setText( "Status" );
        label.setHorizontalAlignment( SwingConstants.CENTER );
        label.setBorder( BorderFactory.createLoweredBevelBorder() );
        label.setFont( new Font( f.getName(), Font.BOLD, f.getSize() ) );
        label.setBackground( Color.white );
        label.setOpaque( true );

        button.setText( "Cancel" );
        button.addActionListener( this );
        button.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );

        add( label, new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 0,
                0, 20, 0 ), 20, 20 ) );

        add( button, new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 0,
                0, 0, 0 ), 20, 10 ) );

        add( waitLabel, new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 20,
                0, 0, 0 ), 0, 0 ) );
    }

    public void setCancelAction( ActionListener cancelAction )
    {
        this.cancelAction = cancelAction;
    }

    public void setCancelable( boolean cancelable )
    {
        button.setVisible( cancelable );
    }

    public void setText( String text )
    {
        if( text == null )
        {
            label.setText( "" );
            label.setVisible( false );
            return;
        }

        label.setVisible( true );
        label.setText( text );
    }

    public void actionPerformed( ActionEvent e )
    {
        setVisible( false );
        if( cancelAction != null )
        {
            cancelAction.actionPerformed( new ActionEvent( this, 0, "" ) );
        }
        cancelAction = null;
    }

    public void paint( Graphics g )
    {
        float[] my_kernel = { 0.10f, 0.10f, 0.10f, 0.10f, 0.20f, 0.10f, 0.10f,
            0.10f, 0.10f };
        ConvolveOp op = new ConvolveOp( new Kernel( 3, 3, my_kernel ) );
        BufferedImage buf = new BufferedImage( getWidth(), getHeight(),
            BufferedImage.TYPE_INT_ARGB );

        Graphics2D gr = ( Graphics2D )buf.getGraphics();

        contentPane.paint( gr );

        BufferedImage img = op.filter( buf, null );
        g.drawImage( img, 0, 0, null );

        Color ppColor = new Color( 25, 25, 25, 80 );
        g.setColor( ppColor );
        g.fillRect( 0, 0, this.getWidth(), this.getHeight() );

        super.paint( g );

    }

    public void setContentPane( Container contentPane )
    {
        this.contentPane = contentPane;
    }

    public void setVisible( boolean visible )
    {
        super.setVisible( visible );

        if( visible )
        {
            this.setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
        }
        else
        {
            this.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        }
    }
}

class BusySetter implements Runnable
{
    private BusyFrame frame = null;

    private boolean busy = false;

    private String message = null;

    private boolean cancelable = false;

    private ActionListener cancelAction = null;

    public BusySetter( BusyFrame frame, boolean busy, String message,
        boolean cancelable, ActionListener cancelAction )
    {
        this.frame = frame;
        this.busy = busy;

        this.message = message;
        this.cancelable = cancelable;
        this.cancelAction = cancelAction;
    }

    public void run()
    {
        SeeThroughPane pane = ( SeeThroughPane )frame.getGlassPane();

        pane.setText( message );
        pane.setCancelable( cancelable );
        pane.setCancelAction( cancelAction );
        pane.setVisible( busy );
    }
}
