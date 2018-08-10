package org.mc.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jutils.ui.StandardFrameView;
import org.jutils.ui.model.IView;
import org.mc.McIcons;
import org.mc.ui.net.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final List<IConnectionView> views;

    /***************************************************************************
     * 
     **************************************************************************/
    public McFrame()
    {
        this.frameView = new StandardFrameView();
        this.views = new ArrayList<>();

        frameView.getView().setIconImages( McIcons.getMulticonImages() );

        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 400, 400 );
        frameView.setTitle( "Multicon" );
        frameView.setContent( createContent() );

        frameView.getView().addWindowListener( new ClosingListener( this ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Container createContent()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        int r = 0;
        ActionListener l;

        l = ( e ) -> showView( new MulticastView() );
        addButton( panel, "Multicast", l, r++ );

        l = ( e ) -> showView( new UdpServerView() );
        addButton( panel, "UDP Receiver", l, r++ );

        l = ( e ) -> showView( new UdpClientView() );
        addButton( panel, "UDP Sender", l, r++ );

        l = ( e ) -> showView( new TcpServerView() );
        addButton( panel, "TCP Server", l, r++ );

        l = ( e ) -> showView( new TcpClientView() );
        addButton( panel, "TCP Client", l, r++ );

        return panel;
    }

    /***************************************************************************
     * @param panel
     * @param text
     * @param listener
     * @param row
     **************************************************************************/
    private static void addButton( JPanel panel, String text,
        ActionListener listener, int row )
    {
        JButton button = new JButton( text );
        button.addActionListener( listener );
        GridBagConstraints constraints = new GridBagConstraints( 0, row, 1, 1,
            0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 6, 6, 6, 6 ), 6, 6 );
        panel.add( button, constraints );
    }

    /***************************************************************************
     * @param view
     **************************************************************************/
    private void showView( IConnectionView view )
    {
        views.add( view );

        StandardFrameView odv = new StandardFrameView();

        odv.setTitle( view.getTitle() );
        odv.setContent( view.getView() );

        odv.getView().addWindowListener( new WindowListener()
        {
            @Override
            public void windowOpened( WindowEvent e )
            {
            }

            @Override
            public void windowIconified( WindowEvent e )
            {
            }

            @Override
            public void windowDeiconified( WindowEvent e )
            {
            }

            @Override
            public void windowDeactivated( WindowEvent e )
            {
            }

            @Override
            public void windowClosing( WindowEvent e )
            {
                view.close();
                views.remove( view );
            }

            @Override
            public void windowClosed( WindowEvent e )
            {
            }

            @Override
            public void windowActivated( WindowEvent e )
            {
            }
        } );

        odv.getView().setIconImages( getView().getIconImages() );
        odv.getView().pack();
        odv.getView().setLocationRelativeTo( getView() );
        odv.getView().setVisible( true );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ClosingListener extends WindowAdapter
    {
        /**  */
        private final McFrame parent;

        /**
         * @param parent
         */
        public ClosingListener( McFrame parent )
        {
            this.parent = parent;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void windowClosing( WindowEvent e )
        {
            for( IConnectionView view : parent.views )
            {
                view.close();
            }
        }
    }
}
