package org.cojo.ui;

import java.awt.Component;
import java.util.List;

import org.cojo.data.Project;
import org.cojo.data.User;
import org.jutils.ui.ListView;
import org.jutils.ui.ListView.IItemListModel;
import org.jutils.ui.model.IDataView;

/**
 *
 */
public class UsersPanel implements IDataView<List<User>>
{
    /**  */
    private final UsersListModel model;
    /**  */
    private final ListView<User> view;

    /**
     * 
     */
    public UsersPanel()
    {
        this.model = new UsersListModel();
        this.view = new ListView<>( model );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getView()
    {
        return view.getView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getData()
    {
        return view.getData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setData( List<User> users )
    {
        view.setData( users );
    }

    public void setProject( Project project )
    {
        model.setProject( project );
    }

    /**
     *
     */
    private static final class UsersListModel implements IItemListModel<User>
    {
        /**  */
        private Project project;

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( User user )
        {
            return user.name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public User promptForNew( ListView<User> view )
        {
            User user = null;
            // boolean done = false;

            // while( !done )
            {
                String name = view.promptForName( "user name" );

                if( name != null )
                {
                    // TODO make sure user doen't exist
                    user = project.createUser( name );
                    // done = true;
                }
                // else
                // {
                // done = true;
                // }
            }

            return user;
        }

        public void setProject( Project project )
        {
            this.project = project;
        }
    }
}
