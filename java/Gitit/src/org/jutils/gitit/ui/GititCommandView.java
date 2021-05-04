package org.jutils.gitit.ui;

import javax.swing.JComponent;

import org.jutils.core.ui.StandardFormView;
import org.jutils.core.ui.fields.StringFormField;
import org.jutils.core.ui.model.IDataView;
import org.jutils.gitit.data.GititConfig.GititCommand;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GititCommandView implements IDataView<GititCommand>
{
    /**  */
    private JComponent view;
    /**  */
    private final StringFormField nameField;
    /**  */
    private final StringFormField commandField;

    /**  */
    private GititCommand command;

    /***************************************************************************
     * 
     **************************************************************************/
    public GititCommandView()
    {
        this.nameField = new StringFormField( "Name" );
        this.commandField = new StringFormField( "Command" );

        this.view = createView();

        setData( new GititCommand() );

        nameField.setUpdater( ( d ) -> command.name = d );
        commandField.setUpdater( ( d ) -> command.command = d );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( nameField );
        form.addField( commandField );

        return form.getView();
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
    public GititCommand getData()
    {
        return command;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( GititCommand command )
    {
        this.command = command;

        nameField.setValue( command.name );
        commandField.setValue( command.command );
    }
}
