package chatterbox.io;

import java.io.IOException;

import org.jutils.data.TextStyleList;
import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AttributeSetSerializer implements IDataSerializer<TextStyleList>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public TextStyleList read( IDataStream stream ) throws IOException
    {
        // TODO Serialize attribute set
        return null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( TextStyleList data, IDataStream stream )
        throws IOException
    {
        // TODO Serialize attribute set
    }
}
