package testbed;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.*;

import org.jutils.apps.filespy.ui.SearchView;
import org.jutils.ui.FrameRunner;
import org.jutils.ui.StatusBarPanel;

public class LayeredPaneExample extends FrameRunner
{
    @Override
    protected JFrame createFrame()
    {
        JFrame frame = new JFrame();

        frame.setTitle( "JLayeredPane example" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 500, 500 );

        frame.setContentPane( createContentPane() );
        frame.setLocationRelativeTo( null );

        return frame;
    }

    private Container createContentPane()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        StatusBarPanel statusBar = new StatusBarPanel();

        panel.add( createLayers( statusBar ), BorderLayout.CENTER );
        panel.add( statusBar.getView(), BorderLayout.SOUTH );

        return panel;
    }

    private JLayeredPane createLayers( StatusBarPanel statusBar )
    {
        JLayeredPane layeredPane = new JLayeredPane();
        JPanel pane = new SearchView( statusBar ).getView();

        pane.setBounds( 0, 0, 500, 500 );

        layeredPane.add( pane, new Integer( 100 ) );

        return layeredPane;
    }

    @Override
    protected boolean validate()
    {
        return true;
    }

    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new LayeredPaneExample() );
    }
}
