package org.jutils.apps.filespy.data;

/*******************************************************************************
 *
 ******************************************************************************/
public class LineMatch
{
    private int lineNumber = -1;

    private String preMatch = null;

    private String match = null;

    private String postMatch = null;

    /***************************************************************************
     * @param str String
     * @param off int
     * @param len int
     **************************************************************************/
    public LineMatch( int lineNumber, String preMatch, String match,
        String postMatch )
    {
        this.lineNumber = lineNumber;
        this.preMatch = preMatch;
        this.match = match;
        this.postMatch = postMatch;
    }

    /***************************************************************************
     * @return int
     **************************************************************************/
    public int getLineNumber()
    {
        return lineNumber;
    }

    /***************************************************************************
     * @return String
     **************************************************************************/
    public String getPreUnmatched()
    {
        return preMatch;
    }

    /***************************************************************************
     * @return String
     **************************************************************************/
    public String getMatched()
    {
        return match;
    }

    /***************************************************************************
     * @return String
     **************************************************************************/
    public String getPostUnmatched()
    {
        return postMatch;
    }

    /***************************************************************************
     * @return String
     **************************************************************************/
    @SuppressWarnings( "unused")
    private String toHtml()
    {
        // TODO Use it or lose it
        return preMatch + "<b>" + match + "</b>" + postMatch;
    }

    /***************************************************************************
     * @return String
     **************************************************************************/
    public String toString()
    {
        return preMatch + match + postMatch;
    }
}
