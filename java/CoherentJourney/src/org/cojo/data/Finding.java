package org.cojo.data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Finding
{
    public int id;
    public int userId;
    public LocalDateTime time;
    public String description;
    public boolean accepted;
    public String comments;

    /**
     * @param id
     * @param userId
     * @param description
     * @param accepted
     * @param comments
     */
    public Finding( int id, int userId, String description,
        boolean accepted, String comments )
    {
        this.id = id;
        this.userId = userId;
        this.time = LocalDateTime.now( ZoneOffset.UTC );
        this.description = description;
        this.accepted = accepted;
        this.comments = comments;
    }
}
