package org.tuvok.data;

import java.util.*;

import org.tuvok.model.INote;
import org.tuvok.model.IUpdate;

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
    public String toString()
    {
        return title;
    }
}
