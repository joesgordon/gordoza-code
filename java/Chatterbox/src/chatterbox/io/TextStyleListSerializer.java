package chatterbox.io;

import java.io.IOException;

import org.jutils.core.data.FontDescription;
import org.jutils.core.data.TextStyleList;
import org.jutils.core.data.TextStyleList.TextStyle;
import org.jutils.core.io.IDataSerializer;
import org.jutils.core.io.IDataStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TextStyleListSerializer implements IDataSerializer<TextStyleList>
{
    /**  */
    private final FontDescriptionSerializer fontSerializer;

    /***************************************************************************
     * 
     **************************************************************************/
    public TextStyleListSerializer()
    {
        this.fontSerializer = new FontDescriptionSerializer();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public TextStyleList read( IDataStream stream ) throws IOException
    {
        TextStyleList list = new TextStyleList();
        int styleCount = stream.readInt();

        for( int i = 0; i < styleCount; i++ )
        {
            int location = stream.readInt();
            int count = stream.readInt();
            FontDescription description = fontSerializer.read( stream );

            TextStyle style = new TextStyle( location, count, description );

            list.styles.add( style );
        }

        return list;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( TextStyleList data, IDataStream stream )
        throws IOException
    {
        stream.writeInt( data.styles.size() );

        for( TextStyle style : data.styles )
        {
            stream.writeInt( style.location );
            stream.writeInt( style.count );
            fontSerializer.write( style.description, stream );
        }
    }
}
