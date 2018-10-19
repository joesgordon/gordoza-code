package org.taskflow.ui;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 *
 ******************************************************************************/
public class AddTaskView implements IDataView<String>
{
    /**  */
    private final JComponent view;
    /**  */
    private final JLabel promptField;
    /**  */
    private final StringFormField nameField;

    /***************************************************************************
     * 
     **************************************************************************/
    public AddTaskView()
    {
        this.promptField = new JLabel( "Please enter a task name" );
        this.nameField = new StringFormField( "Name" );
        this.view = createView();

        nameField.setValue( "New Task" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        StandardFormView form = new StandardFormView();

        form.addComponent( promptField );
        form.addField( nameField );

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
    public String getData()
    {
        return nameField.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( String value )
    {
        nameField.setValue( value );
    }
}
