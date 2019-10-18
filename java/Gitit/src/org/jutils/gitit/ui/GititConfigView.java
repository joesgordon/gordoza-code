package org.jutils.gitit.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jutils.OptionUtils;
import org.jutils.SwingUtils;
import org.jutils.gitit.data.GititConfig;
import org.jutils.gitit.data.GititConfig.GititCommand;
import org.jutils.ui.DirectoryChooser;
import org.jutils.ui.ErrorView;
import org.jutils.ui.ItemListView;
import org.jutils.ui.ListView;
import org.jutils.ui.ListView.IItemListModel;
import org.jutils.ui.TitleView;
import org.jutils.ui.event.FileDropTarget;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 *
 ******************************************************************************/
public class GititConfigView implements IDataView<GititConfig>
{
    /**  */
    private final JPanel view;
    /**  */
    private final ListView<File> filesView;
    /**  */
    private final ItemListView<GititCommand> commandsView;

    /**  */
    private GititConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public GititConfigView()
    {
        this.filesView = new ListView<File>( new FileListModel() );
        this.commandsView = new ItemListView<GititCommand>(
            new GititCommandView(), new GititCommandListModel() );
        this.view = createView();
    }

    /***************************************************************************
     * @return the panel that represents this view.
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        TitleView title;
        GridBagConstraints constraints;

        FileDropTarget fileTarget = new FileDropTarget(
            ( e ) -> invokeFileDropped( e.getItem().getFiles() ) );

        JPanel filesPanel = filesView.getView();
        filesPanel.setDropTarget( fileTarget );
        filesPanel.setBorder( new EmptyBorder( 0, 8, 8, 8 ) );
        title = new TitleView( "Git Clones", filesPanel );
        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.5,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 6, 6, 0, 6 ), 0, 0 );
        panel.add( title.getView(), constraints );

        title = new TitleView( "Commands", commandsView.getView() );
        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.5,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 6, 6, 6, 6 ), 0, 0 );
        panel.add( title.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * @param files
     **************************************************************************/
    private void invokeFileDropped( List<File> files )
    {
        List<File> filesAdd = new ArrayList<>( files.size() );
        StringBuilder error = new StringBuilder();

        for( File f : files )
        {
            if( f.isDirectory() )
            {
                File gitDir = new File( f, ".git" );

                if( gitDir.isDirectory() )
                {
                    filesAdd.add( f );
                }
                else
                {
                    error.append( String.format( "  No .git found: %s\n",
                        f.getAbsolutePath() ) );
                }
            }
            else
            {
                error.append( String.format( "Not a directory: %s\n",
                    f.getAbsolutePath() ) );
            }
        }

        boolean addFiles = true;

        if( error.length() > 0 )
        {
            String [] choices = new String[] { "Continue", "Cancel" };
            ErrorView messagePanel = new ErrorView();

            messagePanel.setData( error.toString() );

            String choice = OptionUtils.showOptionMessage( getView(),
                messagePanel.getView(), "Invalid Files", choices, choices[0],
                true );

            addFiles = choice != null && choice.equals( choices[0] );
        }

        if( addFiles )
        {
            config.directories.addAll( filesAdd );
            setData( config );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public GititConfig getData()
    {
        return config;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( GititConfig config )
    {
        this.config = config;

        filesView.setData( config.directories );
        commandsView.setData( config.commands );
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static class FileListModel implements IItemListModel<File>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( File item )
        {
            return item == null ? "N/A" : item.getAbsolutePath();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public File promptForNew( ListView<File> view )
        {
            Window win = SwingUtils.getComponentsWindow( view.getView() );
            DirectoryChooser chooser = new DirectoryChooser( win,
                "Choose Git Clone Directory", "Choose Git Clone Directory" );
            List<File> list = view.getData();

            if( !list.isEmpty() )
            {
                chooser.setSelectedPaths(
                    list.get( list.size() - 1 ).getAbsolutePath() );
            }
            chooser.setVisible( true );

            File [] selected = chooser.getSelected();

            if( selected == null || selected.length == 0 )
            {
                return null;
            }

            return selected[0];
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static class GititCommandListModel
        implements IItemListModel<GititCommand>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( GititCommand item )
        {
            return item == null ? "N/A" : item.name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public GititCommand promptForNew( ListView<GititCommand> view )
        {
            GititCommand cmd = null;
            String name = view.promptForName( "Command" );

            if( name != null )
            {
                cmd = new GititCommand();
                cmd.name = name;
            }

            return cmd;
        }
    }
}
