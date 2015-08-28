package chatterbox.model;

import javax.swing.text.AttributeSet;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MessageAttributeSet
{
    /**  */
    public final int startPosition;

    /**  */
    public final int endPosition;

    /**  */
    public final AttributeSet attributes;

    /***************************************************************************
     * @param attributes
     **************************************************************************/
    public MessageAttributeSet( AttributeSet attributes, int start, int end )
    {
        this.startPosition = start;
        this.endPosition = end;
        this.attributes = attributes;
    }
}
