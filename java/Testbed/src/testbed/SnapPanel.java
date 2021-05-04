package testbed;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JViewport;
import javax.swing.border.EmptyBorder;

import org.jutils.core.ui.AltSplitPane;
import org.jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SnapPanel implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JViewport viewport;
    /**  */
    private final AltSplitPane splitPane;
    /**  */
    private final Container mainView;
    /**  */
    private final Container snapView;

    /**  */
    private int position;
    /**  */
    private boolean showingSnap;

    /***************************************************************************
     * @param mainView
     * @param snapView
     **************************************************************************/
    public SnapPanel( Container mainView, Container snapView )
    {
        this.view = new JPanel( new BorderLayout() );

        this.viewport = new JViewport();
        this.splitPane = new AltSplitPane();
        this.mainView = mainView;
        this.snapView = snapView;

        this.position = 200;
        this.showingSnap = false;

        // splitPane.setBorderless( true );
        splitPane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        splitPane.setOrientation( JSplitPane.VERTICAL_SPLIT );
        splitPane.setDividerLocation( position );

        mainView.setMinimumSize( new Dimension( 100, 100 ) );
        snapView.setMinimumSize( new Dimension( 0, 0 ) );

        splitPane.setBottomComponent( snapView );

        view.add( viewport, BorderLayout.CENTER );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Container getMainView()
    {
        return mainView;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Container getSnapView()
    {
        return snapView;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isSnapViewShowing()
    {
        return showingSnap;
    }

    /***************************************************************************
     * @param show
     **************************************************************************/
    public void setSnapViewShowing( boolean show )
    {
        showingSnap = show;

        if( show )
        {
            viewport.setView( splitPane );
            splitPane.setTopComponent( mainView );

            if( snapView != null )
            {
                if( position > -1 )
                {
                    splitPane.setDividerLocation( position );
                }
                else
                {
                    splitPane.repaint();
                    int x = ( int )( 0.8 * viewport.getHeight() );
                    splitPane.setDividerLocation( x );
                }
            }
            else
            {
                throw new IllegalStateException(
                    "Cannot show the snap view because it has never been set." );
            }
        }
        else
        {
            viewport.setView( mainView );
            position = splitPane.getDividerLocation();
            // splitPane.setDividerLocation( 1.0 );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }
}
