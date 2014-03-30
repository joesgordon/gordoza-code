package testbed.layers;

import java.awt.*;

import javax.swing.*;

import org.jutils.apps.filespy.ui.RegexPanel;
import org.jutils.io.LogUtils;
import org.jutils.ui.*;

public class LayeredTestFrame extends JFrame
{
    public LayeredTestFrame()
    {
        super();

        setContentPane( createContentPane() );
    }

    private Container createContentPane()
    {
        JPanel contentPane = new JPanel( new BorderLayout() );
        TransitionPanel xpanel = new TransitionPanel();

        LogUtils.printDebug( "Adding shiznit" );
        xpanel.transitionTo( createPanel2() );
        xpanel.transitionTo( new JLabel( "blah" ) );

        contentPane.add( xpanel, BorderLayout.CENTER );
        contentPane.add( new StatusBarPanel().getView(), BorderLayout.SOUTH );

        return contentPane;
    }

    @SuppressWarnings( "unused")
    private JPanel createPanel1()
    {
        JPanel panel = new GradientPanel();
        panel.setBackground( Color.red );
        return panel;
    }

    private JPanel createPanel2()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        DirectoryTree dirTree = new DirectoryTree();
        JScrollPane jsp = new JScrollPane( dirTree.getView() );
        panel.setBorder( BorderFactory.createRaisedBevelBorder() );

        panel.add( jsp, BorderLayout.CENTER );

        return panel;
    }

    @SuppressWarnings( "unused")
    private JPanel createPanel4()
    {
        return new RegexPanel().getView();
    }
}
