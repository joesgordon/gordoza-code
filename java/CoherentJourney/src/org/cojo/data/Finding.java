package org.cojo.data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/***************************************************************************
 * 
 **************************************************************************/
public class Finding
{
    /** A unique identifier for this finding. */
    public int id;
    /** The user ID who created this finding. */
    public int userId;
    /** The time this finding was created. */
    public LocalDateTime time;
    /** The text of the finding. */
    public String description;
    /** {@code true} if the finding was accepted; {@code false} if rejected. */
    public boolean accepted;
    /** An explanation for rejection or other comments. */
    public String comments;

    /***************************************************************************
     * @param id
     * @param userId
     * @param description
     * @param accepted
     * @param comments
     **************************************************************************/
    public Finding( int id, int userId, String description, boolean accepted,
        String comments )
    {
        this.id = id;
        this.userId = userId;
        this.time = LocalDateTime.now( ZoneOffset.UTC );
        this.description = description;
        this.accepted = accepted;
        this.comments = comments;
    }
}
