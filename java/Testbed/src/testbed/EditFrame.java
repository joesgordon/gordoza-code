package testbed;

import java.awt.*;

import javax.swing.*;

import com.jgoodies.looks.Options;

public class EditFrame extends JFrame
{
    private JTextPane chatEditorPane;

    public EditFrame()
    {
        JPanel contentPanel = new JPanel( new GridBagLayout() );

        chatEditorPane = new JTextPane();
        chatEditorPane.setBorder( BorderFactory.createLoweredBevelBorder() );

        contentPanel.add( chatEditorPane,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        // ChatterboxFrame.addComponentListeners( chatEditorPane );

        setContentPane( contentPanel );
    }

    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel( Options.PLASTICXP_NAME );
                }
                catch( Exception exception )
                {
                    exception.printStackTrace();
                }

                EditFrame frame = new EditFrame();
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setSize( 350, 400 );
                frame.validate();
                frame.setLocationRelativeTo( null );
                frame.setVisible( true );
            }
        } );
    }
}
