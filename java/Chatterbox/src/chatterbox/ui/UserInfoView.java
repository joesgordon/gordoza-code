package chatterbox.ui;

import java.awt.Component;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.updater.ReflectiveUpdater;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IDataView;

import chatterbox.data.ChatUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UserInfoView implements IDataView<ChatUser>
{
    /**  */
    private final StringFormField userIdField;
    /**  */
    private final StringFormField displayNameField;
    /**  */
    private final StringFormField nickNameField;
    /**  */
    private final Component view;

    /**  */
    private ChatUser user;

    /***************************************************************************
     * 
     **************************************************************************/
    public UserInfoView()
    {
        this.userIdField = new StringFormField( "User ID" );
        this.displayNameField = new StringFormField( "Display Name" );
        this.nickNameField = new StringFormField( "Nickname" );

        this.view = createView();

        setData( new ChatUser( "blah" ) );

        nickNameField.setUpdater(
            new ReflectiveUpdater<>( this, "user.nickName" ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createView()
    {
        StandardFormView form = new StandardFormView();

        userIdField.setEditable( false );
        displayNameField.setEditable( false );

        form.addField( userIdField );
        form.addField( displayNameField );
        form.addField( nickNameField );

        return form.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public ChatUser getData()
    {
        return user;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( ChatUser data )
    {
        this.user = data;

        userIdField.setValue( user.userId );
        displayNameField.setValue( user.displayName );
        nickNameField.setValue( user.nickName );
    }
}
