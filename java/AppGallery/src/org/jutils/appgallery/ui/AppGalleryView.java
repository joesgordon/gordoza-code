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
    private JPanel createView( List<ILibraryApp> apps )
    {
        JPanel panel = new JPanel( new BorderLayout() );
        JList<ILibraryApp> list = new JList<>();
        JScrollPane pane = new JScrollPane( list );

        list.setCellRenderer( new AppRenderer() );
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
    private static class AppRenderer extends DefaultListCellRenderer
    {
        public Component getListCellRendererComponent( JList<?> list,
            Object value, int index, boolean isSelected, boolean cellHasFocus )
        {
            Component comp = super.getListCellRendererComponent( list, value,
                index, isSelected, cellHasFocus );

            if( value != null )
            {
                ILibraryApp app = ( ILibraryApp )value;
                setIcon( app.getIcon32() );
                setText( app.getName() );
            }

            return comp;
        }
    }

    private static class ListMouseListener extends MouseAdapter
    {
        private final JList<ILibraryApp> list;

        public ListMouseListener( JList<ILibraryApp> list )
        {
            this.list = list;
        }

        public void mouseClicked( MouseEvent e )
        {
            if( e.getClickCount() == 2 )
            {
                ILibraryApp app = list.getSelectedValue();

                if( app != null )
                {
                    AppGalleryView.displayApp( app );
                }
            }
        }
    };
}
