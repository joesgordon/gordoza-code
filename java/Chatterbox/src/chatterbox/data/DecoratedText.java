package chatterbox.data;

import org.jutils.core.data.TextStyleList;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DecoratedText
{
    /**  */
    public String text;
    /**  */
    public final TextStyleList attributes;

    /***************************************************************************
     * 
     **************************************************************************/
    public DecoratedText()
    {
        this.text = "";
        this.attributes = new TextStyleList();
    }

    /***************************************************************************
     * @param text
     * @param attributes
     **************************************************************************/
    public DecoratedText( String text, TextStyleList attributes )
    {
        this.text = text;
        this.attributes = attributes;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        return text;
    }
}
