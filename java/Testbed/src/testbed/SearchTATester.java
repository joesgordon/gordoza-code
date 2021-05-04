package testbed;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jutils.core.ui.FindOptions;
import org.jutils.core.ui.SearchableTextArea;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.app.FrameRunner;
import org.jutils.core.ui.app.IFrameApp;
import org.jutils.core.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class SearchTATester implements IView<JComponent>
{
    /**  */
    private final JPanel contentPane;
    /**  */
    private final JScrollPane textAreaPane;
    /**  */
    private final SearchableTextArea textArea;

    /***************************************************************************
     *
     **************************************************************************/
    public SearchTATester()
    {
        this.contentPane = new JPanel();
        this.textAreaPane = new JScrollPane();
        this.textArea = new SearchableTextArea();

        contentPane.setLayout( new GridBagLayout() );
        textAreaPane.setViewportView( textArea.getView() );

        contentPane.add( textAreaPane,
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 10, 10, 10, 10 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param args String[]
     **************************************************************************/
    public static void main( String[] args )
    {
        FrameRunner.invokeLater( new IFrameApp()
        {
            private SearchTATester taTester;

            @Override
            public void finalizeGui()
            {
                FindOptions o = new FindOptions();
                o.textToFind = "\\w.{6}\\s\\whe\\sp\\w\\S\\Sured";
                o.useRegex = true;
                o.matchCase = false;
                o.wrapAround = true;
                // textArea.searchAndHighlight( o );
                taTester.textArea.setOptions( o );
            }

            @Override
            public JFrame createFrame()
            {
                StandardFrameView view = new StandardFrameView();
                taTester = new SearchTATester();

                view.setContent( taTester.getView() );
                view.setSize( 400, 300 );
                view.setTitle( "Searchable Text Frame" );
                view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

                return view.getView();
            }
        } );
    }

    @Override
    public JComponent getView()
    {
        return contentPane;
    }
}
