package org.jutils.appgallery.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.appgallery.ILibraryApp;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.model.IComponentView;
import org.jutils.ui.model.LabelListCellRenderer;
import org.jutils.ui.model.LabelListCellRenderer.IListCellLabelDecorator;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AppGalleryView implements IComponentView
{
    /**  */
    private final JPanel view;

    /***************************************************************************
     * @param apps
     **************************************************************************/
    public AppGalleryView( List<ILibraryApp> apps )
    {
        view = createView( apps );
    }

    /***************************************************************************
     * @param apps
     * @return
     **************************************************************************/
    private static JPanel createView( List<ILibraryApp> apps )
    {
        JPanel panel = new JPanel( new BorderLayout() );
        JList<ILibraryApp> list = new JList<>();
        JScrollPane pane = new JScrollPane( list );

        list.setCellRenderer(
            new LabelListCellRenderer( new AppCellDecorator() ) );
        list.setListData( apps.toArray( new ILibraryApp[apps.size()] ) );
        list.addMouseListener( new ListMouseListener( list ) );

        pane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        panel.add( pane, BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * @param app
     **************************************************************************/
    public static void displayApp( ILibraryApp app )
    {
        Component comp = app.createApp();
        JFrame frame;

        if( comp instanceof JFrame )
        {
            frame = ( JFrame )comp;
        }
        else
        {
            StandardFrameView frameView = new StandardFrameView();
            JPanel panel = new JPanel( new BorderLayout() );

            panel.add( comp, BorderLayout.CENTER );

            frameView.setContent( panel );

            frame = frameView.getView();

            frame.setTitle( app.getName() );
        }

        frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        frame.setSize( 500, 500 );
        frame.validate();
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class AppCellDecorator implements IListCellLabelDecorator
    {
        @Override
        public void decorate( JLabel label, JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus )
        {
            if( value != null )
            {
                ILibraryApp app = ( ILibraryApp )value;
                label.setIcon( app.getIcon32() );
                label.setText( app.getName() );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ListMouseListener extends MouseAdapter
    {
        private final JList<ILibraryApp> list;

        public ListMouseListener( JList<ILibraryApp> list )
        {
            this.list = list;
        }

        public void mouseClicked( MouseEvent e )
        {
            if( !SwingUtilities.isRightMouseButton( e ) &&
                e.getClickCount() == 2 && !e.isConsumed() )
            {
                ILibraryApp app = list.getSelectedValue();

                if( app != null )
                {
                    AppGalleryView.displayApp( app );
                }
            }
        }
    }
}
