package testbed;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.ui.USplitPane;

/**
 * 
 */
public class USnapPanel extends JPanel
{
    /**  */
    private JViewport viewport;
    /**  */
    private USplitPane splitPane;
    /**  */
    private Container mainView;
    /**  */
    private Container snapView;
    /**  */
    private int position;
    /**  */
    private boolean showingSnap;

    /**
     * @param mainView
     * @param snapView
     */
    public USnapPanel( Container mainView, Container snapView )
    {
        super( new BorderLayout() );

        viewport = new JViewport();
        splitPane = new USplitPane();
        position = -1;

        // splitPane.setBorderless( true );
        splitPane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        splitPane.setOrientation( JSplitPane.VERTICAL_SPLIT );
        splitPane.setDividerLocation( 200 );

        setMainView( mainView );
        setSnapView( snapView );

        add( viewport, BorderLayout.CENTER );
    }

    /**
     * @return
     */
    public Container getMainView()
    {
        return mainView;
    }

    /**
     * @param c
     */
    public void setMainView( Container c )
    {
        mainView = c;

        if( c != null )
        {
            c.setMinimumSize( new Dimension( 100, 100 ) );
        }

        if( showingSnap )
        {
            splitPane.setTopComponent( c );
        }
        else
        {
            viewport.setView( c );
        }
    }

    /**
     * @return
     */
    public Container getSnapView()
    {
        return snapView;
    }

    /**
     * @param c
     */
    public void setSnapView( Container c )
    {
        snapView = c;

        if( c != null )
        {
            c.setMinimumSize( new Dimension( 0, 0 ) );
        }

        splitPane.setBottomComponent( c );
    }

    /**
     * @return
     */
    public boolean isSnapViewShowing()
    {
        return showingSnap;
    }

    /**
     * @param show
     */
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
}
