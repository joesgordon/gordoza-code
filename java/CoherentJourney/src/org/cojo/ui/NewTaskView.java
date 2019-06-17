package org.cojo.ui;

import java.awt.Component;
import java.util.List;

import javax.swing.JComponent;

import org.cojo.data.ProjectManager;
import org.cojo.data.Task;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IDataView;

/***************************************************************************
 * 
 **************************************************************************/
public class NewTaskView implements IDataView<Task>
{
    /**  */
    private final JComponent view;
    /**  */
    private final StringFormField titleField;

    /**  */
    private final ProjectManager manager;

    /**  */
    private Task task;

    /***************************************************************************
     * @param project
     **************************************************************************/
    public NewTaskView( ProjectManager manager )
    {
        this.manager = manager;

        this.titleField = new StringFormField( "Title" );
        this.view = createView();

        setData( manager.createTask() );

        titleField.setUpdater( ( d ) -> setTitle( d ) );
    }

    /***************************************************************************
     * @param title
     **************************************************************************/
    private void setTitle( String title )
    {
        task.title = title;

        List<Task> tasks = manager.findTasks( title );
        // TODO search and display tasks with part of title
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( titleField );

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
    public Task getData()
    {
        return task;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Task task )
    {
        titleField.setValue( task.title );
        // TODO Auto-generated method stub
    }
}
