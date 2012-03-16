package testbed.layers;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.Timer;

import org.jutils.ui.FrameRunner;
import org.jutils.ui.UToolBar;

/*******************************************************************************
 * This class manages a group of {@link JPanel}s. This class provides methods
 * for easily switching between panels, and provides a fading animation while
 * switching.
 ******************************************************************************/
public class JFadePanel extends JPanel
{
    private CardLayout layout;
    private Timer fadeTimer;
    private float alpha;

    protected List<? extends JPanel> panels;

    private boolean fading = false;

    /***************************************************************************
     * Constructor
     * @param components
     **************************************************************************/
    public JFadePanel( JPanel... components )
    {
        this( Arrays.asList( components ) );
    }

    /***************************************************************************
     * Constructor
     * @param components
     **************************************************************************/
    public JFadePanel( List<? extends JPanel> components )
    {
        panels = new ArrayList<JPanel>( components );
        layout = new CardLayout();

        setLayout( layout );

        for( JComponent comp : this.panels )
        {
            add( comp, comp.toString() );
        }

        fadeTimer = new Timer( 50, new FadeListener() );

        alpha = 1.0f;
    }

    /***************************************************************************
     * Returns the total number of panels.
     * @return
     **************************************************************************/
    public int getPanelCount()
    {
        return panels.size();
    }

    /***************************************************************************
     * Shows the next panel.
     **************************************************************************/
    public void next()
    {
        layout.next( this );
        fade();
    }

    /***************************************************************************
     * Shows the last panel in the list.
     **************************************************************************/
    public void last()
    {
        layout.last( this );
        fade();
    }

    /***************************************************************************
     * Shows the first panel in the list.
     **************************************************************************/
    public void first()
    {
        layout.first( this );
        fade();
    }

    /***************************************************************************
     * Shows the previous panel.
     **************************************************************************/
    public void previous()
    {
        layout.previous( this );
        fade();
    }

    /***************************************************************************
     * Fades in the current panel.
     **************************************************************************/
    private void fade()
    {
        alpha = 0.0f;
        fading = true;
        fadeTimer.start();
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     */
    @Override
    public void paint( Graphics g )
    {
        if( fading )
        {
            Graphics2D g2d = ( Graphics2D )g;
            BufferedImage bi = new BufferedImage( getWidth(), getHeight(),
                BufferedImage.TYPE_INT_ARGB );
            super.paint( bi.getGraphics() );
            g2d.setComposite( AlphaComposite.SrcOver.derive( alpha ) );
            g2d.drawImage( bi, 0, 0, null );
        }
        else
        {
            super.paint( g );
        }
    }

    // Utility classes

    /***************************************************************************
     * 
     **************************************************************************/
    private class FadeListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            alpha += 0.05f;
            repaint();
            if( alpha >= 1.0f )
            {
                alpha = 1.0f;
                fadeTimer.stop();
                fading = false;
            }
        }
    }

    /***************************************************************************
     * JFadePanel demo
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        class DemoRunner extends FrameRunner
        {
            @Override
            protected JFrame createFrame()
            {
                JFrame frame = new JFrame();

                JPanel panel1 = new JPanel();
                panel1.add( new JButton( "A button" ) );
                panel1.add( new JTextField( "a text field" ) );

                JPanel panel2 = new JPanel();
                panel2.add( new JCheckBox( "A check box" ) );
                panel2.add( new JButton( "another button" ) );

                JPanel panel3 = new JPanel();
                panel3.add( new JRadioButton( "a radio button" ) );
                panel3.add( new JLabel( "a label" ) );

                JPanel panel4 = new JPanel();
                panel4.add( new JTextArea( "an editor pane" ) );

                final JFadePanel fp = new JFadePanel( panel1, panel2, panel3,
                    panel4 );
                JButton first = new JButton( "|<-" );
                first.setFocusable( false );
                first.addActionListener( new ActionListener()
                {
                    @Override
                    public void actionPerformed( ActionEvent e )
                    {
                        fp.first();
                    }
                } );

                JButton last = new JButton( "->|" );
                last.setFocusable( false );
                last.addActionListener( new ActionListener()
                {
                    @Override
                    public void actionPerformed( ActionEvent e )
                    {
                        fp.last();
                    }
                } );

                JButton next = new JButton( "->" );
                next.setFocusable( false );
                next.addActionListener( new ActionListener()
                {
                    @Override
                    public void actionPerformed( ActionEvent e )
                    {
                        fp.next();
                    }
                } );

                JButton prev = new JButton( "<-" );
                prev.setFocusable( false );
                prev.addActionListener( new ActionListener()
                {
                    @Override
                    public void actionPerformed( ActionEvent e )
                    {
                        fp.previous();
                    }
                } );

                JToolBar toolbar = new UToolBar();
                toolbar.setFloatable( false );
                toolbar.setFocusable( false );
                toolbar.add( first );
                toolbar.add( prev );
                toolbar.add( next );
                toolbar.add( last );

                frame.setTitle( "JFadePanel Demo" );
                frame.setSize( 350, 400 );
                frame.setLayout( new BorderLayout() );
                frame.add( toolbar, BorderLayout.PAGE_START );
                frame.add( fp, BorderLayout.CENTER );

                return frame;
            }

            @Override
            protected boolean validate()
            {
                return false;
            }
        }

        SwingUtilities.invokeLater( new DemoRunner() );
    }
}
