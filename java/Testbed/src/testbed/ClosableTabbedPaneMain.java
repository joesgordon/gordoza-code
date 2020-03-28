package testbed;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jutils.core.ui.ClosableTabbedPane;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.app.FrameRunner;
import org.jutils.core.ui.app.IFrameApp;

/*******************************************************************************
 * ClosableTabbedPane example
 ******************************************************************************/
public class ClosableTabbedPaneMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        FrameRunner.invokeLater( new ClosableTabbedPaneApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ClosableTabbedPaneApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            StandardFrameView frame = new StandardFrameView();
            ClosableTabbedPane tabs = new ClosableTabbedPane();

            frame.setTitle( "ClosableTabbedPane Example" );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frame.setSize( 500, 500 );

            frame.setContent( tabs );

            tabs.addTab( "Tab 0", new JPanel() );
            tabs.addTab( "Tab 1", new JPanel() );
            tabs.addTab( "Tab 2", new JPanel() );
            tabs.addTab( "Tab 3", new JPanel() );

            return frame.getView();
        }

        @Override
        public void finalizeGui()
        {
        }
    }
}
