package org.tuvok.data;

import java.util.GregorianCalendar;

/**
 *
 */
public class Record
{
    /**  */
    public String title = "";
    /**  */
    public String description = "";
    /**  */
    public GregorianCalendar dateCreated = new GregorianCalendar();
    /**  */
    public GregorianCalendar dateUpdated = new GregorianCalendar();

    /**
     *
     */
    public Record()
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
