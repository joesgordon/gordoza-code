package org.mc.ui;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.model.IView;
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

        frameView.getView().setIconImages( IconConstants.getImages(
            IconConstants.CHAT_16, IconConstants.CHAT_32, IconConstants.CHAT_48,
            IconConstants.CHAT_64 ) );

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
        addButton( panel, "UDP Server", l, r++ );

        l = ( e ) -> showView( new UdpClientView() );
        addButton( panel, "UDP Client", l, r++ );

        l = ( e ) -> showView( new TcpIpServerView() );
        addButton( panel, "TCP/IP Server", l, r++ );

        l = ( e ) -> showView( new TcpIpClientView() );
        addButton( panel, "TCP/IP Client", l, r++ );

        return panel;
    }

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

        OkDialogView odv = new OkDialogView( getView(), view.getView(),
            ModalityType.MODELESS, OkDialogButtons.OK_ONLY );

        odv.setOkButtonText( "Close" );

        odv.addOkListener( ( b ) -> {
            view.close();
            views.remove( view );
        } );

        odv.show();
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
        private final McFrame parent;

        public ClosingListener( McFrame parent )
        {
            this.parent = parent;
        }

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
