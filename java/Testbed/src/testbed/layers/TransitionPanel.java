package testbed.layers;

import java.awt.BorderLayout;

import javax.swing.*;

public class TransitionPanel extends JPanel
{
    private JLayeredPane layeredPane;
    private int layer = 0;

    public TransitionPanel()
    {
        super( new BorderLayout() );

        layeredPane = new JLayeredPane();

        // layeredPane.setLayout( new FlowLayout() );
        layeredPane.setOpaque( false );

        add( layeredPane, BorderLayout.CENTER );
        setVisible( true );
    }

    public void transitionTo( JComponent comp )
    {
        System.out.println( "Adding shiznit to layer " + layer );
        comp.setVisible( true );
        layeredPane.add( comp, new Integer( layer ) );
        // layeredPane.add( new InternalFrame( "", comp ), new Integer( layer )
        // );
        layeredPane.moveToFront( comp );
        layer++;
    }
}
