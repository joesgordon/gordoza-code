package chatterbox.data;

import java.util.*;

import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import chatterbox.model.IMessageAttributeSet;
import chatterbox.model.IUser;

/**
 * 
 */
public class UiChatMessage extends DefaultMessage
{
    /**
     * @param doc
     * @param user
     * @param conversationId
     */
    public UiChatMessage( IUser sender, StyledDocument doc,
        String conversationId )
    {
        super( sender, getText( doc ), getAttributes( doc ), new Date(),
            conversationId, true );
    }

    /**
     * @param doc
     * @return
     */
    private static List<IMessageAttributeSet> getAttributes( StyledDocument doc )
    {
        ArrayList<IMessageAttributeSet> attributeSets = new ArrayList<IMessageAttributeSet>();
        return attributeSets;
    }

    /**
     * @param doc
     * @return
     */
    private static String getText( StyledDocument doc )
    {
        try
        {
            return doc.getText( 0, doc.getLength() );
        }
        catch( BadLocationException ex )
        {
            throw new RuntimeException( ex );
        }
    }
}
