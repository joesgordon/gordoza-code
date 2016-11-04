package testbed;

import java.awt.*;

import javax.swing.*;

import org.jutils.ui.calendar.CalendarPanel;

/*******************************************************************************
 *
 ******************************************************************************/
public class CalendarFrame extends JFrame
{
    /**  */
    private JPanel contentPane;

    /**  */
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    /**  */
    private CalendarPanel calPanel = new CalendarPanel();

    /***************************************************************************
     * 
     **************************************************************************/
    public CalendarFrame()
    {
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        contentPane = ( JPanel )getContentPane();
        contentPane.setLayout( gridBagLayout1 );
        setSize( new Dimension( 400, 300 ) );
        setTitle( "Frame Title" );

        calPanel.setDate( null );

        contentPane.add( calPanel.getView(),
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param args String[]
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel(
                        com.jgoodies.looks.Options.getCrossPlatformLookAndFeelClassName() );
                    // UIManager.setLookAndFeel( UIManager.
                    // getCrossPlatformLookAndFeelClassName() );
                }
                catch( Exception exception )
                {
                    exception.printStackTrace();
                }

                CalendarFrame frame = new CalendarFrame();
                // -------------------------------------------------------------
                // Validate frames that have preset sizes. Pack frames that have
                // useful preferred size info, e.g. from their layout.
                // -------------------------------------------------------------
                frame.validate();

                frame.setLocationRelativeTo( null );

                frame.setVisible( true );
            }
        } );
    }
}
