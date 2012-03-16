package org.tuvok.model;

import java.util.List;

public interface ITask
{
    public List<ITask> getSubtasks();

    public List<IAttachment> getAttachments();

    public List<INote> getNotes();

    public List<IUpdate> getUpdates();

    public List<IEffort> getEfforts();

    public List<String> getTags();

    public String getTitle();

    public String getDescription();

    public boolean isManualCompleteOverride();

    public Priority getPriority();

    public float getPercentComplete();
}
