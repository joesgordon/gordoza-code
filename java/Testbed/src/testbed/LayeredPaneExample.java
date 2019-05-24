package testbed;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import org.jutils.apps.filespy.ui.SearchParamsView;
import org.jutils.ui.StatusBarPanel;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;

public class LayeredPaneExample implements IFrameApp
{
    @Override
    public JFrame createFrame()
    {
        JFrame frame = new JFrame();

        frame.setTitle( "JLayeredPane example" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 500, 500 );

        frame.setContentPane( createContentPane() );
        frame.setLocationRelativeTo( null );

        return frame;
    }

    private static Container createContentPane()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        StatusBarPanel statusBar = new StatusBarPanel();

        panel.add( createLayers(), BorderLayout.CENTER );
        panel.add( statusBar.getView(), BorderLayout.SOUTH );

        return panel;
    }

    private static JLayeredPane createLayers()
    {
        JLayeredPane layeredPane = new JLayeredPane();
        JPanel pane = new SearchParamsView().getView();

        pane.setBounds( 0, 0, 500, 500 );

        layeredPane.add( pane, Integer.valueOf( 100 ) );

        return layeredPane;
    }

    @Override
    public void finalizeGui()
    {
    }

    public static void main( String[] args )
    {
        FrameRunner.invokeLater( new LayeredPaneExample() );
    }
}
