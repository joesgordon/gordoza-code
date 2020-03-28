package testbed;

import java.awt.*;

import javax.swing.*;

import org.jutils.core.ui.IPaintable;
import org.jutils.core.ui.PaintingComponent;
import org.jutils.core.ui.app.FrameRunner;
import org.jutils.core.ui.app.IFrameApp;

/**
 * @see <a href="http://stackoverflow.com/questions/5069152">StackOverflow
 * Question 5069152</a>
 */
public class SplitPaneTest
{
    /**
     * 
     */
    private static class ColorXPaintable implements IPaintable
    {
        /**  */
        private final Color color;

        /**
         * @param color
         */
        public ColorXPaintable( Color color )
        {
            this.color = color;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void paint( JComponent c, Graphics2D g )
        {
            g.setColor( color );
            g.drawLine( 0, 0, c.getWidth(), c.getHeight() );
            g.drawLine( c.getWidth(), 0, 0, c.getHeight() );
        }

    }

    /**
     * @param args
     */
    public static void main( String[] args )
    {
        FrameRunner.invokeLater( new SplitPaneTestApp(), false );
    }

    /**
     * 
     */
    private static final class SplitPaneTestApp implements IFrameApp
    {
        /**  */
        private double ratio = 0.9;
        /**  */
        private double delta = ratio / 80;

        /**
         * @param jsp
         */
        private void updateLocation( JSplitPane jsp )
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

        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame createFrame()
        {
            JFrame f = new JFrame( "JSplitPane" );
            f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            PaintingComponent p1 = new PaintingComponent(
                new ColorXPaintable( Color.red ) );
            PaintingComponent p2 = new PaintingComponent(
                new ColorXPaintable( Color.blue ) );

            p1.setPreferredSize( new Dimension( 300, 300 ) );
            p2.setPreferredSize( new Dimension( 300, 300 ) );

            final JSplitPane jsp = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT,
                true, p1, p2 );
            Timer timer = new Timer( 50, ( e ) -> {
                updateLocation( jsp );
            } );

            f.add( jsp );
            timer.start();

            return f;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void finalizeGui()
        {
        }
    }
}
