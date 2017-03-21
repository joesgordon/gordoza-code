package chatterbox.io;

import java.io.IOException;

import javax.swing.text.AttributeSet;

import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AttributeSetSerializer implements IDataSerializer<AttributeSet>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public AttributeSet read( IDataStream stream ) throws IOException
    {
        // TODO Serialize attribute set
        return null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( AttributeSet t, IDataStream stream ) throws IOException
    {
        // TODO Serialize attribute set
    }
}
