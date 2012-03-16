package org.duak.model;

public interface IProgressReporter
{
    public void setProgressIndeterminate();

    public void setprogress( double percent );

    public void setStatus( String status );
}
