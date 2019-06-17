package org.cojo.ui;

import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.util.List;

import org.cojo.data.ProjectManager;
import org.cojo.data.ProjectUser;
import org.jutils.ui.ListView;
import org.jutils.ui.ListView.IItemListModel;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IDataView;

/**
 *
 */
public class UsersPanel implements IDataView<List<ProjectUser>>
{
    /**  */
    private final UsersListModel model;
    /**  */
    private final ListView<ProjectUser> view;

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
    public List<ProjectUser> getData()
    {
        return view.getData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setData( List<ProjectUser> users )
    {
        view.setData( users );
    }

    public void setProject( ProjectManager project )
    {
        model.setProject( project );
    }

    /**
     *
     */
    private static final class UsersListModel
        implements IItemListModel<ProjectUser>
    {
        /**  */
        private final StringFormField firstNameField;
        /**  */
        private final StringFormField lastNameField;
        /**  */
        private final Component newUserView;
        /**  */
        private ProjectManager project;

        public UsersListModel()
        {
            this.firstNameField = new StringFormField( "First Name" );
            this.lastNameField = new StringFormField( "Last Name" );
            this.newUserView = createForm();
        }

        private Component createForm()
        {
            StandardFormView form = new StandardFormView();

            form.addField( firstNameField );
            form.addField( lastNameField );

            return form.getView();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( ProjectUser user )
        {
            return user.getName();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ProjectUser promptForNew( ListView<ProjectUser> view )
        {
            ProjectUser user = null;

            OkDialogView dialogView = new OkDialogView( view.getView(),
                newUserView, ModalityType.DOCUMENT_MODAL,
                OkDialogButtons.OK_CANCEL );

            dialogView.setTitle( "Enter New User's Name" );

            if( dialogView.show() )
            {
                // TODO make sure user doen't exist
                user = project.createUser( firstNameField.getValue(),
                    lastNameField.getValue() );
            }

            return user;
        }

        /**
         * @param project
         */
        public void setProject( ProjectManager project )
        {
            this.project = project;
        }
    }
}
