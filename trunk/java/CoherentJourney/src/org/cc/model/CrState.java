package org.cc.model;

public enum CrState
{
    New, AwatingSccb, AwatingIccb, Deferred, Approved, InWork, Implemented, Closed;

    public String toString()
    {
        switch( this )
        {
            case New:
                return "New";
            case AwatingSccb:
                return "Awaiting SCCB";
            case AwatingIccb:
                return "Awaiting ICCB";
            case Deferred:
                return "Deferred";
            case Approved:
                return "Approved";
            case InWork:
                return "In Work";
            case Implemented:
                return "Implemented";
            case Closed:
                return "Closed";
        }

        throw new IllegalStateException( "Unknown value" );
    }
}
