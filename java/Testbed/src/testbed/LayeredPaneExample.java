package testbed;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.*;

import org.jutils.apps.filespy.FileSpyMain;
import org.jutils.apps.filespy.ui.SearchView;
import org.jutils.ui.StatusBarPanel;
import org.jutils.ui.app.FrameApplication;
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

        panel.add( createLayers( statusBar ), BorderLayout.CENTER );
        panel.add( statusBar.getView(), BorderLayout.SOUTH );

        return panel;
    }

    private static JLayeredPane createLayers( StatusBarPanel statusBar )
    {
        JLayeredPane layeredPane = new JLayeredPane();
        JPanel pane = new SearchView( statusBar,
            FileSpyMain.getOptions() ).getView();

        pane.setBounds( 0, 0, 500, 500 );

        layeredPane.add( pane, new Integer( 100 ) );

        return layeredPane;
    }

    @Override
    public void finalizeGui()
    {
    }

    public static void main( String[] args )
    {
        FrameApplication.invokeLater( new LayeredPaneExample() );
    }
}
