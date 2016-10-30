package testbed;

import java.awt.*;

import javax.swing.*;

import org.jutils.ui.CollapsibleView;
import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;

public class CollapsibleViewMain implements IFrameApp
{
    @Override
    public JFrame createFrame()
    {
        JFrame frame = new JFrame( "Collapsible View Tester" );
        JPanel panel = new JPanel( new GridBagLayout() );
        CollapsibleView collapsibleView = new CollapsibleView();
        GridBagConstraints constraints;

        collapsibleView.setComponent( createInnerPanel() );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( new JLabel( "Test Field" ), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( new JTextField( "Test Field" ), constraints );

        constraints = new GridBagConstraints( 0, 1, 2, 1, 1.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( collapsibleView.getView(), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( new JLabel( "Test Field" ), constraints );

        constraints = new GridBagConstraints( 1, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( new JTextField( "Test Field" ), constraints );

        constraints = new GridBagConstraints( 0, 3, 2, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( Box.createHorizontalStrut( 0 ), constraints );

        frame.setContentPane( panel );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 500, 500 );

        return frame;
    }

    private static Component createInnerPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( new JLabel( "Inner Test Field" ), constraints );

        constraints = new GridBagConstraints( 1, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( new JTextField( "Inner Test Field" ), constraints );

        return panel;
    }

    @Override
    public void finalizeGui()
    {
    }

    public static void main( String[] args )
    {
        FrameApplication.invokeLater( new CollapsibleViewMain() );
    }
}
