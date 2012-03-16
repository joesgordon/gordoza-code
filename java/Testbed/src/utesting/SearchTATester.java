package utesting;

import java.awt.*;

import javax.swing.*;

import org.jutils.ui.FindOptions;
import org.jutils.ui.SearchableTextArea;

/*******************************************************************************
 *
 ******************************************************************************/
public class SearchTATester extends JFrame
{
    private JPanel contentPane;

    private JScrollPane textAreaPane = new JScrollPane();

    private SearchableTextArea textArea = new SearchableTextArea();

    /***************************************************************************
     *
     **************************************************************************/
    public SearchTATester()
    {
        try
        {
            setDefaultCloseOperation( EXIT_ON_CLOSE );
            jbInit();
        }
        catch( Exception exception )
        {
            exception.printStackTrace();
        }
    }

    /***************************************************************************
     * @throws java.lang.Exception
     **************************************************************************/
    private void jbInit() throws Exception
    {
        contentPane = ( JPanel )getContentPane();
        contentPane.setLayout( new GridBagLayout() );
        setSize( new Dimension( 400, 300 ) );
        setTitle( "Searchable Text Frame" );
        textArea.setToolTipText( "Press CTRL+F to find text" );
        textAreaPane.setViewportView( textArea );

        contentPane.add( textAreaPane, new GridBagConstraints( 0, 1, 1, 1, 1.0,
            1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 10, 10, 10, 10 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param vis boolean
     **************************************************************************/
    public void setVisible( boolean vis )
    {
        super.setVisible( vis );
        if( vis )
        {
            FindOptions o = new FindOptions();
            o.textToFind = "\\w.{6}\\s\\whe\\sp\\w\\S\\Sured";
            o.useRegex = true;
            o.matchCase = false;
            o.wrapAround = true;
            // textArea.searchAndHighlight( o );
            textArea.setOptions( o );
        }

    }

    /***************************************************************************
     * @param args String[]
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel( com.jgoodies.looks.Options.getCrossPlatformLookAndFeelClassName() );

                    // UIManager.setLookAndFeel( UIManager.
                    // getCrossPlatformLookAndFeelClassName() );
                }
                catch( Exception exception )
                {
                    exception.printStackTrace();
                }

                SearchTATester frame = new SearchTATester();
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
