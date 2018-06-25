package org.taskflow.data;

import java.util.GregorianCalendar;
import java.util.List;

import org.taskflow.model.INote;
import org.taskflow.model.IUpdate;

/**
 *
 */
public class Task
{
    /**  */
    public String title = "";
    /**  */
    public String description = "";
    /**  */
    public List<INote> notes;
    /**  */
    public List<IUpdate> updates;
    /**  */
    public GregorianCalendar dateCreated = new GregorianCalendar();
    /**  */
    public GregorianCalendar dateUpdated = new GregorianCalendar();
    /**  */
    public GregorianCalendar dateClosed = new GregorianCalendar();

    /**
     *
     */
    public Task()
    {
    }

    /**
     * @return String
     */
    @Override
    public String toString()
    {
        return title;
    }
}
