package org.mc.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jutils.SwingUtils;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.model.IView;
import org.mc.MulticonIcons;
import org.mc.ui.BindingFrameView.IBindableView;
import org.mc.ui.net.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticonFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final List<IBindableView> views;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticonFrame()
    {
        this.frameView = new StandardFrameView();
        this.views = new ArrayList<>();

        frameView.getView().setIconImages( MulticonIcons.getMulticonImages() );

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

        l = ( e ) -> showView(
            new ConnectionBindableView( new MulticastView() ) );
        addButton( panel, MulticastView.NAME, l, r++ );

        l = ( e ) -> showView( new ConnectionBindableView( new UdpView() ) );
        addButton( panel, UdpView.NAME, l, r++ );

        l = ( e ) -> showView( new TcpServerView() );
        addButton( panel, TcpServerView.NAME, l, r++ );

        l = ( e ) -> showView(
            new ConnectionBindableView( new TcpClientView() ) );
        addButton( panel, TcpClientView.NAME, l, r++ );

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
    private void showView( IBindableView view )
    {
        views.add( view );

        BindingFrameView frame = new BindingFrameView( view, getView() );

        frame.getView().addWindowListener(
            new BindableClosingListener( view, this ) );

        frame.getView().setIconImages( getView().getIconImages() );
        frame.getView().pack();
        frame.getView().setLocationRelativeTo( getView() );
        frame.getView().setVisible( true );
    }

    /***************************************************************************
     * @param view
     **************************************************************************/
    private void closeView( IBindableView view )
    {
        try
        {
            view.unbind();
        }
        catch( IOException ex )
        {
            SwingUtils.showErrorMessage( getView(), ex.getMessage(),
                "Socket Close Error" );
        }
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
        private final MulticonFrame frame;

        /**
         * @param parent
         */
        public ClosingListener( MulticonFrame parent )
        {
            this.frame = parent;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void windowClosing( WindowEvent e )
        {
            for( IBindableView view : frame.views )
            {
                frame.closeView( view );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class BindableClosingListener extends WindowAdapter
    {
        /**  */
        private final IBindableView view;
        /**  */
        private final MulticonFrame frame;

        /**
         * @param view
         * @param frame
         */
        public BindableClosingListener( IBindableView view,
            MulticonFrame frame )
        {
            this.view = view;
            this.frame = frame;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void windowClosing( WindowEvent e )
        {
            frame.closeView( view );
        }
    }
}
