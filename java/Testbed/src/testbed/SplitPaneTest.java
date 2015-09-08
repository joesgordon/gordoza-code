package testbed;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * @see http://stackoverflow.com/questions/5069152
 */
public class SplitPaneTest
{
    double ratio = 0.9;
    double delta = ratio / 80;

    private void create()
    {
        JFrame f = new JFrame( "JSplitPane" );
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        MyPanel p1 = new MyPanel( Color.red );
        MyPanel p2 = new MyPanel( Color.blue );
        final JSplitPane jsp = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT,
            true, p1, p2 );
        Timer timer = new Timer( 50, new ActionListener()
        {

            @Override
            public void actionPerformed( ActionEvent e )
            {
                ratio += delta;
                if( ratio >= 1.0 )
                {
                    ratio = 1.0;
                    delta = -delta;
                }
                else if( ratio <= 0 )
                {
                    delta = -delta;
                    ratio = 0;
                }
                jsp.setDividerLocation( ratio );
            }
        } );

        f.add( jsp );
        f.pack();
        f.setLocationRelativeTo( null );
        f.setVisible( true );
        timer.start();
    }

    private static class MyPanel extends JPanel
    {

        Color color;

        public MyPanel( Color color )
        {
            this.color = color;
            this.setPreferredSize( new Dimension( 300, 300 ) );
        }

        @Override
        public void paintComponent( Graphics g )
        {
            super.paintComponent( g );
            g.setColor( color );
            g.drawLine( 0, 0, getWidth(), getHeight() );
            g.drawLine( getWidth(), 0, 0, getHeight() );
        }
    }

    public static void main( String[] args )
    {
        EventQueue.invokeLater( new Runnable()
        {

            @Override
            public void run()
            {
                new SplitPaneTest().create();
            }
        } );
    }
}
