package org.cc.edit.ui.panels.model;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.undo.UndoManager;

import org.cc.data.*;
import org.cc.edit.ui.InfoPanel;
import org.cc.edit.ui.panels.ListInfoPanel;
import org.cc.edit.ui.panels.NullableLockInfoPanel;
import org.jutils.Utils;
import org.jutils.ui.DirectoryChooser;
import org.jutils.ui.event.*;
import org.jutils.ui.event.updater.ComboBoxUpdater;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.model.ItemComboBoxModel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AppModelPanel extends InfoPanel<VersioningSystem>
{
    /**  */
    private final ItemComboBoxModel<File> defaultRepoModel;
    /**  */
    private final JComboBox<File> defaultField;
    /**  */
    private final NullableLockInfoPanel lockPanel;
    /**  */
    private final ListInfoPanel<Repository> reposPanel;
    /**  */
    private final ItemActionList<Repository> repoAddedListeners;
    /**  */
    private final ItemActionList<Repository> repoRemovedListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public AppModelPanel( UndoManager manager )
    {
        super( new GridBagLayout() );

        repoAddedListeners = new ItemActionList<Repository>();
        repoRemovedListeners = new ItemActionList<Repository>();

        defaultRepoModel = new ItemComboBoxModel<File>();
        defaultField = new JComboBox<File>( defaultRepoModel );
        lockPanel = new NullableLockInfoPanel();
        reposPanel = new ListInfoPanel<Repository>();

        ComboBoxUpdater<File> defaultUpdater = new ComboBoxUpdater<File>(
            defaultField, new DefaultRepoUpdater( this ) );

        defaultField.addActionListener( defaultUpdater );

        lockPanel.addLockSelectedListener( new LockSelectedListener() );

        reposPanel.setBorder( new TitledBorder( "Repositories" ) );
        reposPanel.addAddItemListener( new NewRepositoryListener() );
        reposPanel.addItemRemovedListener( new RemovedRepositoryListener() );

        add( new JLabel( "Default Repository :" ), new GridBagConstraints( 0,
            0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
            GridBagConstraints.NONE, new Insets( 4, 4, 0, 4 ), 0, 0 ) );
        add( defaultField, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                0, 4, 4, 4 ), 0, 0 ) );

        add( lockPanel, new GridBagConstraints( 0, 3, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                4, 4, 4, 4 ), 0, 0 ) );

        add( reposPanel, new GridBagConstraints( 0, 4, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 0, 4,
                4, 4 ), 0, 0 ) );

        add( Box.createVerticalStrut( 0 ), new GridBagConstraints( 0, 10, 4, 1,
            1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets( 0, 4, 0, 4 ), 0, 0 ) );
    }

    public void addRepoAddedListener( ItemActionListener<Repository> l )
    {
        repoAddedListeners.addListener( l );
    }

    public void addRepoRemovedListener( ItemActionListener<Repository> l )
    {
        repoRemovedListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected void displayData( VersioningSystem data )
    {
        defaultField.removeAllItems();
        for( int i = 0; i < data.getRepositories().size(); i++ )
        {
            defaultField.addItem( data.getRepositories().get( i ).getLocation() );
        }

        if( data.getDefaultRepository() != null )
        {
            defaultField.setSelectedItem( new File( data.getDefaultRepository() ) );
        }
        lockPanel.setData( data.getLockInfo() );

        reposPanel.setData( data.getRepositories() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class NewRepositoryListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent event )
        {
            Frame f = Utils.getComponentsFrame( reposPanel );
            DirectoryChooser d = new DirectoryChooser( f );

            if( reposPanel.getData().size() > 1 )
            {
                d.setSelectedPaths( reposPanel.getData().get( 0 ).getLocation().getAbsolutePath() );
            }

            d.setVisible( true );
            File[] files = d.getSelected();

            if( files != null && files.length == 1 )
            {
                Repository r = new Repository( files[0] );

                reposPanel.addItem( r );
                defaultField.addItem( r.getLocation() );
                getData().getRepositories().add( r );

                if( defaultField.getSelectedIndex() < 0 )
                {
                    defaultField.setSelectedIndex( 0 );
                }

                repoAddedListeners.fireListeners( AppModelPanel.this, r );
            }
            else if( files != null && files.length > 1 )
            {
                JOptionPane.showMessageDialog( f,
                    "A repository path cannot be multiple directories. Please "
                        + "select only one.", "ERROR",
                    JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class RemovedRepositoryListener implements
        ItemActionListener<Repository>
    {
        @Override
        public void actionPerformed( ItemActionEvent<Repository> event )
        {
            Repository r = event.getItem();
            defaultField.removeItem( r.getLocation() );
            getData().getRepositories().remove( r );

            repoRemovedListeners.fireListeners( AppModelPanel.this, r );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class LockSelectedListener implements ItemActionListener<LockInfo>
    {
        @Override
        public void actionPerformed( ItemActionEvent<LockInfo> event )
        {
            getData().setLockInfo( event.getItem() );
        }
    }

    private static class DefaultRepoUpdater implements IUpdater<File>
    {
        private final AppModelPanel panel;

        public DefaultRepoUpdater( AppModelPanel panel )
        {
            this.panel = panel;
        }

        @Override
        public void update( File selected )
        {
            VersioningSystem vs = panel.getData();
            String path = selected.getAbsolutePath();
            vs.setDefaultRepository( path );
        }
    }
}
