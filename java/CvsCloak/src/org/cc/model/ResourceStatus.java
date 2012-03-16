package org.cc.model;

public enum ResourceStatus
{
    UNKNOWN, ADDED, UP_TO_DATE, MODIFIED, NEEDS_UPDATE, CONFLICT, REMOVED;

    public String toString()
    {
        switch( this )
        {
            case UNKNOWN:
                return "Unknown";
            case ADDED:
                return "Unknown";
            case UP_TO_DATE:
                return "Up to date";
            case MODIFIED:
                return "Modified";
            case NEEDS_UPDATE:
                return "Needs update";
            case CONFLICT:
                return "Conflict";
            case REMOVED:
                return "Removed";
        }

        throw new RuntimeException( "Unknown status " + super.toString() );
    }

    public char toChar()
    {
        switch( this )
        {
            case UNKNOWN:
                return '?';
            case ADDED:
                return 'A';
            case UP_TO_DATE:
                return 'G';
            case MODIFIED:
                return 'M';
            case NEEDS_UPDATE:
                return 'U';
            case CONFLICT:
                return 'C';
            case REMOVED:
                return 'R';
        }

        throw new RuntimeException( "Unknown status " + super.toString() );
    }
}
