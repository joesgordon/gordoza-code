package org.taskflow.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public LocalDateTime dateCreated;
    /**  */
    public LocalDateTime dateUpdated;
    /**  */
    public LocalDateTime dateClosed;

    /**
     *
     */
    public Task()
    {
        this.title = "";
        this.description = "";
        this.notes = new ArrayList<>();
        this.updates = new ArrayList<>();
        this.dateCreated = LocalDateTime.now();
        this.dateUpdated = dateCreated;
        this.dateClosed = null;
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
