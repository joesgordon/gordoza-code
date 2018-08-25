package org.taskflow.data;

import java.time.LocalDateTime;

/**
 *
 */
public class Record
{
    /**  */
    public String title;
    /**  */
    public String description;
    /**  */
    public LocalDateTime dateCreated;
    /**  */
    public LocalDateTime dateUpdated;

    /**
     *
     */
    public Record()
    {
        this.title = "";
        this.description = "";
        this.dateCreated = LocalDateTime.now();
        this.dateUpdated = dateCreated;
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
