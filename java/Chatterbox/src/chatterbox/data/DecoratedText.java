package chatterbox.data;

import javax.swing.text.AttributeSet;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DecoratedText
{
    /**  */
    public AttributeSet attributes;
    /**  */
    public String text;

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        return text;
    }
}
