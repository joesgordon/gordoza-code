package chatterbox.model;

import javax.swing.text.AttributeSet;

public interface IMessageAttributeSet
{
    public int getStartPosition();

    public int getEndPosition();

    public AttributeSet getAttributes();
}
