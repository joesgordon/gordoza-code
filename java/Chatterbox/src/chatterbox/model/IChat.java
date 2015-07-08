package chatterbox.model;

import chatterbox.data.MessageBody;
import chatterbox.data.UserAvailability;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IChat
{
    /***************************************************************************
     * @param msg
     **************************************************************************/
    public void sendMessage( MessageBody msg );

    /***************************************************************************
     * @param availability
     **************************************************************************/
    public void setAvailability( UserAvailability availability );
}
