package org.mc.messages;

import java.util.Arrays;

public class CStringMessage implements MessageInterface
{
    private String messageString;

    public CStringMessage( byte[] bytes )
    {
        messageString = new String( bytes, 0, bytes.length - 1 );
    }

    public CStringMessage( String str )
    {
        messageString = str;
    }

    public String getMessageString()
    {
        return messageString;
    }

    @Override
    public byte[] toBytes()
    {
        byte[] strBytes = messageString.getBytes();
        byte[] msgBytes = Arrays.copyOf( strBytes, strBytes.length + 1 );
        return msgBytes;
    }
}
