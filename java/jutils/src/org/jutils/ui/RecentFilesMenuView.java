package org.jutils.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.IDataView;

public class RecentFilesMenuView implements IDataView<List<File>>
{
    private final JMenu menu;
    private final ItemActionList<File> selectedListeners;
    private final int maxFileCount;

    private List<File> files;

    public RecentFilesMenuView()
    {
        this( 5 );
    }

    public RecentFilesMenuView( int maxFileCount )
    {
        this.maxFileCount = maxFileCount;
        this.menu = new JMenu( "Recent Files" );
        this.selectedListeners = new ItemActionList<>();
    }

    public void addSelectedListener( ItemActionListener<File> l )
    {
        selectedListeners.addListener( l );
    }

    @Override
    public JMenu getView()
    {
        return menu;
    }

    @Override
    public List<File> getData()
    {
        return files;
    }

    @Override
    public void setData( List<File> files )
    {
        JMenuItem item;

        this.files = files;

        menu.removeAll();
        int count = Math.min( maxFileCount, files.size() );

        for( int i = 0; i < count; i++ )
        {
            File file = files.get( i );
            item = new JMenuItem( ( i + 1 ) + " " + file.getName() );
            item.addActionListener( new ItemSelected( this, file ) );
            menu.add( item );
        }
    }

    private static class ItemSelected implements ActionListener
    {
        private final RecentFilesMenuView view;
        private final File file;

        public ItemSelected( RecentFilesMenuView view, File file )
        {
            this.view = view;
            this.file = file;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            view.selectedListeners.fireListeners( view, file );
        }
    }
}
